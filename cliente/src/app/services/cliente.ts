import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ClienteInterface {
  idCliente: number,
  nombre: string,
  nit: string,
  saldo: number,
  activo: boolean
}


@Injectable({
  providedIn: 'root',
})
export class Cliente {
  private apiUrl = 'http://localhost:8080/api/clientes';

  constructor(private http: HttpClient) { }

  listarClientes(): Observable<ClienteInterface[]> {
    return this.http.get<ClienteInterface[]>(this.apiUrl);
  }

  listarClientesInactivos(): Observable<ClienteInterface[]> {
    return this.http.get<ClienteInterface[]>(`${this.apiUrl}/inactivos`);
  }

  buscarPorId(id: number): Observable<ClienteInterface> {
    return this.http.get<ClienteInterface>(`${this.apiUrl}/${id}`);
  }

  crearCliente(cliente: ClienteInterface): Observable<string> {
    return this.http.post(this.apiUrl, cliente, { responseType: 'text' });
  }

  actualizarCliente(id: number, cliente: ClienteInterface): Observable<string> {
    return this.http.put(`${this.apiUrl}/${id}`, cliente, { responseType: 'text' });
  }

  desactivarCliente(id: number): Observable<string> {
    return this.http.put(`${this.apiUrl}/${id}/desactivar`, {}, { responseType: 'text' });
  }
}
