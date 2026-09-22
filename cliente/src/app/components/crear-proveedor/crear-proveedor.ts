import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Proveedor, ProveedorInterface } from '../../services/proveedor';

@Component({
  selector: 'app-crear-proveedor',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './crear-proveedor.html',
  styleUrl: './crear-proveedor.css',
})
export class CrearProveedor {
  nombreProveedor: string = '';
  ubicacion: string = '';

  mensajeError = signal<string>('');
  guardando = signal<boolean>(false);

  constructor(private proveedorService: Proveedor, private router: Router) {}

  guardar(): void {
    this.mensajeError.set('');

    if (this.nombreProveedor.trim() === '') {
      this.mensajeError.set('El nombre del proveedor es obligatorio');
      return;
    }

    const nuevoProveedor: ProveedorInterface = {
      idProveedor: '',
      nombreProveedor: this.nombreProveedor,
      ubicacion: this.ubicacion,
      activo: true
    };

    this.guardando.set(true);

    this.proveedorService.crearProveedor(nuevoProveedor).subscribe({
      next: (respuesta) => {
        this.guardando.set(false);
        this.router.navigate(['/proveedores']);
      },
      error: (err) => {
        this.guardando.set(false);
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo guardar el proveedor');
        }
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/proveedores']);
  }
}
