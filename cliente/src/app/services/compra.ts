import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DetalleCompraInterface {
  idCompra: number,
  idProducto: string,
  cantidad: number,
  precioUnitario: number,
  nombreProducto: string
}

export interface CompraInterface {
  idCompra: number,
  idProveedor: string,
  idUsuario: string,
  fecha: string,
  total: number,
  nombreProveedor: string,
  detalles: DetalleCompraInterface[]
}

@Injectable({
  providedIn: 'root',
})
export class Compra {
  private apiUrl = 'http://localhost:8080/api/compras';

  constructor(private http: HttpClient) { }

  listarCompras(): Observable<CompraInterface[]> {
    return this.http.get<CompraInterface[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<CompraInterface> {
    return this.http.get<CompraInterface>(`${this.apiUrl}/${id}`);
  }

  registrarCompra(compra: CompraInterface): Observable<string> {
    return this.http.post(this.apiUrl, compra, { responseType: 'text' });
  }
}
