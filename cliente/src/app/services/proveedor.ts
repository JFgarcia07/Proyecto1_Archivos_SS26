import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProveedorInterface {
  idProveedor: string,
  nombreProveedor: string,
  ubicacion: string,
  activo: boolean
}

@Injectable({
  providedIn: 'root',
})
export class Proveedor {
  private apiUrl = 'http://localhost:8080/api/proveedores';

  constructor(private http: HttpClient) { }

  listarProveedores(): Observable<ProveedorInterface[]> {
    return this.http.get<ProveedorInterface[]>(this.apiUrl);
  }

  listarProveedoresInactivos(): Observable<ProveedorInterface[]> {
    return this.http.get<ProveedorInterface[]>(`${this.apiUrl}/inactivos`);
  }

  buscarPorId(id: string): Observable<ProveedorInterface> {
    return this.http.get<ProveedorInterface>(`${this.apiUrl}/${id}`);
  }

  crearProveedor(proveedor: ProveedorInterface): Observable<string> {
    return this.http.post(this.apiUrl, proveedor, { responseType: 'text' });
  }

  actualizarProveedor(id: string, proveedor: ProveedorInterface): Observable<string> {
    return this.http.put(`${this.apiUrl}/${id}`, proveedor, { responseType: 'text' });
  }

  desactivarProveedor(id: string): Observable<string> {
    return this.http.put(`${this.apiUrl}/${id}/desactivar`, {}, { responseType: 'text' });
  }
}
