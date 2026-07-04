import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from '../../services/game';

interface PatternButton {
  id: string;
  label: string;
}

interface PatternGroup {
  title: string;
  patterns: PatternButton[];
}

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game.html',
  styleUrls: ['./game.css'],
})
export class GameComponent {

  // Catalogue affiché, groupé par catégorie
  groups: PatternGroup[] = [
    {
      title: 'Basiques',
      patterns: [
        { id: 'blinker', label: '🔁 Blinker' },
        { id: 'glider', label: '🚀 Glider' },
        { id: 'block', label: '🟦 Block' },
      ],
    },
    {
      title: 'Oscillateurs',
      patterns: [
        { id: 'pulsar', label: '💠 Pulsar' },
        { id: 'pentadecathlon', label: '🫀 Pentadécathlon' },
      ],
    },
    {
      title: 'Vaisseaux',
      patterns: [
        { id: 'lwss', label: '🛸 LWSS' },
        { id: 'mwss', label: '🛰️ MWSS' },
        { id: 'hwss', label: '🚁 HWSS' },
      ],
    },
    {
      title: 'Usines',
      patterns: [
        { id: 'gosper', label: '🏭 Gosper Gun' },
      ],
    },
    {
      title: 'Méthuselahs',
      patterns: [
        { id: 'rpentomino', label: '🌋 R-pentomino' },
        { id: 'acorn', label: '🌰 Acorn' },
        { id: 'diehard', label: '💀 Diehard' },
      ],
    },
  ];

  grid: boolean[][] = [];

  rows = 20;
  cols = 20;

  // Bornes de la barre de dimensions (mode custom)
  minSize = 5;
  maxSize = 80;

  currentPattern = 'custom';

  isCustomMode = true;
  isDrawingMode = true;

  torus = true;

  // Vitesse de la simulation, en générations par seconde
  speed = 3;
  minSpeed = 1;
  maxSpeed = 20;

  interval: any;
  running = false;

  constructor(private gameService: GameService) { }

  ngOnInit() {
    this.resetToCustom();
  }

  // Taille de cellule (px), adaptée à la grille pour rester lisible.
  // Champ recalculé seulement quand la grille change de taille, pour ne
  // pas refaire le calcul à chaque cellule et à chaque génération.
  cellSize = 22;

  private updateCellSize() {
    const n = Math.max(this.rows, this.cols);
    this.cellSize = Math.max(6, Math.min(22, Math.floor(560 / n)));
  }

  // Délai entre deux générations (ms), déduit de la vitesse choisie
  get intervalMs(): number {
    return Math.round(1000 / this.speed);
  }

  // La vitesse est réglable à tout moment ; si la simulation tourne, on
  // relance l'intervalle pour appliquer la nouvelle cadence immédiatement.
  setSpeed(value: number) {
    this.speed = Math.max(this.minSpeed, Math.min(this.maxSpeed, Math.round(value) || this.minSpeed));
    if (this.running) {
      clearInterval(this.interval);
      this.run();
    }
  }

  private buildEmptyGrid(rows: number, cols: number): boolean[][] {
    return Array.from({ length: rows }, () => Array(cols).fill(false));
  }

  private resetToCustom() {
    this.currentPattern = 'custom';
    this.isCustomMode = true;
    this.isDrawingMode = true;
    this.torus = true;
    this.rows = 20;
    this.cols = 20;
    this.grid = this.buildEmptyGrid(this.rows, this.cols);
    this.updateCellSize();
  }

  selectPattern(pattern: string) {
    // Changer de pattern en pleine simulation l'arrête automatiquement
    if (this.running) this.stop();

    this.currentPattern = pattern;

    if (pattern === 'custom') {
      this.resetToCustom();
      return;
    }

    // Pattern prédéfini : le backend renvoie la grille dimensionnée + le tore adapté
    this.isCustomMode = false;
    this.isDrawingMode = false;

    this.gameService.preview(pattern).subscribe(state => {
      this.grid = state.grid;
      this.rows = state.grid.length;
      this.cols = state.grid[0]?.length ?? 0;
      this.torus = state.torus;
      this.updateCellSize();
    });
  }

  trackByRow(index: number) {
    return index;
  }

  trackByCell(index: number) {
    return index;
  }

  toggleCell(i: number, j: number) {
    if (!this.isDrawingMode || this.running) return;
    this.grid[i][j] = !this.grid[i][j];
  }

  // Le mode tore peut être basculé à tout moment, même pendant la simulation
  toggleTorus() {
    this.torus = !this.torus;
  }

  setRows(value: number) {
    this.resize(this.clampSize(value), this.cols);
  }

  setCols(value: number) {
    this.resize(this.rows, this.clampSize(value));
  }

  private clampSize(value: number): number {
    return Math.max(this.minSize, Math.min(this.maxSize, Math.round(value) || this.minSize));
  }

  // Redimensionne la grille en conservant le dessin existant (recadré)
  private resize(newRows: number, newCols: number) {
    const resized = Array.from({ length: newRows }, (_, i) =>
      Array.from({ length: newCols }, (_, j) => this.grid[i]?.[j] ?? false)
    );
    this.grid = resized;
    this.rows = newRows;
    this.cols = newCols;
    this.updateCellSize();
  }

  clearGrid() {
    if (this.running) return;
    this.grid = this.buildEmptyGrid(this.rows, this.cols);
  }

  start() {
    if (this.running) return;
    this.running = true;
    this.isDrawingMode = false;
    this.run();
  }

  run() {
    this.interval = setInterval(() => {
      if (!this.running) return;

      this.gameService.next(this.grid, this.torus).subscribe(res => {
        // Une réponse arrivée après un stop/switch ne doit pas écraser
        // la grille du nouveau pattern qu'on vient de charger.
        if (!this.running) return;
        this.grid = res;
      });
    }, this.intervalMs);
  }

  stop() {
    this.running = false;
    clearInterval(this.interval);

    // On repasse en dessin uniquement pour une grille custom
    this.isDrawingMode = this.isCustomMode;
  }
}
