import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Venta, VentaInterface, DetalleVentaInterface } from '../../services/venta';

@Component({
  selector: 'app-ver-venta',
  imports: [CommonModule, Navbar],
  templateUrl: './ver-venta.html',
  styleUrl: './ver-venta.css',
})
export class VerVenta implements OnInit {

  venta = signal<VentaInterface | null>(null);
  cargando = signal<boolean>(true);
  mensajeError = signal<string>('');

  constructor(
    private ventaService: Venta,
    private router: Router,
    private ruta: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');

    if (id === null) {
      this.mensajeError.set('No se indicó la venta a consultar');
      this.cargando.set(false);
      return;
    }

    this.cargarVenta(parseInt(id, 10));
  }

  cargarVenta(id: number): void {
    this.cargando.set(true);

    this.ventaService.buscarPorId(id).subscribe({
      next: (datos) => {
        this.venta.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudo cargar la venta');
        this.cargando.set(false);
      }
    });
  }

  descargarFactura(): void {
    const venta = this.venta();

    if (venta === null) {
      return;
    }

    this.ventaService.descargarFactura(venta.idVenta).subscribe({
      next: (archivo) => {
        this.ventaService.guardarPdf(archivo, 'factura_' + venta.idVenta + '.pdf');
      },
      error: (err) => {
        this.mensajeError.set('No se pudo descargar la factura');
      }
    });
  }

  calcularLinea(detalle: DetalleVentaInterface): number {
    return detalle.cantidad * detalle.precioUnitario;
  }

  formatearFecha(fecha: string): string {
    if (!fecha) {
      return '';
    }
    return fecha.substring(0, 10);
  }

  regresar(): void {
    this.router.navigate(['/ventas']);
  }
}