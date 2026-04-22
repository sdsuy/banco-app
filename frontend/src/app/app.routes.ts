import { Routes } from '@angular/router';
import { ClientesList } from './features/clientes/clientes-list/clientes-list';
import { CuentasList } from './features/cuentas/cuentas-list/cuentas-list';
import { MovimientosList } from './features/movimientos/movimientos-list/movimientos-list';
import { ReportesList } from './features/reportes/reportes-list/reportes-list';

export const routes: Routes = [
  { path: '', redirectTo: 'clientes', pathMatch: 'full' },
  { path: 'clientes', component: ClientesList },
  { path: 'cuentas', component: CuentasList },
  { path: 'movimientos', component: MovimientosList},
  { path: 'reportes', component: ReportesList}
];
