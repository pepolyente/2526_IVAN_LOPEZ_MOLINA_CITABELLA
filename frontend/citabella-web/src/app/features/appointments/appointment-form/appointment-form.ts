import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppointmentService } from '../../../core/services/appointment.service';
import { ClientService } from '../../../core/services/client.service';
import { EmployeeService } from '../../../core/services/employee.service';
import { TreatmentService } from '../../../core/services/treatment.service';
import { CreateAppointmentRequest } from '../../../shared/models/appointment.model';
import { ClientResponse } from '../../../shared/models/client.model';
import { EmployeeResponse } from '../../../shared/models/employee.model';
import { TreatmentResponse } from '../../../shared/models/treatment.model';

@Component({
  selector: 'app-appointment-form',
  standalone: false,
  templateUrl: './appointment-form.html',
  styleUrl: './appointment-form.css',
})
export class AppointmentForm implements OnInit {

  clients: ClientResponse[]       = [];
  employees: EmployeeResponse[]   = [];
  treatments: TreatmentResponse[] = [];

  form: CreateAppointmentRequest = {
    treatmentsIds: [],
  };

  loading = false;
  error   = '';

  constructor(
    private appointmentSvc: AppointmentService,
    private clientSvc:      ClientService,
    private employeeSvc:    EmployeeService,
    private treatmentSvc:   TreatmentService,
    private router:         Router,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.clientSvc.getAll({ page: 0, size: 200 }).subscribe(data => {
      this.clients = data.content;
      this.changeDetectorRef.detectChanges();
    });
    this.employeeSvc.getAll({ page: 0, size: 200 }).subscribe(data => {
      this.employees = data.content;
      this.changeDetectorRef.detectChanges();
    });
    this.treatmentSvc.getAll({ page: 0, size: 200 }).subscribe(data => {
      this.treatments = data.content;
      this.changeDetectorRef.detectChanges();
    });
  }

  toggleTreatment(id: number): void {
    const index = this.form.treatmentsIds.indexOf(id);
    if (index === -1) this.form.treatmentsIds.push(id);
    else this.form.treatmentsIds.splice(index, 1);
  }

  isSelected(id: number): boolean {
    return this.form.treatmentsIds.includes(id);
  }

  submit(): void {
    this.loading = true;
    this.error   = '';
    this.appointmentSvc.create(this.form).subscribe({
      next: () => this.router.navigate(['/panel/appointments']),
      error: err => {
        this.error = err.error?.message ?? 'Error al crear la cita';
        this.loading = false;
      },
    });
  }

  cancel(): void {
    this.router.navigate(['/panel/appointments']);
  }
}
