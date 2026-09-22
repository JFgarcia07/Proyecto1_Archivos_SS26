import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerVenta } from './ver-venta';

describe('VerVenta', () => {
  let component: VerVenta;
  let fixture: ComponentFixture<VerVenta>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerVenta],
    }).compileComponents();

    fixture = TestBed.createComponent(VerVenta);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
