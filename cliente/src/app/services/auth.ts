import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginResponse {
  succes: boolean;
  token: string;
  idUsuario: string;
  idRol: number;
}

@Injectable({
  providedIn: 'root'
})
export class Auth {

  private apiUrl = 'http://localhost:8080/api/auth';
  private platformId = inject(PLATFORM_ID);

  constructor(private http: HttpClient) { }

  private esNavegador(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, {
      email: email,
      password: password
    });
  }

  guardarSesion(respuesta: LoginResponse): void {
    if (!this.esNavegador()) {
      return;
    }
    localStorage.setItem('token', respuesta.token);
    localStorage.setItem('idUsuario', respuesta.idUsuario);
    localStorage.setItem('idRol', respuesta.idRol.toString());
  }

  obtenerToken(): string | null {
    if (!this.esNavegador()) {
      return null;
    }
    return localStorage.getItem('token');
  }

  obtenerIdRol(): number | null {
    if (!this.esNavegador()) {
      return null;
    }
    const idRol = localStorage.getItem('idRol');
    return idRol ? parseInt(idRol, 10) : null;
  }

  estadoLogin(): boolean {
    return this.obtenerToken() !== null;
  }

  obtenerIdUsuario(): string | null {
    if (!this.esNavegador()) {
      return null;
    }
    return localStorage.getItem('idUsuario');
  }

  cerrarSesion(): void {
    if (!this.esNavegador()) {
      return;
    }
    localStorage.removeItem('token');
    localStorage.removeItem('idUsuario');
    localStorage.removeItem('idRol');
  }
}