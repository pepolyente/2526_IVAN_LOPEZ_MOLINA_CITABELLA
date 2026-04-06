import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentForm } from './appointment-form';

describe('AppointmentForm', () => {
  let component: AppointmentForm;
  let fixture: ComponentFixture<AppointmentForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AppointmentForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
