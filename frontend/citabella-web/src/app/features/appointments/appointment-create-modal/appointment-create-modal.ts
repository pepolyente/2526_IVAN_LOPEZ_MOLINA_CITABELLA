import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { AppointmentService } from '../../../core/services/appointment.service';
import { ClientService } from '../../../core/services/client.service';
import { EmployeeService } from '../../../core/services/employee.service';
import { TreatmentService } from '../../../core/services/treatment.service';
import { CreateAppointmentRequest } from '../../../shared/models/appointment.model';
import { ClientResponse } from '../../../shared/models/client.model';
import { EmployeeResponse } from '../../../shared/models/employee.model';
import { TreatmentResponse } from '../../../shared/models/treatment.model';

@Component({
  selector: 'app-appointment-create-modal',
  standalone: false,
  templateUrl: './appointment-create-modal.html',
  styleUrl: './appointment-create-modal.css',
})
export class AppointmentCreateModal implements OnInit {
  @Input() initialStart!: Date;
  @Input() initialEnd!: Date;
  @Output() close   = new EventEmitter<void>();
  @Output() created = new EventEmitter<void>();

  step = 1;

  clientResults: ClientResponse[] = [];
  employeeResults: EmployeeResponse[] = [];
  treatmentResults: TreatmentResponse[] = [];

  clientSearchTerm = '';
  employeeSearchTerm = '';
  treatmentSearchTerm = '';

  selectedClient: ClientResponse | null = null;
  selectedEmployee: EmployeeResponse | null = null;

  selectedTreatments: TreatmentResponse[] = [];

  form: CreateAppointmentRequest = {
    clientId: undefined,
    employeeId: undefined,
    treatmentsIds: [],
    startAt: '',
    endAt: '',
  };

  loading = false;
  error = '';

  constructor(
    private appointmentSvc: AppointmentService,
    private clientSvc: ClientService,
    private employeeSvc: EmployeeService,
    private treatmentSvc: TreatmentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.form.startAt = this.toLocalDatetime(this.initialStart);
    this.form.endAt   = this.toLocalDatetime(this.initialEnd);
  }

  searchClients(): void {
    if (!this.clientSearchTerm.trim()) {
      this.clientResults = [];
      return;
    }
    this.clientSvc.getAll({ page: 0, size: 10, search: this.clientSearchTerm })
      .subscribe({
        next: page => {this.clientResults = page.content;this.cdr.detectChanges();},
        error: () => this.clientResults = []
      });
  }

  searchEmployees(): void {
    if (!this.employeeSearchTerm.trim()) {
      this.employeeResults = [];
      return;
    }
    this.employeeSvc.getAll({ page: 0, size: 10, search: this.employeeSearchTerm })
      .subscribe({
        next: page =>{ this.employeeResults = page.content;this.cdr.detectChanges();},
        error: () => this.employeeResults = []
      });
  }

  searchTreatments(): void {
    if (!this.treatmentSearchTerm.trim()) {
      this.treatmentResults = [];
      return;
    }
    this.treatmentSvc.getDetailed({ page: 0, size: 20, search: this.treatmentSearchTerm, active: true })
      .subscribe({
        next: page => {this.treatmentResults = page.content;this.cdr.detectChanges();},
        error: () => this.treatmentResults = []
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

  nextStep(): void {
    if (this.step === 1 && !this.form.clientId) {
      this.error = 'Debes seleccionar un cliente';
      return;
    }
    this.error = '';
    this.step = Math.min(this.step + 1, 3);
  }

  prevStep(): void { this.step = Math.max(this.step - 1, 1); }

  canGoNext(): boolean {
    if (this.step === 1) return !!this.form.clientId;
    if (this.step === 2) return this.form.treatmentsIds.length > 0 && !!this.form.employeeId;
    return true;
  }

  submit(): void {
    if (!this.form.clientId || !this.form.employeeId || this.form.treatmentsIds.length === 0) {
      this.error = 'Completa todos los datos';
      return;
    }
    this.loading = true;
    this.error = '';
    this.appointmentSvc.create(this.form).subscribe({
      next: () => {
        this.loading = false;
        this.created.emit();
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.error?.message ?? 'Error al crear la cita';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }
  private toLocalDatetime(date: Date): string {
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
  }

  formatTime(dt: string): string {
    return dt ? dt.slice(11, 16) : '';
  }
}
