import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GameService } from '../../services/game';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './game.html',
  styleUrls: ['./game.css'],
})
export class GameComponent {

  grid: boolean[][] = [];
  selectedPattern = 'blinker';
  interval: any;
  running = false;

  constructor(private gameService: GameService) {}

  start() {
    this.gameService.start(this.selectedPattern).subscribe(res => {
      this.grid = res;
      this.run();
    });
  }

  run() {
    this.running = true;

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
  }
}