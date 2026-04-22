import { inject, Injectable } from '@angular/core';
import { environment } from '../../enviroments/enviroment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reporte } from '../models/reporte.model';

@Injectable({
  providedIn: 'root',
})
export class ReporteService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/reportes`;

  obtener(clienteId: number, inicio: string, fin: string): Observable<Reporte[]> {
    const params = new HttpParams()
      .set('clienteId', clienteId)
      .set('inicio', inicio)
      .set('fin', fin);

    return this.http.get<Reporte[]>(this.apiUrl, { params });
  }

  descargarPdf(clienteId: number, inicio: string, fin: string): Observable<Blob> {
    const params = new HttpParams()
      .set('clienteId', clienteId)
      .set('inicio', inicio)
      .set('fin', fin);

    return this.http.get(`${this.apiUrl}/pdf`, {
      params,
      responseType: 'blob'
    });
  }
}
