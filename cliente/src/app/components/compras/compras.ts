import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Compra, CompraInterface } from '../../services/compra';

@Component({
  selector: 'app-compras',
  imports: [CommonModule, Navbar, RouterLink],
  templateUrl: './compras.html',
  styleUrl: './compras.css',
})
export class Compras implements OnInit{
  compras = signal<CompraInterface[]>([]);
  cargando = signal<boolean>(true);
  mensajeError = signal<string>('');

  constructor(private compraService: Compra) {}

  ngOnInit(): void {
    this.cargarCompras();
  }

  cargarCompras(): void {
    this.cargando.set(true);
    this.mensajeError.set('');

    this.compraService.listarCompras().subscribe({
      next: (datos) => {
        this.compras.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar las compras');
        this.cargando.set(false);
      }
    });
  }

  formatearFecha(fecha: string): string {
    if (!fecha) {
      return '';
    }
    return fecha.substring(0, 10);
  }
}
