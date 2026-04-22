import { ComponentFixture, TestBed } from "@angular/core/testing";
import { of, throwError } from "rxjs";
import { CuentaService } from "../../../services/cuenta.service";
import { ClienteService } from "../../../services/cliente.service";
import { CuentasList } from './cuentas-list';

describe('CuentasList', () => {
  let component: CuentasList;
  let fixture: ComponentFixture<CuentasList>;

  const cuentaServiceMock = {
    listar: jest.fn(),
    crear: jest.fn(),
    actualizar: jest.fn(),
    eliminar: jest.fn()
  };

  const clienteServiceMock = {
    listar: jest.fn()
  };

  beforeEach(async () => {
    clienteServiceMock.listar.mockReturnValue(of([]));
    cuentaServiceMock.listar.mockReturnValue(throwError(() => new Error('boom')));

    await TestBed.configureTestingModule({
      imports: [CuentasList],
      providers: [
        { provide: CuentaService, useValue: cuentaServiceMock },
        { provide: ClienteService, useValue: clienteServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CuentasList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('debe mostrar error si no puede cargar cuentas', () => {
    expect(component.error()).toBe('No se pudieron cargar las cuentas.');
  });
});
