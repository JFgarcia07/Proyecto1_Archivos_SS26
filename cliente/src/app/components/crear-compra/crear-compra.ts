import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Compra, CompraInterface, DetalleCompraInterface } from '../../services/compra';
import { Proveedor, ProveedorInterface } from '../../services/proveedor';
import { Producto, ProductoInterface } from '../../services/producto';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-crear-compra',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './crear-compra.html',
  styleUrl: './crear-compra.css',
})
export class CrearCompra implements OnInit{
  idProveedor: string = '';

  idProductoSeleccionado: string = '';
  cantidad: number | null = null;
  precioUnitario: number | null = null;

  proveedores = signal<ProveedorInterface[]>([]);
  productos = signal<ProductoInterface[]>([]);
  detalles = signal<DetalleCompraInterface[]>([]);

  mensajeError = signal<string>('');
  guardando = signal<boolean>(false);

  constructor(
    private compraService: Compra,
    private proveedorService: Proveedor,
    private productoService: Producto,
    private authService: Auth,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarProveedores();
    this.cargarProductos();
  }

  cargarProveedores(): void {
    this.proveedorService.listarProveedores().subscribe({
      next: (datos) => {
        this.proveedores.set(datos);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar los proveedores');
      }
    });
  }

  cargarProductos(): void {
    this.productoService.listarProductos().subscribe({
      next: (datos) => {
        this.productos.set(datos);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar los productos');
      }
    });
  }

  /**
   * Agrega una linea de producto al detalle de la compra,
   * validando que no este repetido y que los datos sean correctos.
   */
  agregarProducto(): void {
    this.mensajeError.set('');

    if (this.idProductoSeleccionado === '') {
      this.mensajeError.set('Debe seleccionar un producto');
      return;
    }
    if (this.cantidad === null || this.cantidad <= 0) {
      this.mensajeError.set('La cantidad debe ser mayor a cero');
      return;
    }
    if (this.precioUnitario === null || this.precioUnitario < 0) {
      this.mensajeError.set('El precio unitario no puede ser negativo');
      return;
    }

    const listaActual = this.detalles();

    for (let i = 0; i < listaActual.length; i++) {
      if (listaActual[i].idProducto === this.idProductoSeleccionado) {
        this.mensajeError.set('Ese producto ya fue agregado a la compra');
        return;
      }
    }

    let nombre = '';
    const listaProductos = this.productos();
    for (let i = 0; i < listaProductos.length; i++) {
      if (listaProductos[i].idProducto === this.idProductoSeleccionado) {
        nombre = listaProductos[i].nombreProducto;
      }
    }

    const nuevaLinea: DetalleCompraInterface = {
      idCompra: 0,
      idProducto: this.idProductoSeleccionado,
      cantidad: this.cantidad,
      precioUnitario: this.precioUnitario,
      nombreProducto: nombre
    };

    this.detalles.set([...listaActual, nuevaLinea]);

    this.idProductoSeleccionado = '';
    this.cantidad = null;
    this.precioUnitario = null;
  }

  quitarProducto(idProducto: string): void {
    const listaActual = this.detalles();
    const nuevaLista: DetalleCompraInterface[] = [];

    for (let i = 0; i < listaActual.length; i++) {
      if (listaActual[i].idProducto !== idProducto) {
        nuevaLista.push(listaActual[i]);
      }
    }

    this.detalles.set(nuevaLista);
  }

  calcularSubtotal(detalle: DetalleCompraInterface): number {
    return detalle.cantidad * detalle.precioUnitario;
  }

  calcularTotal(): number {
    let total = 0;
    const lista = this.detalles();

    for (let i = 0; i < lista.length; i++) {
      total = total + (lista[i].cantidad * lista[i].precioUnitario);
    }

    return total;
  }

  guardar(): void {
    this.mensajeError.set('');

    if (this.idProveedor === '') {
      this.mensajeError.set('Debe seleccionar un proveedor');
      return;
    }
    if (this.detalles().length === 0) {
      this.mensajeError.set('Debe agregar al menos un producto');
      return;
    }

    const idUsuario = this.authService.obtenerIdUsuario();

    if (idUsuario === null) {
      this.mensajeError.set('No se pudo identificar al usuario');
      return;
    }

    const nuevaCompra: CompraInterface = {
      idCompra: 0,
      idProveedor: this.idProveedor,
      idUsuario: idUsuario,
      fecha: '',
      total: this.calcularTotal(),
      nombreProveedor: '',
      detalles: this.detalles()
    };

    this.guardando.set(true);

    this.compraService.registrarCompra(nuevaCompra).subscribe({
      next: (respuesta) => {
        this.guardando.set(false);
        this.router.navigate(['/compras']);
      },
      error: (err) => {
        this.guardando.set(false);
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo registrar la compra');
        }
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/compras']);
  }
}
