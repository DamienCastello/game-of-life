import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private api = '/api/game';

  constructor(private http: HttpClient) { }

  preview(pattern: string) {
    return this.http.post<boolean[][]>(`${this.api}/preview`, {
      pattern: pattern
    });
  }

  start(pattern: string, grid: boolean[][]) {
    return this.http.post<boolean[][]>(`${this.api}/start`, {
      pattern,
      grid
    });
  }

  next(grid: boolean[][]) {
    return this.http.post<boolean[][]>(`${this.api}/next`, grid);
  }
}