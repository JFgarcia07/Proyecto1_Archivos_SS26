import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LogInterface {
  idLog: number,
  idUsuario: string,
  nombreUsuario: string,
  accion: string,
  modulo: string,
  fecha: string,
  detalle: string
}

@Injectable({
  providedIn: 'root',
})
export class Log {
  private apiUrl = 'http://localhost:8080/api/logs';

  constructor(private http: HttpClient) { }

  /**
   * Consulta la bitacora; solo se envian los filtros con valor.
   */
  listarLogs(modulo: string, fechaInicio: string, fechaFin: string): Observable<LogInterface[]> {
    let parametros = new HttpParams();

    if (modulo !== '') {
      parametros = parametros.set('modulo', modulo);
    }
    if (fechaInicio !== '') {
      parametros = parametros.set('fechaInicio', fechaInicio);
    }
    if (fechaFin !== '') {
      parametros = parametros.set('fechaFin', fechaFin);
    }

    return this.http.get<LogInterface[]>(this.apiUrl, { params: parametros });
  }
}
