import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Producto, ProductoInterface } from '../../services/producto';

@Component({
  selector: 'app-crear-producto',
  imports: [CommonModule, FormsModule, RouterLink, Navbar],
  templateUrl: './crear-producto.html',
  styleUrl: './crear-producto.css',
})
export class CrearProducto {
  nombreProducto: string = '';
  categoria: string = ''
  precio: number | null = null;

  mensajeError: string = '';
  guardado: boolean = false;

  categorias: string[] = ['Escritorios', 'Sillas', 'Mesas', 'Estanteria', 'Sofás', 'Accesorios'];

  constructor(private productoService: Producto, private router: Router) {}

  guardar(): void {
    this.mensajeError = '';

    if (this.nombreProducto.trim() === '') {
      this.mensajeError = 'El nombre del producto es obligatorio';
      return;
    }
    if (this.categoria.trim() === '') {
      this.mensajeError = 'Debe seleccionar una categoría';
      return;
    }
    if (this.precio === null || this.precio < 0) {
      this.mensajeError = 'El precio debe ser un valor mayor o igual a cero';
      return;
    }

    const nuevoProducto: ProductoInterface = {
      idProducto: '',
      nombreProducto: this.nombreProducto,
      categoria: this.categoria,
      precio: this.precio,
      stock: 0,
      activo: true
    };

    this.guardado = true;

    this.productoService.crearProducto(nuevoProducto).subscribe({
      next: (respuesta) => {
        this.guardado = false;
        this.router.navigate(['/productos']);
      },
      error: (err) => {
        this.guardado = false;
        if (err.error) {
          this.mensajeError = err.error;
        } else {
          this.mensajeError = 'No se pudo guardar el producto';
        }
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/productos']);
  }
}
