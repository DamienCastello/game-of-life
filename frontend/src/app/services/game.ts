import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private api = '/api/game';

  constructor(private http: HttpClient) { }

  // Charge un pattern prédéfini : renvoie la grille dimensionnée
  preview(pattern: string) {
    return this.http.post<boolean[][]>(`${this.api}/preview`, { pattern });
  }

  // Calcule la génération suivante à partir de la grille courante
  next(grid: boolean[][]) {
    return this.http.post<boolean[][]>(`${this.api}/next`, { grid });
  }
}
