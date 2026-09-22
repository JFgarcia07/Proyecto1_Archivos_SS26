import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Compra, CompraInterface, DetalleCompraInterface } from '../../services/compra';

@Component({
  selector: 'app-ver-compra',
  imports: [CommonModule, Navbar],
  templateUrl: './ver-compra.html',
  styleUrl: './ver-compra.css',
})
export class VerCompra implements OnInit{
  compra = signal<CompraInterface | null>(null);
  cargando = signal<boolean>(true);
  mensajeError = signal<string>('');

  constructor(
    private compraService: Compra,
    private router: Router,
    private ruta: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');

    if (id === null) {
      this.mensajeError.set('No se indicó la compra a consultar');
      this.cargando.set(false);
      return;
    }

    this.cargarCompra(parseInt(id, 10));
  }

  cargarCompra(id: number): void {
    this.cargando.set(true);

    this.compraService.buscarPorId(id).subscribe({
      next: (datos) => {
        this.compra.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudo cargar la compra');
        this.cargando.set(false);
      }
    });
  }

  calcularSubtotal(detalle: DetalleCompraInterface): number {
    return detalle.cantidad * detalle.precioUnitario;
  }

  formatearFecha(fecha: string): string {
    if (!fecha) {
      return '';
    }
    return fecha.substring(0, 10);
  }

  regresar(): void {
    this.router.navigate(['/compras']);
  }
}
