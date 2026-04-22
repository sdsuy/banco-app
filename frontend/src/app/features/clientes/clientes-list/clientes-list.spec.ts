import { ComponentFixture, TestBed } from "@angular/core/testing";
import { of } from "rxjs";
import { ClienteService } from "../../../services/cliente.service";
import { ClientesList } from './clientes-list';

describe('ClientesList', () => {
  let component: ClientesList;
  let fixture: ComponentFixture<ClientesList>;

  const clienteServiceMock = {
    listar: jest.fn(),
    crear: jest.fn(),
    actualizar: jest.fn(),
    eliminar: jest.fn()
  };

  beforeEach(async () => {
    clienteServiceMock.listar.mockReturnValue(of([
      {
        id: 1,
        nombre: 'Jose Lema',
        genero: 'MASCULINO',
        edad: 30,
        identificacion: '1234567890',
        direccion: 'Otavalo',
        telefono: '099111111',
        clienteId: 'jlema',
        estado: true
      },
      {
        id: 2,
        nombre: 'Marianela Montalvo',
        genero: 'FEMENINO',
        edad: 28,
        identificacion: '999999999',
        direccion: 'Amazonas',
        telefono: '099222222',
        clienteId: 'mmontalvo',
        estado: true
      }
    ]));

    await TestBed.configureTestingModule({
      imports: [ClientesList],
      providers: [
        { provide: ClienteService, useValue: clienteServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientesList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('debe cargar clientes al iniciar', () => {
    expect(clienteServiceMock.listar).toHaveBeenCalled();
    expect(component.clientes().length).toBe(2);
    expect(component.clientesFiltrados().length).toBe(2);
  });

  it('debe filtrar clientes por búsqueda', () => {
    component.busqueda.set('jose');
    fixture.detectChanges();

    expect(component.clientesFiltrados().length).toBe(1);
    expect(component.clientesFiltrados()[0].nombre).toBe('Jose Lema');
  });
});
