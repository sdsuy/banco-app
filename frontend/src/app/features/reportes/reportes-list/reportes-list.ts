import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClienteService } from '../../../services/cliente.service';
import { ReporteService } from '../../../services/reporte.service';
import { Cliente } from '../../../models/cliente.model';
import { Reporte } from '../../../models/reporte.model';

@Component({
  selector: 'app-reportes-list',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, DatePipe],
  templateUrl: './reportes-list.html',
  styleUrl: './reportes-list.css',
})
export class ReportesList implements OnInit {
  private fb = inject(FormBuilder);
  private clienteService = inject(ClienteService);
  private reporteService = inject(ReporteService);

  clientes: Cliente[] = [];
  reportes: Reporte[] = [];

  error = '';
  mensaje = '';

  form = this.fb.group({
    clienteId: [null as number | null, [Validators.required]],
    inicio: ['', [Validators.required]],
    fin: ['', [Validators.required]]
  });

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clienteService.listar().subscribe({
      next: (data) => {
        this.clientes = data;
      },
      error: () => {
        this.error = 'No se pudieron cargar los clientes.';
      }
    });
  }

  consultar(): void {
    this.error = '';
    this.mensaje = '';
    this.reportes = [];

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const clienteId = Number(this.form.value.clienteId);
    const inicio = this.form.value.inicio ?? '';
    const fin = this.form.value.fin ?? '';

    this.reporteService.obtener(clienteId, inicio, fin).subscribe({
      next: (data) => {
        this.reportes = data;
        this.mensaje = data.length > 0
          ? 'Reporte generado correctamente.'
          : 'No se encontraron movimientos para el rango indicado.';
      },
      error: (err) => {
        this.error = this.extraerError(err);
      }
    });
  }

  descargarPdf(): void {
    this.error = '';
    this.mensaje = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const clienteId = Number(this.form.value.clienteId);
    const inicio = this.form.value.inicio ?? '';
    const fin = this.form.value.fin ?? '';

    this.reporteService.descargarPdf(clienteId, inicio, fin).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const anchor = document.createElement('a');
        anchor.href = url;
        anchor.download = `estado-cuenta-${clienteId}-${inicio}-${fin}.pdf`;
        anchor.click();
        window.URL.revokeObjectURL(url);
        this.mensaje = 'PDF descargado correctamente.';
      },
      error: (err) => {
        this.error = this.extraerError(err);
      }
    });
  }

  campoInvalido(nombreCampo: string): boolean {
    const control = this.form.get(nombreCampo);
    return !!control && control.invalid && (control.dirty || control.touched);
  }

  totalCreditos(): number {
    return this.reportes
      .filter((r) => r.movimiento > 0)
      .reduce((acc, r) => acc + r.movimiento, 0);
  }

  totalDebitos(): number {
    return this.reportes
      .filter((r) => r.movimiento < 0)
      .reduce((acc, r) => acc + r.movimiento, 0);
  }

  private extraerError(err: any): string {
    if (typeof err?.error === 'string') {
      return err.error;
    }

    if (err?.error?.message) {
      return err.error.message;
    }

    if (err?.error && typeof err.error === 'object') {
      const primerError = Object.values(err.error)[0];
      if (typeof primerError === 'string') {
        return primerError;
      }
    }

    return 'Ocurrió un error al generar el reporte.';
  }
}
