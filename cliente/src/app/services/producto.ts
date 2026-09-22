import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductoInterface {
  idProducto: string,
  nombreProducto: string,
  categoria: string,
  precio: number,
  stock: number,
  activo: boolean
}

@Injectable({
  providedIn: 'root',
})
export class Producto {
  private apiUrl = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) { }

  listarProductos(): Observable<ProductoInterface[]> {
    return this.http.get<ProductoInterface[]>(this.apiUrl);
  }

  listarProductosInactivos(): Observable<ProductoInterface[]> {
    return this.http.get<ProductoInterface[]>(`${this.apiUrl}/inactivos`);
  }

  buscarPorId(id: string): Observable<ProductoInterface> {
    return this.http.get<ProductoInterface>(`${this.apiUrl}/${id}`);
  }

  crearProducto(producto: ProductoInterface): Observable<string> {
    return this.http.post(this.apiUrl, producto, { responseType: 'text' });
  }

  actualizarProducto(id: string, producto: ProductoInterface): Observable<string> {
    return this.http.put(`${this.apiUrl}/${id}`, producto, { responseType: 'text' });
  }

  desactivarProducto(id: string): Observable<string> {
    return this.http.put(`${this.apiUrl}/${id}/desactivar`, {}, { responseType: 'text' });
  }
}