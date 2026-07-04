import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

// Une règle du jeu, illustrée par un petit schéma 3x3 "avant -> après".
// `before` / `after` : 9 cases (true = vivante). La case centrale est
// l'index 4 (celle dont on décide le sort).
interface Rule {
  icon: string;
  title: string;
  neighbours: string;
  text: string;
  before: boolean[];
  after: boolean[];
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent implements OnInit, OnDestroy {

  // ---------------------------------------------------------------
  // Mini-simulation qui tourne en direct dans le hero.
  // Elle utilise exactement les mêmes règles que le jeu (grille "tore").
  // ---------------------------------------------------------------
  demoRows = 16;
  demoCols = 32;
  cells: boolean[] = [];      // grille aplatie : index = row * demoCols + col
  private timer: any;
  private stepCount = 0;

  ngOnInit() {
    this.seed();
    this.timer = setInterval(() => this.step(), 160);
  }

  ngOnDestroy() {
    // On coupe la simulation quand on quitte la page (pas de fuite).
    clearInterval(this.timer);
  }

  // Remplit la grille d'une "soupe" aléatoire (~28% de cellules vivantes).
  private seed() {
    this.cells = Array.from({ length: this.demoRows * this.demoCols }, () => Math.random() < 0.28);
    this.stepCount = 0;
  }

  // Calcule une génération selon les 3 règles de Conway.
  private step() {
    const total = this.demoRows * this.demoCols;
    const next = new Array<boolean>(total).fill(false);
    let alive = 0;

    for (let row = 0; row < this.demoRows; row++) {
      for (let col = 0; col < this.demoCols; col++) {
        const i = row * this.demoCols + col;
        const n = this.countNeighbours(row, col);
        // vivante : survit avec 2 ou 3 voisines ; morte : naît avec 3 voisines
        next[i] = this.cells[i] ? (n === 2 || n === 3) : (n === 3);
        if (next[i]) alive++;
      }
    }

    this.cells = next;
    this.stepCount++;

    // On relance une nouvelle soupe si ça s'éteint ou après un moment,
    // pour que la démo reste toujours vivante et variée.
    if (alive < total * 0.03 || this.stepCount > 260) {
      this.seed();
    }
  }

  // Compte les 8 voisines, avec des bords qui bouclent (grille "tore").
  private countNeighbours(row: number, col: number): number {
    let count = 0;
    for (let dr = -1; dr <= 1; dr++) {
      for (let dc = -1; dc <= 1; dc++) {
        if (dr === 0 && dc === 0) continue;
        const r = (row + dr + this.demoRows) % this.demoRows;
        const c = (col + dc + this.demoCols) % this.demoCols;
        if (this.cells[r * this.demoCols + c]) count++;
      }
    }
    return count;
  }

  trackByIndex(index: number) {
    return index;
  }

  // ---------------------------------------------------------------
  // Les 3 règles, avec leur petit schéma "avant -> après".
  // ---------------------------------------------------------------
  rules: Rule[] = [
    {
      icon: '💚',
      title: 'Survie',
      neighbours: '2 ou 3 voisines',
      text: 'Une cellule vivante entourée de 2 ou 3 voisines vivantes reste en vie.',
      before: [false, true, false, true, true, true, false, false, false],
      after: [false, false, false, false, true, false, false, false, false],
    },
    {
      icon: '💀',
      title: 'Mort',
      neighbours: '< 2 ou > 3 voisines',
      text: 'Trop seule (isolement) ou trop entourée (surpopulation), une cellule vivante meurt.',
      before: [false, true, false, false, true, false, false, false, false],
      after: [false, false, false, false, false, false, false, false, false],
    },
    {
      icon: '✨',
      title: 'Naissance',
      neighbours: 'exactement 3 voisines',
      text: 'Une case morte ayant exactement 3 voisines vivantes prend vie.',
      before: [false, true, false, true, false, true, false, false, false],
      after: [false, false, false, false, true, false, false, false, false],
    },
  ];
}
