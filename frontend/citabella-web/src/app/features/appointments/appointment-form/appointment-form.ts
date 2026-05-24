import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppointmentService } from '../../../core/services/appointment.service';
import { ClientService } from '../../../core/services/client.service';
import { EmployeeService } from '../../../core/services/employee.service';
import { TreatmentService } from '../../../core/services/treatment.service';
import { ToastService } from '../../../core/services/toast.service';
import { CreateAppointmentRequest } from '../../../shared/models/appointment.model';
import { ClientResponse } from '../../../shared/models/client.model';
import { EmployeeResponse } from '../../../shared/models/employee.model';
import { TreatmentResponse } from '../../../shared/models/treatment.model';

@Component({
  selector: 'app-appointment-form',
  standalone: false,
  templateUrl: './appointment-form.html',
  styleUrls: ['./appointment-form.css']
})
export class AppointmentForm implements OnInit {
  clientSearchTerm = '';
  clientResults: ClientResponse[] = [];
  selectedClient: ClientResponse | null = null;

  employeeSearchTerm = '';
  employeeResults: EmployeeResponse[] = [];
  selectedEmployee: EmployeeResponse | null = null;


  treatmentSearchTerm = '';
  treatmentResults: TreatmentResponse[] = [];
  selectedTreatments: TreatmentResponse[] = [];

  form: CreateAppointmentRequest = {
    clientId: undefined,
    employeeId: undefined,
    treatmentsIds: [],
    startAt: '',
    endAt: '',
    notes: ''
  };

  loading = false;

  constructor(
    private appointmentSvc: AppointmentService,
    private clientSvc: ClientService,
    private employeeSvc: EmployeeService,
    private treatmentSvc: TreatmentService,
    private toast: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  // ─── Clientes ──────────────────────────────────────────────
  searchClients(): void {
    if (!this.clientSearchTerm.trim()) {
      this.clientResults = [];
      return;
    }
    this.clientSvc.getAll({ page: 0, size: 10, search: this.clientSearchTerm })
      .subscribe({
        next: page => this.clientResults = page.content,
        error: () => this.clientResults = []
      });
  }

  selectClient(client: ClientResponse): void {
    this.selectedClient = client;
    this.form.clientId = client.id;
    this.clientResults = [];
    this.clientSearchTerm = '';
  }

  clearClient(): void {
    this.selectedClient = null;
    this.form.clientId = undefined;
  }

  // ─── Empleados ─────────────────────────────────────────────
  searchEmployees(): void {
    if (!this.employeeSearchTerm.trim()) {
      this.employeeResults = [];
      return;
    }
    this.employeeSvc.getAll({ page: 0, size: 10, search: this.employeeSearchTerm })
      .subscribe({
        next: page => this.employeeResults = page.content,
        error: () => this.employeeResults = []
      });
  }

  selectEmployee(employee: EmployeeResponse): void {
    this.selectedEmployee = employee;
    this.form.employeeId = employee.id;
    this.employeeResults = [];
    this.employeeSearchTerm = '';
  }

  clearEmployee(): void {
    this.selectedEmployee = null;
    this.form.employeeId = undefined;
  }

  // ─── Tratamientos ──────────────────────────────────────────
  searchTreatments(): void {
    if (!this.treatmentSearchTerm.trim()) {
      this.treatmentResults = [];
      return;
    }
    this.treatmentSvc.getDetailed({ page: 0, size: 20, search: this.treatmentSearchTerm, active: true })
      .subscribe({
        next: page => this.treatmentResults = page.content,
        error: () => this.treatmentResults = []
      });
  }

  toggleTreatment(treatment: TreatmentResponse): void {
    const index = this.form.treatmentsIds.indexOf(treatment.id);
    if (index === -1) {
      this.form.treatmentsIds.push(treatment.id);
      this.selectedTreatments.push(treatment);
    } else {
      this.form.treatmentsIds.splice(index, 1);
      this.selectedTreatments = this.selectedTreatments.filter(t => t.id !== treatment.id);
    }
  }

  isTreatmentSelected(id: number): boolean {
    return this.form.treatmentsIds.includes(id);
  }

  // ─── Envío ─────────────────────────────────────────────────
  submit(): void {
    if (!this.form.clientId || !this.form.employeeId || this.form.treatmentsIds.length === 0) {
      this.toast.show('Debes seleccionar cliente, empleado y al menos un tratamiento', 'warning');
      return;
    }
    if (!this.form.startAt || !this.form.endAt) {
      this.toast.show('Selecciona fecha y hora de inicio y fin', 'warning');
      return;
    }
    this.loading = true;
    this.appointmentSvc.create(this.form).subscribe({
      next: () => {
        this.toast.show('Cita creada correctamente', 'success');
        this.router.navigate(['/panel/appointments']);
      },
      error: err => {
        const msg = err.error?.message ?? 'Error al crear la cita';
        this.toast.show(msg, 'error');
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/panel/appointments']);
  }
}
