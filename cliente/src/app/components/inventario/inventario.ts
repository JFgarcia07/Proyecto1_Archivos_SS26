import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Navbar } from '../navbar/navbar';
import { Inventario as InventarioService, MovimientoInterface } from '../../services/inventario';
import { ProductoInterface } from '../../services/producto';

@Component({
  selector: 'app-inventario',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './inventario.html',
  styleUrl: './inventario.css',
})
export class Inventario implements OnInit{
  existencias = signal<ProductoInterface[]>([]);
  alertas = signal<ProductoInterface[]>([]);
  movimientos = signal<MovimientoInterface[]>([]);
  stockMinimo = signal<number>(0);

  cargandoExistencias = signal<boolean>(true);
  cargandoMovimientos = signal<boolean>(true);
  mensajeError = signal<string>('');
  errorMovimientos = signal<string>('');

  filtroProducto: string = '';
  filtroTipo: string = '';
  fechaInicio: string = '';
  fechaFin: string = '';

  constructor(private inventarioService: InventarioService) {}

  ngOnInit(): void {
    this.cargarStockMinimo();
    this.cargarExistencias();
    this.cargarAlertas();
    this.cargarMovimientos();
  }

  cargarStockMinimo(): void {
    this.inventarioService.obtenerStockMinimo().subscribe({
      next: (valor) => {
        this.stockMinimo.set(valor);
      },
      error: (err) => {
        this.mensajeError.set('No se pudo obtener el stock mínimo');
      }
    });
  }

  cargarExistencias(): void {
    this.cargandoExistencias.set(true);

    this.inventarioService.listarExistencias().subscribe({
      next: (datos) => {
        this.existencias.set(datos);
        this.cargandoExistencias.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar las existencias');
        this.cargandoExistencias.set(false);
      }
    });
  }

  cargarAlertas(): void {
    this.inventarioService.listarAlertas().subscribe({
      next: (datos) => {
        this.alertas.set(datos);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar las alertas');
      }
    });
  }

  cargarMovimientos(): void {
    this.errorMovimientos.set('');

    if (this.fechaInicio !== '' && this.fechaFin !== '' && this.fechaInicio > this.fechaFin) {
      this.errorMovimientos.set('La fecha inicial no puede ser mayor que la fecha final');
      return;
    }

    this.cargandoMovimientos.set(true);

    this.inventarioService.listarMovimientos(
      this.filtroProducto, this.filtroTipo, this.fechaInicio, this.fechaFin
    ).subscribe({
      next: (datos) => {
        this.movimientos.set(datos);
        this.cargandoMovimientos.set(false);
      },
      error: (err) => {
        this.errorMovimientos.set('No se pudieron cargar los movimientos');
        this.cargandoMovimientos.set(false);
      }
    });
  }

  limpiarFiltros(): void {
    this.filtroProducto = '';
    this.filtroTipo = '';
    this.fechaInicio = '';
    this.fechaFin = '';
    this.cargarMovimientos();
  }

  /**
   * Muestra el historial de un producto especifico y lleva
   * la vista a la seccion de movimientos.
   */
  verMovimientosDe(idProducto: string): void {
    this.filtroProducto = idProducto;
    this.filtroTipo = '';
    this.fechaInicio = '';
    this.fechaFin = '';
    this.cargarMovimientos();

    const seccion = document.getElementById('seccion-movimientos');
    if (seccion !== null) {
      seccion.scrollIntoView({ behavior: 'smooth' });
    }
  }

  totalUnidades(): number {
    let total = 0;
    const lista = this.existencias();

    for (let i = 0; i < lista.length; i++) {
      total = total + lista[i].stock;
    }
    return total;
  }

  totalAgotados(): number {
    let total = 0;
    const lista = this.existencias();

    for (let i = 0; i < lista.length; i++) {
      if (lista[i].stock <= 0) {
        total = total + 1;
      }
    }
    return total;
  }

  estadoStock(stock: number): string {
    if (stock <= 0) {
      return 'AGOTADO';
    }
    if (stock <= this.stockMinimo()) {
      return 'BAJO';
    }
    return 'NORMAL';
  }

  claseEstado(stock: number): string {
    const estado = this.estadoStock(stock);

    if (estado === 'AGOTADO') {
      return 'bg-danger';
    }
    if (estado === 'BAJO') {
      return 'bg-warning text-dark';
    }
    return 'bg-success';
  }

  textoOrigen(movimiento: MovimientoInterface): string {
    if (movimiento.origen === 'COMPRA') {
      return 'Compra No. ' + movimiento.idReferencia;
    }
    return 'Venta No. ' + movimiento.idReferencia;
  }

  formatearFecha(fecha: string): string {
    if (!fecha) {
      return '';
    }
    return fecha.substring(0, 16);
  }
}
