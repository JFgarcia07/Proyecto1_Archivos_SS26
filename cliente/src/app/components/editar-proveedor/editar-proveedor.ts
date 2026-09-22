import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Proveedor, ProveedorInterface } from '../../services/proveedor';


@Component({
  selector: 'app-editar-proveedor',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './editar-proveedor.html',
  styleUrl: './editar-proveedor.css',
})
export class EditarProveedor implements OnInit {

  idProveedor: string = '';
  nombreProveedor: string = '';
  ubicacion: string = '';

  cargando = signal<boolean>(true);
  guardando = signal<boolean>(false);
  mensajeError = signal<string>('');

  constructor(
    private proveedorService: Proveedor,
    private router: Router,
    private ruta: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');

    if (id === null) {
      this.mensajeError.set('No se indicó el proveedor a editar');
      this.cargando.set(false);
      return;
    }

    this.idProveedor = id;
    this.cargarProveedor(id);
  }

  cargarProveedor(id: string): void {
    this.cargando.set(true);

    this.proveedorService.buscarPorId(id).subscribe({
      next: (datos) => {
        this.idProveedor = datos.idProveedor;
        this.nombreProveedor = datos.nombreProveedor;
        this.ubicacion = datos.ubicacion;
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudo cargar el proveedor');
        this.cargando.set(false);
      }
    });
  }

  guardar(): void {
    this.mensajeError.set('');

    if (this.nombreProveedor.trim() === '') {
      this.mensajeError.set('El nombre del proveedor es obligatorio');
      return;
    }

    const proveedorEditado: ProveedorInterface = {
      idProveedor: this.idProveedor,
      nombreProveedor: this.nombreProveedor,
      ubicacion: this.ubicacion,
      activo: true
    };

    this.guardando.set(true);

    this.proveedorService.actualizarProveedor(this.idProveedor, proveedorEditado).subscribe({
      next: (respuesta) => {
        this.guardando.set(false);
        this.router.navigate(['/proveedores']);
      },
      error: (err) => {
        this.guardando.set(false);
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo actualizar el proveedor');
        }
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/proveedores']);
  }
}