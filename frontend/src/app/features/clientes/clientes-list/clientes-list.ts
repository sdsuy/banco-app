import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClienteService } from '../../../services/cliente.service';
import { Cliente } from '../../../models/cliente.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-clientes-list',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './clientes-list.html',
  styleUrl: './clientes-list.css',
})
export class ClientesList implements OnInit {
  private fb = inject(FormBuilder);
  private clienteService = inject(ClienteService);

  clientes = signal<Cliente[]>([]);
  busqueda = signal('');
  error = signal('');
  mensaje = signal('');
  editandoId = signal<number | undefined>(undefined);

  clientesFiltrados = computed(() => {
    const texto = this.busqueda().toLowerCase().trim();
    const lista = this.clientes();

    if (!texto) {
      return lista;
    }

    return lista.filter((cliente) =>
      cliente.nombre.toLowerCase().includes(texto) ||
      cliente.identificacion.toLowerCase().includes(texto) ||
      cliente.clienteId.toLowerCase().includes(texto) ||
      cliente.telefono.toLowerCase().includes(texto)
    );
  });

  form = this.fb.group({
    nombre: ['', [Validators.required]],
    genero: ['MASCULINO' as 'MASCULINO' | 'FEMENINO' | 'OTRO', [Validators.required]],
    edad: [0, [Validators.required, Validators.min(0)]],
    identificacion: ['', [Validators.required]],
    direccion: ['', [Validators.required]],
    telefono: ['', [Validators.required]],
    clienteId: ['', [Validators.required]],
    contrasena: ['', [Validators.required]],
    estado: [true, [Validators.required]]
  });

  ngOnInit(): void {
    this.cargarClientes();
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

  guardar(): void {
    this.error.set('');
    this.mensaje.set('');

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: Cliente = {
      nombre: this.form.value.nombre ?? '',
      genero: (this.form.value.genero ?? 'MASCULINO') as 'MASCULINO' | 'FEMENINO' | 'OTRO',
      edad: Number(this.form.value.edad ?? 0),
      identificacion: this.form.value.identificacion ?? '',
      direccion: this.form.value.direccion ?? '',
      telefono: this.form.value.telefono ?? '',
      clienteId: this.form.value.clienteId ?? '',
      contrasena: this.form.value.contrasena ?? '',
      estado: Boolean(this.form.value.estado)
    };

    const id = this.editandoId();
    const request = id
      ? this.clienteService.actualizar(id, payload)
      : this.clienteService.crear(payload);

    request.subscribe({
      next: () => {
        this.mensaje.set(id ? 'Cliente actualizado correctamente.' : 'Cliente creado correctamente.');
        this.cancelar();
        this.cargarClientes();
      },
      error: (err) => {
        this.error.set(this.extraerError(err));
      }
    });
  }

  editar(cliente: Cliente): void {
    this.error.set('');
    this.mensaje.set('');
    this.editandoId.set(cliente.id);

    this.form.patchValue({
      nombre: cliente.nombre,
      genero: cliente.genero,
      edad: cliente.edad,
      identificacion: cliente.identificacion,
      direccion: cliente.direccion,
      telefono: cliente.telefono,
      clienteId: cliente.clienteId,
      contrasena: '',
      estado: cliente.estado
    });
  }

  eliminar(id?: number): void {
    if (!id) return;

    this.error.set('');
    this.mensaje.set('');

    this.clienteService.eliminar(id).subscribe({
      next: () => {
        this.mensaje.set('Cliente eliminado correctamente.');
        this.cargarClientes();
      },
      error: (err) => {
        this.error.set(this.extraerError(err));
      }
    });
  }

  cancelar(): void {
    this.editandoId.set(undefined);
    this.form.reset({
      nombre: '',
      genero: 'MASCULINO',
      edad: 0,
      identificacion: '',
      direccion: '',
      telefono: '',
      clienteId: '',
      contrasena: '',
      estado: true
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
