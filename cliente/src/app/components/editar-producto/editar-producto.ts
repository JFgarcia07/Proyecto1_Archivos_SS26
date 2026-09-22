import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Producto, ProductoInterface } from '../../services/producto';

@Component({
  selector: 'app-editar-producto',
  imports: [CommonModule, FormsModule, Navbar, RouterLink],
  templateUrl: './editar-producto.html',
  styleUrl: './editar-producto.css',
})
export class EditarProducto implements OnInit {

  idProducto : string = '';
  nombreProducto : string = '';
  categoria : string = '';
  precio: number | null = null;
  stock: number = 0

  cargando = signal<boolean>(true);
  guardando = signal<boolean>(false);
  mensajeError = signal<string>('');

  categorias : string[] = [
    'Escritorio',
    'Sillas',
    'Mesas',
    'Estanterías',
    'Sofás',
    'Accesorios'
  ]

  constructor(private productoService: Producto, private router: Router, private ruta: ActivatedRoute) {}

  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');

    if(id === null){
      this.mensajeError.set('No se indicó el producto a editar');
      this.cargando.set(false);
      return;
    }

    this.idProducto = id;
    this.cargarProducto(id);
  }

  cargarProducto(id:string): void{
    this.cargando.set(true);

    this.productoService.buscarPorId(id).subscribe({
      next: (datos) => {
        this.idProducto = datos.idProducto;
        this.nombreProducto = datos.nombreProducto;
        this.categoria = datos.categoria;
        this.precio = datos.precio;
        this.stock = datos.stock;
        this.cargando.set(false);
      }, 
      error: (err) =>  {
        this.mensajeError.set('No se pudo cargar el producto')
        this.cargando.set(false);
      }
    });
  }

  guardar(): void {
    this.mensajeError.set('');

    if(this.nombreProducto.trim() === ''){
      this.mensajeError.set('El nombre del producto no puede esta vacio');
      return;
    } else if (this.categoria.trim() === ''){
      this.mensajeError.set('Debe seleccionar una categoria');
      return;
    } else if (this.precio === null || this.precio < 0){
      this.mensajeError.set('El precio no puede ser vacio y ser mayor a 0');
      return;
    }

    const productoEditado : ProductoInterface = {
      idProducto: this.idProducto,
      nombreProducto: this.nombreProducto,
      categoria: this.categoria,
      precio: this.precio,
      stock: this.stock,
      activo: true
    };

    this.guardando.set(true);

    this.productoService.actualizarProducto(this.idProducto, productoEditado).subscribe({
      next: (respuesta) => {
        this.guardando.set(false);
        this.router.navigate(['/productos']);
      },
      error: (err) => {
        this.guardando.set(false);
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo actualizar el producto');
        }
      }
    });
  }

  cancelar() : void {
    this.router.navigate(['/productos']);
  }
}
