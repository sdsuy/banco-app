import { ComponentFixture, TestBed } from "@angular/core/testing";
import { of } from "rxjs";
import { ReportesList } from './reportes-list';
import { ClienteService } from "../../../services/cliente.service";
import { ReporteService } from "../../../services/reporte.service";

describe('ReportesList', () => {
  let component: ReportesList;
  let fixture: ComponentFixture<ReportesList>;

  const clienteServiceMock = {
    listar: jest.fn()
  };

  const reporteServiceMock = {
    obtener: jest.fn(),
    descargarPdf: jest.fn()
  };

  beforeEach(async () => {
    clienteServiceMock.listar.mockReturnValue(of([
      {
        id: 1,
        nombre: 'Jose Lema',
        genero: 'MASCULINO',
        edad: 30,
        identificacion: '123',
        direccion: 'Otavalo',
        telefono: '099111111',
        clienteId: 'jlema',
        estado: true
      }
    ]));

    reporteServiceMock.obtener.mockReturnValue(of([
      {
        fecha: '2022-02-10T10:00:00',
        cliente: 'Jose Lema',
        numeroCuenta: '478758',
        tipoCuenta: 'AHORROS',
        saldoInicial: 2000,
        estado: true,
        movimiento: 600,
        saldoDisponible: 2600
      }
    ]));

    await TestBed.configureTestingModule({
      imports: [ReportesList],
      providers: [
        { provide: ClienteService, useValue: clienteServiceMock },
        { provide: ReporteService, useValue: reporteServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReportesList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('debe consultar reporte y cargar resultados', () => {
    component.form.patchValue({
      clienteId: 1,
      inicio: '2022-02-01',
      fin: '2022-02-28'
    });

    component.consultar();

    expect(reporteServiceMock.obtener).toHaveBeenCalledWith(1, '2022-02-01', '2022-02-28');
    expect(component.reportes().length).toBe(1);
    expect(component.mensaje()).toBe('Reporte generado correctamente.');
  });
});
