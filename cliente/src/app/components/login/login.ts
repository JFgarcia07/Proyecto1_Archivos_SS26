import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email: string = '';
  password: string = '';
  mensajeError: string = '';

  constructor(private authService: Auth, private router: Router) { }

  onSubmit(): void {
    this.mensajeError = '';

    this.authService.login(this.email, this.password).subscribe({
      next: (respuesta) => {
        if (respuesta.succes) {
          this.authService.guardarSesion(respuesta);
          this.router.navigate(['/inicio']);
        } else {
          this.mensajeError = 'Credenciales incorrectas';
        }
      },
      error: (err) => {
        this.mensajeError = 'Credenciales incorrectas';
      }
    });
  }
}
