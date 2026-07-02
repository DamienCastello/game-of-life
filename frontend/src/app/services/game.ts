import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private api = 'http://localhost:8080/api/game';

  constructor(private http: HttpClient) { }

  start(pattern: string) {
    return this.http.post<boolean[][]>(`${this.api}/start`, {
      pattern: pattern
    });
  }

  next() {
    return this.http.get<boolean[][]>(`${this.api}/next`);
  }
}