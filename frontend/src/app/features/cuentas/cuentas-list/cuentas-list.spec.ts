import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CuentasList } from './cuentas-list';

describe('CuentasList', () => {
  let component: CuentasList;
  let fixture: ComponentFixture<CuentasList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CuentasList],
    }).compileComponents();

    fixture = TestBed.createComponent(CuentasList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
