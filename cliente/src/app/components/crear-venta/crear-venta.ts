import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Venta, VentaInterface, DetalleVentaInterface } from '../../services/venta';
import { Cliente, ClienteInterface } from '../../services/cliente';
import { Producto, ProductoInterface } from '../../services/producto';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-crear-venta',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './crear-venta.html',
  styleUrl: './crear-venta.css',
})
export class CrearVenta implements OnInit {

  private readonly PORCENTAJE_IVA = 0.12;

  idCliente: string = '';

  idProductoSeleccionado: string = '';
  cantidad: number | null = null;

  precioSeleccionado = signal<number>(0);
  stockSeleccionado = signal<number>(0);

  clientes = signal<ClienteInterface[]>([]);
  productos = signal<ProductoInterface[]>([]);
  detalles = signal<DetalleVentaInterface[]>([]);

  mensajeError = signal<string>('');
  guardando = signal<boolean>(false);

  constructor(
    private ventaService: Venta,
    private clienteService: Cliente,
    private productoService: Producto,
    private authService: Auth,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarClientes();
    this.cargarProductos();
  }

  cargarClientes(): void {
    this.clienteService.listarClientes().subscribe({
      next: (datos) => {
        this.clientes.set(datos);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar los clientes');
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

  buscarProducto(idProducto: string): ProductoInterface | null {
    const lista = this.productos();

    for (let i = 0; i < lista.length; i++) {
      if (lista[i].idProducto === idProducto) {
        return lista[i];
      }
    }
    return null;
  }

  alSeleccionarProducto(): void {
    const producto = this.buscarProducto(this.idProductoSeleccionado);

    if (producto === null) {
      this.precioSeleccionado.set(0);
      this.stockSeleccionado.set(0);
      return;
    }

    this.precioSeleccionado.set(producto.precio);
    this.stockSeleccionado.set(producto.stock);
  }

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

    const producto = this.buscarProducto(this.idProductoSeleccionado);
    if (producto === null) {
      this.mensajeError.set('El producto seleccionado no existe');
      return;
    }

    if (this.cantidad > producto.stock) {
      this.mensajeError.set(
        'Existencia insuficiente de ' + producto.nombreProducto +
        '. Disponible: ' + producto.stock
      );
      return;
    }

    const listaActual = this.detalles();

    for (let i = 0; i < listaActual.length; i++) {
      if (listaActual[i].idProducto === this.idProductoSeleccionado) {
        this.mensajeError.set('Ese producto ya fue agregado a la venta');
        return;
      }
    }

    const nuevaLinea: DetalleVentaInterface = {
      idVenta: 0,
      idProducto: producto.idProducto,
      cantidad: this.cantidad,
      precioUnitario: producto.precio,
      nombreProducto: producto.nombreProducto
    };

    this.detalles.set([...listaActual, nuevaLinea]);

    this.idProductoSeleccionado = '';
    this.cantidad = null;
    this.precioSeleccionado.set(0);
    this.stockSeleccionado.set(0);
  }

  quitarProducto(idProducto: string): void {
    const listaActual = this.detalles();
    const nuevaLista: DetalleVentaInterface[] = [];

    for (let i = 0; i < listaActual.length; i++) {
      if (listaActual[i].idProducto !== idProducto) {
        nuevaLista.push(listaActual[i]);
      }
    }

    this.detalles.set(nuevaLista);
  }

  calcularLinea(detalle: DetalleVentaInterface): number {
    return detalle.cantidad * detalle.precioUnitario;
  }

  calcularSubtotal(): number {
    let subtotal = 0;
    const lista = this.detalles();

    for (let i = 0; i < lista.length; i++) {
      subtotal = subtotal + (lista[i].cantidad * lista[i].precioUnitario);
    }
    return subtotal;
  }

  calcularIva(): number {
    return this.calcularSubtotal() * this.PORCENTAJE_IVA;
  }

  calcularTotal(): number {
    return this.calcularSubtotal() + this.calcularIva();
  }

  guardar(): void {
    this.mensajeError.set('');

    if (this.idCliente === '') {
      this.mensajeError.set('Debe seleccionar un cliente');
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

    const nuevaVenta: VentaInterface = {
      idVenta: 0,
      idCliente: parseInt(this.idCliente, 10),
      idUsuario: idUsuario,
      fecha: '',
      subtotal: 0,
      iva: 0,
      total: 0,
      nombreCliente: '',
      nitCliente: '',
      nombreUsuario: '',
      detalles: this.detalles()
    };

    this.guardando.set(true);

    this.ventaService.registrarVenta(nuevaVenta).subscribe({
      next: (respuesta) => {
        const idVenta = parseInt(respuesta, 10);
        this.descargarFacturaYSalir(idVenta);
      },
      error: (err) => {
        this.guardando.set(false);
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo registrar la venta');
        }
      }
    });
  }

  /**
   * Descarga la factura de la venta recien registrada y regresa
   * al listado. Si la descarga fallara, la venta ya quedo guardada
   * y la factura puede descargarse despues desde el listado.
   */
  descargarFacturaYSalir(idVenta: number): void {
    this.ventaService.descargarFactura(idVenta).subscribe({
      next: (archivo) => {
        this.ventaService.guardarPdf(archivo, 'factura_' + idVenta + '.pdf');
        this.guardando.set(false);
        this.router.navigate(['/ventas']);
      },
      error: (err) => {
        this.guardando.set(false);
        this.router.navigate(['/ventas']);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/ventas']);
  }
}