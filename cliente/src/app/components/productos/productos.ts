import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Producto, ProductoInterface } from '../../services/producto';

@Component({
  selector: 'app-productos',
  imports: [CommonModule, Navbar, RouterLink],
  templateUrl: './productos.html',
  styleUrl: './productos.css',
})
export class Productos implements OnInit {

  productos = signal<ProductoInterface[]>([]);
  productosInactivos = signal<ProductoInterface[]>([]);
  cargando = signal<boolean>(true);
  cargandoInactivos = signal<boolean>(true);
  mensajeError = signal<string>('');

  constructor(private productoService: Producto) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarProductosInactivos();
  }

  cargarProductos(): void {
    this.cargando.set(true);
    this.mensajeError.set('');

    this.productoService.listarProductos().subscribe({
      next: (datos) => {
        this.productos.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar los productos');
        this.cargando.set(false);
      }
    });
  }

  cargarProductosInactivos(): void {
    this.cargandoInactivos.set(true);

    this.productoService.listarProductosInactivos().subscribe({
      next: (datos) => {
        this.productosInactivos.set(datos);
        this.cargandoInactivos.set(false);
      },
      error: (err) => {
        this.cargandoInactivos.set(false);
      }
    });
  }

  desactivar(idProducto: string, nombreProducto: string): void {
    const confirmado = confirm(
      `¿Desactivar el producto "${nombreProducto}"?\n\n` +
      `El producto dejará de mostrarse en el catálogo, pero su historial de compras y ventas se conservará.`
    );

    if (!confirmado) {
      return;
    }

    this.productoService.desactivarProducto(idProducto).subscribe({
      next: (respuesta) => {
        this.cargarProductos();
        this.cargarProductosInactivos();
      },
      error: (err) => {
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo desactivar el producto');
        }
      }
    });
  }
}