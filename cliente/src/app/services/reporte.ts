import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompraInterface } from './compra';
import { VentaInterface } from './venta';

export interface ReporteFilaInterface {
  codigo: string,
  nombre: string,
  detalle: string,
  cantidad: number,
  conteo: number,
  monto: number
}

export interface ResumenPeriodoInterface {
  periodo: string,
  numeroVentas: number,
  subtotal: number,
  iva: number,
  total: number
}

@Injectable({
  providedIn: 'root',
})
export class Reporte {
  private apiUrl = 'http://localhost:8080/api/reportes';

  constructor(private http: HttpClient) { }

  /**
   * Arma los parametros de fecha; solo se envian los que tienen valor.
   */
  private construirParametros(fechaInicio: string, fechaFin: string): HttpParams {
    let parametros = new HttpParams();

    if (fechaInicio !== '') {
      parametros = parametros.set('fechaInicio', fechaInicio);
    }
    if (fechaFin !== '') {
      parametros = parametros.set('fechaFin', fechaFin);
    }
    return parametros;
  }

  productosMasVendidos(fechaInicio: string, fechaFin: string): Observable<ReporteFilaInterface[]> {
    return this.http.get<ReporteFilaInterface[]>(`${this.apiUrl}/productos-mas-vendidos`,
      { params: this.construirParametros(fechaInicio, fechaFin) });
  }

  productosMenorExistencia(): Observable<ReporteFilaInterface[]> {
    return this.http.get<ReporteFilaInterface[]>(`${this.apiUrl}/productos-menor-existencia`);
  }

  productosMasMovimientos(fechaInicio: string, fechaFin: string): Observable<ReporteFilaInterface[]> {
    return this.http.get<ReporteFilaInterface[]>(`${this.apiUrl}/productos-mas-movimientos`,
      { params: this.construirParametros(fechaInicio, fechaFin) });
  }

  compras(fechaInicio: string, fechaFin: string): Observable<CompraInterface[]> {
    return this.http.get<CompraInterface[]>(`${this.apiUrl}/compras`,
      { params: this.construirParametros(fechaInicio, fechaFin) });
  }

  topProveedores(fechaInicio: string, fechaFin: string): Observable<ReporteFilaInterface[]> {
    return this.http.get<ReporteFilaInterface[]>(`${this.apiUrl}/top-proveedores`,
      { params: this.construirParametros(fechaInicio, fechaFin) });
  }

  productosMasAdquiridos(fechaInicio: string, fechaFin: string): Observable<ReporteFilaInterface[]> {
    return this.http.get<ReporteFilaInterface[]>(`${this.apiUrl}/productos-mas-adquiridos`,
      { params: this.construirParametros(fechaInicio, fechaFin) });
  }

  ventas(fechaInicio: string, fechaFin: string): Observable<VentaInterface[]> {
    return this.http.get<VentaInterface[]>(`${this.apiUrl}/ventas`,
      { params: this.construirParametros(fechaInicio, fechaFin) });
  }

  topClientes(fechaInicio: string, fechaFin: string): Observable<ReporteFilaInterface[]> {
    return this.http.get<ReporteFilaInterface[]>(`${this.apiUrl}/top-clientes`,
      { params: this.construirParametros(fechaInicio, fechaFin) });
  }

  productosMayoresIngresos(fechaInicio: string, fechaFin: string): Observable<ReporteFilaInterface[]> {
    return this.http.get<ReporteFilaInterface[]>(`${this.apiUrl}/productos-mayores-ingresos`,
      { params: this.construirParametros(fechaInicio, fechaFin) });
  }

  resumenVentas(agrupacion: string, fechaInicio: string, fechaFin: string): Observable<ResumenPeriodoInterface[]> {
    const parametros = this.construirParametros(fechaInicio, fechaFin).set('agrupacion', agrupacion);
    return this.http.get<ResumenPeriodoInterface[]>(`${this.apiUrl}/resumen-ventas`, { params: parametros });
  }
}