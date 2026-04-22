import { Routes } from '@angular/router';
import { ClientesList } from './features/clientes/clientes-list/clientes-list';
import { CuentasList } from './features/cuentas/cuentas-list/cuentas-list';

export const routes: Routes = [
  { path: '', redirectTo: 'clientes', pathMatch: 'full' },
  { path: 'clientes', component: ClientesList },
  { path: 'cuentas', component: CuentasList }
];
