import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const platformId = inject(PLATFORM_ID);

  // En el servidor (SSR) no hay localStorage: se deja pasar y
  // la validacion real ocurre cuando la pagina carga en el navegador.
  if (!isPlatformBrowser(platformId)) {
    return true;
  }

  const authService = inject(Auth);
  const router = inject(Router);

  if (authService.estadoLogin()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};