import { Component, inject, OnInit } from '@angular/core';
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

  clientes: Cliente[] = [];
  clientesFiltrados: Cliente[] = [];
  editandoId?: number;
  error = '';
  mensaje = '';
  busqueda = '';

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
        this.clientes = data;
        this.aplicarFiltro();
      },
      error: () => {
        this.error = 'No se pudieron cargar los clientes.';
      }
    });
  }

  guardar(): void {
    this.error = '';
    this.mensaje = '';

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

    const request = this.editandoId
      ? this.clienteService.actualizar(this.editandoId, payload)
      : this.clienteService.crear(payload);

    request.subscribe({
      next: () => {
        this.mensaje = this.editandoId
          ? 'Cliente actualizado correctamente.'
          : 'Cliente creado correctamente.';
        this.cancelar();
        this.cargarClientes();
      },
      error: (err) => {
        this.error = this.extraerError(err);
      }
    });
  }

  editar(cliente: Cliente): void {
    this.error = '';
    this.mensaje = '';
    this.editandoId = cliente.id;

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

    this.error = '';
    this.mensaje = '';

    this.clienteService.eliminar(id).subscribe({
      next: () => {
        this.mensaje = 'Cliente eliminado correctamente.';
        this.cargarClientes();
      },
      error: (err) => {
        this.error = this.extraerError(err);
      }
    });
  }

  cancelar(): void {
    this.editandoId = undefined;
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

  aplicarFiltro(): void {
    const texto = this.busqueda.toLowerCase().trim();

    if (!texto) {
      this.clientesFiltrados = [...this.clientes];
      return;
    }

    this.clientesFiltrados = this.clientes.filter((cliente) =>
      cliente.nombre.toLowerCase().includes(texto) ||
      cliente.identificacion.toLowerCase().includes(texto) ||
      cliente.clienteId.toLowerCase().includes(texto) ||
      cliente.telefono.toLowerCase().includes(texto)
    );
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
