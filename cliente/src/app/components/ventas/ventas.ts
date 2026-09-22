import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Venta, VentaInterface } from '../../services/venta';

@Component({
  selector: 'app-ventas',
  imports: [CommonModule, Navbar, RouterLink],
  templateUrl: './ventas.html',
  styleUrl: './ventas.css',
})
export class Ventas {

 ventas = signal<VentaInterface[]>([]);
  cargando = signal<boolean>(true);
  mensajeError = signal<string>('');

  constructor(private ventaService: Venta) {}

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas(): void {
    this.cargando.set(true);
    this.mensajeError.set('');

    this.ventaService.listarVentas().subscribe({
      next: (datos) => {
        this.ventas.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar las ventas');
        this.cargando.set(false);
      }
    });
  }

  descargarFactura(idVenta: number): void {
    this.ventaService.descargarFactura(idVenta).subscribe({
      next: (archivo) => {
        this.ventaService.guardarPdf(archivo, 'factura_' + idVenta + '.pdf');
      },
      error: (err) => {
        this.mensajeError.set('No se pudo descargar la factura');
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
