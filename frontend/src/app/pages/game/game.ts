import { AfterViewInit, Component, ElementRef, HostListener, ViewChild } from '@angular/core';
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
export class GameComponent implements AfterViewInit {

  // Référence au plateau, pour mesurer l'espace réellement disponible
  @ViewChild('board') boardRef?: ElementRef<HTMLElement>;

  // Référence au menu déroulant mobile, pour le fermer si on clique dehors
  @ViewChild('patternMenu') patternMenuRef?: ElementRef<HTMLElement>;

  // Ouverture du menu déroulant des patterns (mobile)
  menuOpen = false;

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

  // État central du jeu, détenu côté client : la grille de cellules et ses
  // dimensions. Le backend est sans état → on lui renvoie cette grille à
  // chaque génération (voir run() / GameService.next).
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

  // Taille de cellule (px). Recalculée seulement quand la grille ou
  // l'écran change (pas à chaque cellule ni à chaque génération).
  cellSize = 22;

  // Ajuste la taille des cellules pour que toute la grille tienne dans
  // l'espace réellement disponible (mesuré sur le plateau), quel que soit
  // le device et l'orientation (portrait / paysage).
  private updateCellSize() {
    const board = this.boardRef?.nativeElement;
    // Avant que la vue soit prête, on retombe sur la taille de la fenêtre.
    const availWidth = (board?.clientWidth ?? window.innerWidth) - 24;
    const availHeight = (board?.clientHeight ?? window.innerHeight) - 24;

    const byWidth = Math.floor(availWidth / this.cols);
    const byHeight = Math.floor(availHeight / this.rows);

    // On prend la plus petite des deux pour tenir dans les deux dimensions,
    // borné entre 3px (grande grille sur petit écran) et 24px.
    this.cellSize = Math.max(3, Math.min(24, Math.min(byWidth, byHeight)));
  }

  // La vue est prête : on mesure le plateau. Différé d'un tick pour ne pas
  // modifier une valeur déjà lue dans le cycle de détection courant.
  ngAfterViewInit() {
    setTimeout(() => this.updateCellSize());
  }

  // Recalcule à chaque redimensionnement / rotation de l'écran.
  @HostListener('window:resize')
  onResize() {
    this.updateCellSize();
  }

  @HostListener('window:orientationchange')
  onOrientationChange() {
    // On laisse le layout se stabiliser après la rotation avant de mesurer.
    setTimeout(() => this.updateCellSize(), 150);
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

  // ----- Menu déroulant des patterns (mobile) -----

  // Libellé du pattern courant, affiché sur le bouton du menu.
  get currentPatternLabel(): string {
    if (this.currentPattern === 'custom') return '✏️ Custom';
    for (const group of this.groups) {
      const p = group.patterns.find(x => x.id === this.currentPattern);
      if (p) return p.label;
    }
    return '✏️ Custom';
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  // Choix d'un pattern depuis le menu : on ferme puis on sélectionne.
  pickPattern(pattern: string) {
    this.menuOpen = false;
    this.selectPattern(pattern);
  }

  // Ferme le menu si on clique en dehors (le clic sur le bouton, lui, est
  // à l'intérieur du menu, donc il ne le referme pas immédiatement).
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    if (!this.menuOpen) return;
    const menu = this.patternMenuRef?.nativeElement;
    if (menu && !menu.contains(event.target as Node)) {
      this.menuOpen = false;
    }
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
