import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from '../../services/game';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game.html',
  styleUrls: ['./game.css'],
})
export class GameComponent {

  grid: boolean[][] = [];

  rows = 20;
  cols = 20;

  currentPattern: string = 'custom';

  isCustomMode = true;
  isDrawingMode = true;

  interval: any;
  running = false;

  constructor(private gameService: GameService) { }

  ngOnInit() {
    this.initEmptyGrid();
  }

  initEmptyGrid() {
    this.grid = Array.from({ length: this.rows }, () =>
      Array(this.cols).fill(false)
    );
  }

  selectPattern(pattern: string) {

    if (this.running) return;

    this.currentPattern = pattern;
    this.isCustomMode = (pattern === 'custom');

    if (this.isCustomMode) {
      this.initEmptyGrid();
      this.isDrawingMode = true;
      return;
    }

    this.isDrawingMode = false;

    this.gameService.preview(pattern).subscribe(grid => {
      console.log(grid);
      this.grid = grid;
    });
  }

  toggleCell(i: number, j: number) {
    if (!this.isDrawingMode || this.running) return;

    this.grid[i][j] = !this.grid[i][j];
  }

  start() {
    this.running = true;
    this.isDrawingMode = false;
    this.isCustomMode = false;

    this.gameService.start(this.currentPattern, this.grid).subscribe(res => {
      this.grid = res;
      this.run();
    });
  }

  run() {
    this.interval = setInterval(() => {

      if (!this.running) return;

      this.gameService.next().subscribe(res => {
        this.grid = res;
      });

    }, 300);
  }

  stop() {
    this.running = false;
    clearInterval(this.interval);

    this.isCustomMode = (this.currentPattern === 'custom');
    this.isDrawingMode = this.isCustomMode;
  }
}