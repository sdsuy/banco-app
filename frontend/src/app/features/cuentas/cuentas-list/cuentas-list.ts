import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CuentaService } from '../../../services/cuenta.service';
import { Cuenta } from '../../../models/cuenta.model';
import { Cliente } from '../../../models/cliente.model';
import { ClienteService } from '../../../services/cliente.service';

@Component({
  selector: 'app-cuentas-list',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './cuentas-list.html',
  styleUrl: './cuentas-list.css',
})
export class CuentasList implements OnInit {
  private fb = inject(FormBuilder);
  private cuentaService = inject(CuentaService);
  private clienteService = inject(ClienteService);

  cuentas = signal<Cuenta[]>([]);
  clientes = signal<Cliente[]>([]);

  busqueda = signal('');
  error = signal('');
  mensaje = signal('');
  editandoId = signal<number | undefined>(undefined);

  cuentasFiltradas = computed(() => {
    const texto = this.busqueda().toLowerCase().trim();
    const lista = this.cuentas();

    if (!texto) {
      return lista;
    }

    return lista.filter((cuenta) =>
      cuenta.numeroCuenta.toLowerCase().includes(texto) ||
      cuenta.tipoCuenta.toLowerCase().includes(texto) ||
      (cuenta.nombreCliente ?? '').toLowerCase().includes(texto)
    );
  });

  form = this.fb.group({
    numeroCuenta: ['', [Validators.required]],
    tipoCuenta: ['AHORROS' as 'AHORROS' | 'CORRIENTE', [Validators.required]],
    saldoInicial: [0, [Validators.required, Validators.min(0)]],
    estado: [true, [Validators.required]],
    clienteId: [null as number | null, [Validators.required]]
  });

  ngOnInit(): void {
    this.cargarClientes();
    this.cargarCuentas();
  }

  cargarClientes(): void {
    this.clienteService.listar().subscribe({
      next: (data) => {
        this.clientes.set(data);
      },
      error: () => {
        this.error.set('No se pudieron cargar los clientes.');
      }
    });
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

  guardar(): void {
    this.error.set('');
    this.mensaje.set('');

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: Cuenta = {
      numeroCuenta: this.form.value.numeroCuenta ?? '',
      tipoCuenta: (this.form.value.tipoCuenta ?? 'AHORROS') as 'AHORROS' | 'CORRIENTE',
      saldoInicial: Number(this.form.value.saldoInicial ?? 0),
      estado: Boolean(this.form.value.estado),
      clienteId: Number(this.form.value.clienteId)
    };

    const id = this.editandoId();
    const request = id
      ? this.cuentaService.actualizar(id, payload)
      : this.cuentaService.crear(payload);

    request.subscribe({
      next: () => {
        this.mensaje.set(id ? 'Cuenta actualizada correctamente.' : 'Cuenta creada correctamente.');
        this.cancelar();
        this.cargarCuentas();
      },
      error: (err) => {
        this.error.set(this.extraerError(err));
      }
    });
  }

  editar(cuenta: Cuenta): void {
    this.error.set('');
    this.mensaje.set('');
    this.editandoId.set(cuenta.id);

    this.form.patchValue({
      numeroCuenta: cuenta.numeroCuenta,
      tipoCuenta: cuenta.tipoCuenta,
      saldoInicial: cuenta.saldoInicial,
      estado: cuenta.estado,
      clienteId: cuenta.clienteId
    });
  }

  eliminar(id?: number): void {
    if (!id) return;

    this.error.set('');
    this.mensaje.set('');

    this.cuentaService.eliminar(id).subscribe({
      next: () => {
        this.mensaje.set('Cuenta eliminada correctamente.');
        this.cargarCuentas();
      },
      error: (err) => {
        this.error.set(this.extraerError(err));
      }
    });
  }

  cancelar(): void {
    this.editandoId.set(undefined);
    this.form.reset({
      numeroCuenta: '',
      tipoCuenta: 'AHORROS',
      saldoInicial: 0,
      estado: true,
      clienteId: null
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
