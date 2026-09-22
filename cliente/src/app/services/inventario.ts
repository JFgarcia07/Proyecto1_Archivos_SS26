import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductoInterface } from './producto';

export interface MovimientoInterface {
  idMovimiento: number,
  idProducto: string,
  nombreProducto: string,
  tipoMovimiento: string,
  cantidad: number,
  fecha: string,
  origen: string,
  idReferencia: number,
  tercero: string,
  idUsuario: string
}

@Injectable({
  providedIn: 'root',
})
export class Inventario {
  private apiUrl = 'http://localhost:8080/api/inventario';

  constructor(private http: HttpClient) { }

  listarExistencias(): Observable<ProductoInterface[]> {
    return this.http.get<ProductoInterface[]>(`${this.apiUrl}/existencias`);
  }

  listarAlertas(): Observable<ProductoInterface[]> {
    return this.http.get<ProductoInterface[]>(`${this.apiUrl}/alertas`);
  }

  obtenerStockMinimo(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stock-minimo`);
  }

  listarMovimientos(idProducto: string, tipo: string,
                    fechaInicio: string, fechaFin: string): Observable<MovimientoInterface[]> {
    let parametros = new HttpParams();

    if (idProducto !== '') {
      parametros = parametros.set('idProducto', idProducto);
    }
    if (tipo !== '') {
      parametros = parametros.set('tipo', tipo);
    }
    if (fechaInicio !== '') {
      parametros = parametros.set('fechaInicio', fechaInicio);
    }
    if (fechaFin !== '') {
      parametros = parametros.set('fechaFin', fechaFin);
    }

    return this.http.get<MovimientoInterface[]>(`${this.apiUrl}/movimientos`, { params: parametros });
  }
}
