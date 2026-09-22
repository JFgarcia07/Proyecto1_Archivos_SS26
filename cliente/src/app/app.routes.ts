import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Inicio } from './components/inicio/inicio';
import { Productos } from './components/productos/productos';
import { authGuard } from './guards/auth-guard';
import { CrearProducto } from './components/crear-producto/crear-producto';
import { EditarProducto } from './components/editar-producto/editar-producto';
import { Proveedores } from './components/proveedores/proveedores';
import { CrearProveedor } from './components/crear-proveedor/crear-proveedor';
import { EditarProveedor } from './components/editar-proveedor/editar-proveedor';
import { Clientes } from './components/clientes/clientes';
import { CrearCliente } from './components/crear-cliente/crear-cliente';
import { EditarCliente } from './components/editar-cliente/editar-cliente';
import { Compras } from './components/compras/compras';
import { CrearCompra } from './components/crear-compra/crear-compra';
import { VerCompra } from './components/ver-compra/ver-compra';
import { Ventas } from './components/ventas/ventas';
import { CrearVenta } from './components/crear-venta/crear-venta';
import { VerVenta } from './components/ver-venta/ver-venta';
import { Bitacora } from './components/bitacora/bitacora';
import { Inventario } from './components/inventario/inventario';
import { Reportes } from './components/reportes/reportes';

export const routes: Routes = [
    {path: 'login', component: Login},
    {path: 'inicio', component: Inicio, canActivate: [authGuard] },
    { path: 'productos', component: Productos, canActivate: [authGuard] },
    { path: 'crear-producto', component: CrearProducto, canActivate: [authGuard] },
    { path: 'editar-producto/:id', component: EditarProducto, canActivate: [authGuard] },
    { path: 'proveedores', component: Proveedores, canActivate: [authGuard] },
    { path: 'crear-proveedor', component: CrearProveedor, canActivate: [authGuard] },
    { path: 'editar-proveedor/:id', component: EditarProveedor, canActivate: [authGuard] },
    { path: 'clientes', component: Clientes, canActivate: [authGuard] },
    { path: 'crear-cliente', component: CrearCliente, canActivate: [authGuard] },
    { path: 'editar-cliente/:id', component: EditarCliente, canActivate: [authGuard] },
    { path: 'compras', component: Compras, canActivate: [authGuard] },
    { path: 'crear-compra', component: CrearCompra, canActivate: [authGuard] },
    { path: 'ver-compra/:id', component: VerCompra, canActivate: [authGuard] },
    { path: 'ventas', component: Ventas, canActivate: [authGuard] },
    { path: 'crear-venta', component: CrearVenta, canActivate: [authGuard] },
    { path: 'ver-venta/:id', component: VerVenta, canActivate: [authGuard] },
    { path: 'bitacora', component: Bitacora, canActivate: [authGuard] },
    { path: 'inventario', component: Inventario, canActivate: [authGuard] },
    { path: 'reportes', component: Reportes, canActivate: [authGuard] },
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
