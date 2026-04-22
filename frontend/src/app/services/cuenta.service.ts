import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroments/enviroment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cuenta } from '../models/cuenta.model';

@Injectable({
  providedIn: 'root',
})
export class CuentaService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/cuentas`;

  listar(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(this.apiUrl);
  }

  crear(cuenta: Cuenta): Observable<Cuenta> {
    return this.http.post<Cuenta>(this.apiUrl, cuenta);
  }

  actualizar(id: number, cuenta: Cuenta): Observable<Cuenta> {
    return this.http.put<Cuenta>(`${this.apiUrl}/${id}`, cuenta);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
