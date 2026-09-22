import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerCompra } from './ver-compra';

describe('VerCompra', () => {
  let component: VerCompra;
  let fixture: ComponentFixture<VerCompra>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerCompra],
    }).compileComponents();

    fixture = TestBed.createComponent(VerCompra);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
