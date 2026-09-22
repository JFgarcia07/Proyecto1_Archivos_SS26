import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  
  idRol: number | null = null;
  
  constructor(private authService: Auth, private router: Router) {
    this.idRol = this.authService.obtenerIdRol();
  }

  esAdmin(): boolean {
    return this.idRol === 1;
  }

  esCompras(): boolean {
    return this.idRol === 1 || this.idRol === 2;
  }

  esInventario(): boolean {
    return this.idRol === 1 || this.idRol === 3;
  }
  
  esVentas(): boolean {
    return this.idRol === 1 || this.idRol === 4;
  }
  
  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
