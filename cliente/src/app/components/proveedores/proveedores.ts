import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Proveedor, ProveedorInterface } from '../../services/proveedor';
@Component({
  selector: 'app-proveedores',
  imports: [CommonModule, Navbar, RouterLink],
  templateUrl: './proveedores.html',
  styleUrl: './proveedores.css',
})
export class Proveedores implements OnInit{
  proveedores = signal<ProveedorInterface[]>([]);
  proveedoresInactivos = signal<ProveedorInterface[]>([]);
  cargando = signal<boolean>(true);
  cargandoInactivos = signal<boolean>(true);
  mensajeError = signal<string>('');

  constructor(private proveedorService: Proveedor) {}

  ngOnInit(): void {
    this.cargarProveedores();
    this.cargarProveedoresInactivos();
  }

  cargarProveedores(): void {
    this.cargando.set(true);
    this.mensajeError.set('');

    this.proveedorService.listarProveedores().subscribe({
      next: (datos) => {
        this.proveedores.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.mensajeError.set('No se pudieron cargar los proveedores');
        this.cargando.set(false);
      }
    });
  }

  cargarProveedoresInactivos(): void {
    this.cargandoInactivos.set(true);

    this.proveedorService.listarProveedoresInactivos().subscribe({
      next: (datos) => {
        this.proveedoresInactivos.set(datos);
        this.cargandoInactivos.set(false);
      },
      error: (err) => {
        this.cargandoInactivos.set(false);
      }
    });
  }

  desactivar(idProveedor: string, nombreProveedor: string): void {
    const confirmado = confirm(
      `¿Desactivar el proveedor "${nombreProveedor}"?\n\n` +
      `El proveedor dejará de mostrarse, pero su historial de compras se conservará.`
    );

    if (!confirmado) {
      return;
    }

    this.proveedorService.desactivarProveedor(idProveedor).subscribe({
      next: (respuesta) => {
        this.cargarProveedores();
        this.cargarProveedoresInactivos();
      },
      error: (err) => {
        if (err.error) {
          this.mensajeError.set(err.error);
        } else {
          this.mensajeError.set('No se pudo desactivar el proveedor');
        }
      }
    });
  }
}
