import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Navbar } from '../navbar/navbar';
import { Log, LogInterface } from '../../services/log';

@Component({
  selector: 'app-bitacora',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './bitacora.html',
  styleUrl: './bitacora.css',
})
export class Bitacora implements OnInit {

  modulo: string = '';
  fechaInicio: string = '';
  fechaFin: string = '';

  modulos: string[] = [
    'AUTENTICACION',
    'PRODUCTOS',
    'PROVEEDORES',
    'CLIENTES',
    'COMPRAS',
    'VENTAS'
  ];

  logs = signal<LogInterface[]>([]);
  cargando = signal<boolean>(true);
  mensajeError = signal<string>('');

  constructor(private logService: Log) {}

  ngOnInit(): void {
    this.cargarLogs();
  }

  cargarLogs(): void {
    this.mensajeError.set('');

    if (this.fechaInicio !== '' && this.fechaFin !== '' && this.fechaInicio > this.fechaFin) {
      this.mensajeError.set('La fecha inicial no puede ser mayor que la fecha final');
      return;
    }

    this.cargando.set(true);

    this.logService.listarLogs(this.modulo, this.fechaInicio, this.fechaFin).subscribe({
      next: (datos) => {
        this.logs.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudo cargar la bitácora');
        this.cargando.set(false);
      }
    });
  }

  limpiarFiltros(): void {
    this.modulo = '';
    this.fechaInicio = '';
    this.fechaFin = '';
    this.cargarLogs();
  }

  formatearFecha(fecha: string): string {
    if (!fecha) {
      return '';
    }
    return fecha.substring(0, 19);
  }

  /**
   * Color del distintivo segun el tipo de accion.
   */
  claseAccion(accion: string): string {
    if (accion === 'CREAR' || accion === 'REGISTRAR') {
      return 'bg-success';
    }
    if (accion === 'ACTUALIZAR') {
      return 'bg-primary';
    }
    if (accion === 'DESACTIVAR') {
      return 'bg-danger';
    }
    if (accion === 'GENERAR_FACTURA') {
      return 'bg-info text-dark';
    }
    return 'bg-secondary';
  }
}
