import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportesList } from './reportes-list';

describe('ReportesList', () => {
  let component: ReportesList;
  let fixture: ComponentFixture<ReportesList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportesList],
    }).compileComponents();

    fixture = TestBed.createComponent(ReportesList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
