import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Cliente, ClienteInterface } from '../../services/cliente';

@Component({
  selector: 'app-crear-cliente',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './crear-cliente.html',
  styleUrl: './crear-cliente.css',
})
export class CrearCliente {
   nombre: string = '';
  nit: string = '';

  mensajeError = signal<string>('');
  guardando = signal<boolean>(false);

  constructor(private clienteService: Cliente, private router: Router) {}

  guardar(): void {
    this.mensajeError.set('');

    if (this.nombre.trim() === '') {
      this.mensajeError.set('El nombre del cliente es obligatorio');
      return;
    }
    if (this.nit.trim() === '') {
      this.mensajeError.set('El NIT es obligatorio');
      return;
    }

    const nuevoCliente: ClienteInterface = {
      idCliente: 0,
      nombre: this.nombre,
      nit: this.nit,
      saldo: 0,
      activo: true
    };

    this.guardando.set(true);

    this.clienteService.crearCliente(nuevoCliente).subscribe({
      next: (respuesta) => {
        this.guardando.set(false);
        this.router.navigate(['/clientes']);
      },
      error: (err) => {
        this.guardando.set(false);
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo guardar el cliente');
        }
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/clientes']);
  }
}
