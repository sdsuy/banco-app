export interface Movimiento {
  id?: number;
  fecha?: string;
  tipoMovimiento: 'CREDITO' | 'DEBITO';
  valor: number;
  saldo?: number;
  cuentaId: number;
  numeroCuenta?: string;
}