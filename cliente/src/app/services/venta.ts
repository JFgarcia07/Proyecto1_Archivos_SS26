import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DetalleVentaInterface {
  idVenta: number,
  idProducto: string,
  cantidad: number,
  precioUnitario: number,
  nombreProducto: string
}

export interface VentaInterface {
  idVenta: number,
  idCliente: number,
  idUsuario: string,
  fecha: string,
  subtotal: number,
  iva: number,
  total: number,
  nombreCliente: string,
  nitCliente: string,
  nombreUsuario: string,
  detalles: DetalleVentaInterface[]
}

@Injectable({
  providedIn: 'root',
})
export class Venta {
  private apiUrl = 'http://localhost:8080/api/ventas';

  constructor(private http: HttpClient) { }

  listarVentas(): Observable<VentaInterface[]> {
    return this.http.get<VentaInterface[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<VentaInterface> {
    return this.http.get<VentaInterface>(`${this.apiUrl}/${id}`);
  }

  registrarVenta(venta: VentaInterface): Observable<string> {
    return this.http.post(this.apiUrl, venta, { responseType: 'text' });
  }

  descargarFactura(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/factura`, { responseType: 'blob' });
  }

  /**
   * Guarda en el equipo del usuario el archivo PDF recibido.
   */
  guardarPdf(archivo: Blob, nombre: string): void {
    const url = URL.createObjectURL(archivo);
    const enlace = document.createElement('a');
    enlace.href = url;
    enlace.download = nombre;
    enlace.click();
    setTimeout(() => URL.revokeObjectURL(url), 1000);
  }
}
