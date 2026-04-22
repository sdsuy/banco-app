import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MovimientoService } from '../../../services/movimiento.service';
import { CuentaService } from '../../../services/cuenta.service';
import { Movimiento } from '../../../models/movimiento.model';
import { Cuenta } from '../../../models/cuenta.model';

@Component({
  selector: 'app-movimientos-list',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './movimientos-list.html',
  styleUrl: './movimientos-list.css',
})
export class MovimientosList implements OnInit {
  private fb = inject(FormBuilder);
  private movimientoService = inject(MovimientoService);
  private cuentaService = inject(CuentaService);

movimientos = signal<Movimiento[]>([]);
cuentas = signal<Cuenta[]>([]);

busqueda = signal('');
error = signal('');
mensaje = signal('');

movimientosFiltrados = computed(() => {
  const texto = this.busqueda().toLowerCase().trim();
  const lista = this.movimientos();

  if (!texto) {
    return lista;
  }

  return lista.filter((movimiento) =>
    (movimiento.numeroCuenta ?? '').toLowerCase().includes(texto) ||
    movimiento.tipoMovimiento.toLowerCase().includes(texto) ||
    String(movimiento.valor).includes(texto) ||
    String(movimiento.saldo ?? '').includes(texto)
  );
});

  form = this.fb.group({
    tipoMovimiento: ['CREDITO' as 'CREDITO' | 'DEBITO', [Validators.required]],
    valor: [0, [Validators.required, Validators.min(0.01)]],
    cuentaId: [null as number | null, [Validators.required]]
  });

  ngOnInit(): void {
    this.cargarCuentas();
    this.cargarMovimientos();
  }

  cargarCuentas(): void {
    this.cuentaService.listar().subscribe({
      next: (data) => {
        this.cuentas.set(data);
      },
      error: () => {
        this.error.set('No se pudieron cargar las cuentas.');
      }
    });
  }

  cargarMovimientos(): void {
    this.movimientoService.listar().subscribe({
      next: (data) => {
        this.movimientos.set([...data].reverse());
      },
      error: () => {
        this.error.set('No se pudieron cargar los movimientos.');
      }
    });
  }

  guardar(): void {
    this.error.set('');
    this.mensaje.set('');

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: Movimiento = {
      tipoMovimiento: (this.form.value.tipoMovimiento ?? 'CREDITO') as 'CREDITO' | 'DEBITO',
      valor: Number(this.form.value.valor ?? 0),
      cuentaId: Number(this.form.value.cuentaId)
    };

    this.movimientoService.crear(payload).subscribe({
      next: () => {
        this.mensaje.set('Movimiento registrado correctamente.');
        this.cancelar();
        this.cargarMovimientos();
      },
      error: (err) => {
        this.error.set(this.extraerError(err));
      }
    });
  }

  cancelar(): void {
    this.form.reset({
      tipoMovimiento: 'CREDITO',
      valor: 0,
      cuentaId: null
    });
  }

  campoInvalido(nombreCampo: string): boolean {
    const control = this.form.get(nombreCampo);
    return !!control && control.invalid && (control.dirty || control.touched);
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

    return 'Ocurrió un error al procesar la solicitud.';
  }
}
