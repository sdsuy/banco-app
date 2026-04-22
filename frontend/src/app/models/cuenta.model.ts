export interface Cuenta {
  id?: number;
  numeroCuenta: string;
  tipoCuenta: 'AHORROS' | 'CORRIENTE';
  saldoInicial: number;
  estado: boolean;
  clienteId: number;
  nombreCliente?: string;
}