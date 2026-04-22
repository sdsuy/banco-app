import { Routes } from '@angular/router';
import { ClientesList } from './features/clientes/clientes-list/clientes-list';

export const routes: Routes = [
  { path: '', redirectTo: 'clientes', pathMatch: 'full' },
  { path: 'clientes', component: ClientesList }
];
