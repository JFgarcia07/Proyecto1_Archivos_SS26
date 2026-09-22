import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Cliente, ClienteInterface } from '../../services/cliente';

@Component({
  selector: 'app-clientes',
  imports: [CommonModule, Navbar, RouterLink],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css',
})
export class Clientes implements OnInit{
  clientes = signal<ClienteInterface[]>([]);
  clientesInactivos = signal<ClienteInterface[]>([]);
  cargando = signal<boolean>(true);
  cargandoInactivos = signal<boolean>(true);
  mensajeError = signal<string>('');

  constructor(private clienteService: Cliente) {}

  ngOnInit(): void {
    this.cargarClientes();
    this.cargarClientesInactivos();
  }

  cargarClientes(): void {
    this.cargando.set(true);
    this.mensajeError.set('');

    this.clienteService.listarClientes().subscribe({
      next: (datos) => {
        this.clientes.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar los clientes');
        this.cargando.set(false);
      }
    });
  }

  cargarClientesInactivos(): void {
    this.cargandoInactivos.set(true);

    this.clienteService.listarClientesInactivos().subscribe({
      next: (datos) => {
        this.clientesInactivos.set(datos);
        this.cargandoInactivos.set(false);
      },
      error: (err) => {
        this.cargandoInactivos.set(false);
      }
    });
  }

  desactivar(idCliente: number, nombre: string): void {
    const confirmado = confirm(
      `¿Desactivar al cliente "${nombre}"?\n\n` +
      `El cliente dejará de mostrarse, pero su historial de ventas se conservará.`
    );

    if (!confirmado) {
      return;
    }

    this.clienteService.desactivarCliente(idCliente).subscribe({
      next: (respuesta) => {
        this.cargarClientes();
        this.cargarClientesInactivos();
      },
      error: (err) => {
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo desactivar el cliente');
        }
      }
    });
  }
}
