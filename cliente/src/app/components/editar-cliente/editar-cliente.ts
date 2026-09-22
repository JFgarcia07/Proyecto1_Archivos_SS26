import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Cliente, ClienteInterface } from '../../services/cliente';

@Component({
  selector: 'app-editar-cliente',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './editar-cliente.html',
  styleUrl: './editar-cliente.css',
})
export class EditarCliente implements OnInit{
  idCliente: number = 0;
  nombre: string = '';
  nit: string = '';
  saldo: number = 0;

  cargando = signal<boolean>(true);
  guardando = signal<boolean>(false);
  mensajeError = signal<string>('');

  constructor(
    private clienteService: Cliente,
    private router: Router,
    private ruta: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.ruta.snapshot.paramMap.get('id');

    if (id === null) {
      this.mensajeError.set('No se indicó el cliente a editar');
      this.cargando.set(false);
      return;
    }

    this.idCliente = parseInt(id, 10);
    this.cargarCliente(this.idCliente);
  }

  cargarCliente(id: number): void {
    this.cargando.set(true);

    this.clienteService.buscarPorId(id).subscribe({
      next: (datos) => {
        this.idCliente = datos.idCliente;
        this.nombre = datos.nombre;
        this.nit = datos.nit;
        this.saldo = datos.saldo;
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudo cargar el cliente');
        this.cargando.set(false);
      }
    });
  }

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

    const clienteEditado: ClienteInterface = {
      idCliente: this.idCliente,
      nombre: this.nombre,
      nit: this.nit,
      saldo: this.saldo,
      activo: true
    };

    this.guardando.set(true);

    this.clienteService.actualizarCliente(this.idCliente, clienteEditado).subscribe({
      next: (respuesta) => {
        this.guardando.set(false);
        this.router.navigate(['/clientes']);
      },
      error: (err) => {
        this.guardando.set(false);
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo actualizar el cliente');
        }
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/clientes']);
  }
}
