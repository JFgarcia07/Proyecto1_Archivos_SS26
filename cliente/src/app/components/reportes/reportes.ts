import { Component, OnInit, signal, WritableSignal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { Navbar } from '../navbar/navbar';
import { Reporte, ReporteFilaInterface, ResumenPeriodoInterface } from '../../services/reporte';
import { Producto, ProductoInterface } from '../../services/producto';
import { Inventario, MovimientoInterface } from '../../services/inventario';
import { CompraInterface } from '../../services/compra';
import { VentaInterface } from '../../services/venta';

@Component({
  selector: 'app-reportes',
  imports: [CommonModule, FormsModule, RouterLink, Navbar],
  templateUrl: './reportes.html',
  styleUrl: './reportes.css',
})
export class Reportes implements OnInit {

  pestana = signal<string>('PRODUCTOS');

  fechaInicio: string = '';
  fechaFin: string = '';
  agrupacion: string = 'MES';
  productoHistorial: string = '';

  mensajeError = signal<string>('');

  // Productos e inventario
  masVendidos = signal<ReporteFilaInterface[]>([]);
  menorExistencia = signal<ReporteFilaInterface[]>([]);
  masMovimientos = signal<ReporteFilaInterface[]>([]);
  productos = signal<ProductoInterface[]>([]);
  historial = signal<MovimientoInterface[]>([]);

  // Compras y proveedores
  compras = signal<CompraInterface[]>([]);
  topProveedores = signal<ReporteFilaInterface[]>([]);
  masAdquiridos = signal<ReporteFilaInterface[]>([]);

  // Ventas y clientes
  ventas = signal<VentaInterface[]>([]);
  topClientes = signal<ReporteFilaInterface[]>([]);
  mayoresIngresos = signal<ReporteFilaInterface[]>([]);
  resumen = signal<ResumenPeriodoInterface[]>([]);

  constructor(
    private reporteService: Reporte,
    private productoService: Producto,
    private inventarioService: Inventario
  ) {}

  ngOnInit(): void {
    this.cargarListaProductos();
    this.cargarReportes();
  }

  cambiarPestana(nombre: string): void {
    this.pestana.set(nombre);
  }

  /**
   * Ejecuta una consulta y guarda el resultado en el signal indicado.
   * Evita repetir el mismo bloque subscribe para cada reporte.
   */
  private cargar<T>(peticion: Observable<T[]>, destino: WritableSignal<T[]>): void {
    peticion.subscribe({
      next: (datos) => {
        destino.set(datos);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar algunos reportes');
      }
    });
  }

  cargarReportes(): void {
    this.mensajeError.set('');

    if (this.fechaInicio !== '' && this.fechaFin !== '' && this.fechaInicio > this.fechaFin) {
      this.mensajeError.set('La fecha inicial no puede ser mayor que la fecha final');
      return;
    }

    const inicio = this.fechaInicio;
    const fin = this.fechaFin;

    this.cargar(this.reporteService.productosMasVendidos(inicio, fin), this.masVendidos);
    this.cargar(this.reporteService.productosMenorExistencia(), this.menorExistencia);
    this.cargar(this.reporteService.productosMasMovimientos(inicio, fin), this.masMovimientos);

    this.cargar(this.reporteService.compras(inicio, fin), this.compras);
    this.cargar(this.reporteService.topProveedores(inicio, fin), this.topProveedores);
    this.cargar(this.reporteService.productosMasAdquiridos(inicio, fin), this.masAdquiridos);

    this.cargar(this.reporteService.ventas(inicio, fin), this.ventas);
    this.cargar(this.reporteService.topClientes(inicio, fin), this.topClientes);
    this.cargar(this.reporteService.productosMayoresIngresos(inicio, fin), this.mayoresIngresos);

    this.cargarResumen();
    this.cargarHistorial();
  }

  cargarResumen(): void {
    this.cargar(
      this.reporteService.resumenVentas(this.agrupacion, this.fechaInicio, this.fechaFin),
      this.resumen
    );
  }

  cargarListaProductos(): void {
    this.cargar(this.productoService.listarProductos(), this.productos);
  }

  cargarHistorial(): void {
    if (this.productoHistorial === '') {
      this.historial.set([]);
      return;
    }
    this.cargar(
      this.inventarioService.listarMovimientos(this.productoHistorial, '', this.fechaInicio, this.fechaFin),
      this.historial
    );
  }

  limpiarFiltros(): void {
    this.fechaInicio = '';
    this.fechaFin = '';
    this.cargarReportes();
  }

  totalCompras(): number {
    let total = 0;
    const lista = this.compras();
    for (let i = 0; i < lista.length; i++) {
      total = total + lista[i].total;
    }
    return total;
  }

  totalVentas(): number {
    let total = 0;
    const lista = this.ventas();
    for (let i = 0; i < lista.length; i++) {
      total = total + lista[i].total;
    }
    return total;
  }

  totalResumenVentas(): number {
    let total = 0;
    const lista = this.resumen();
    for (let i = 0; i < lista.length; i++) {
      total = total + lista[i].numeroVentas;
    }
    return total;
  }

  totalResumenMonto(): number {
    let total = 0;
    const lista = this.resumen();
    for (let i = 0; i < lista.length; i++) {
      total = total + lista[i].total;
    }
    return total;
  }

  moneda(valor: number): string {
    return 'Q ' + valor.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  fecha(valor: string): string {
    if (!valor) {
      return '';
    }
    return valor.substring(0, 10);
  }
}