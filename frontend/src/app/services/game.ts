import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// État d'une partie renvoyé par le backend quand on charge un pattern.
// - grid  : la grille de cellules (true = vivante), déjà à la bonne taille
// - torus : mode de bord recommandé (true = les bords bouclent)
export interface GameState {
  grid: boolean[][];
  torus: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private api = '/api/game';

  constructor(private http: HttpClient) { }

  // Charge un pattern prédéfini : renvoie la grille dimensionnée + le mode tore
  preview(pattern: string) {
    return this.http.post<GameState>(`${this.api}/preview`, { pattern });
  }

  // Calcule la génération suivante à partir de la grille et du mode tore courants
  next(grid: boolean[][], torus: boolean) {
    return this.http.post<boolean[][]>(`${this.api}/next`, { grid, torus });
  }
}
