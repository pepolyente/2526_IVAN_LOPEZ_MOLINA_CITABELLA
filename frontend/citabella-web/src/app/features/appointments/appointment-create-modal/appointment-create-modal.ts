import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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

  clients:    ClientResponse[]    = [];
  employees:  EmployeeResponse[]  = [];
  treatments: TreatmentResponse[] = [];

  form: CreateAppointmentRequest = {
    treatmentsIds: [],
    startAt: '',
    endAt:   '',
  };

  loading = false;
  error   = '';

  constructor(
    private appointmentSvc: AppointmentService,
    private clientSvc:      ClientService,
    private employeeSvc:    EmployeeService,
    private treatmentSvc:   TreatmentService,
  ) {}

  ngOnInit(): void {
    this.form.startAt = this.toLocalDatetime(this.initialStart);
    this.form.endAt   = this.toLocalDatetime(this.initialEnd);

    this.clientSvc.getAll({ page: 0, size: 200 }).subscribe(d => this.clients = d.content);
    this.employeeSvc.getAll({ page: 0, size: 200 }).subscribe(d => this.employees = d.content);
    this.treatmentSvc.getAll({ page: 0, size: 200 }).subscribe(d => this.treatments = d.content);
  }

  toggleTreatment(id: number): void {
    const idx = this.form.treatmentsIds.indexOf(id);
    if (idx === -1) this.form.treatmentsIds.push(id);
    else this.form.treatmentsIds.splice(idx, 1);
  }

  isSelected(id: number): boolean {
    return this.form.treatmentsIds.includes(id);
  }

  nextStep(): void { this.step = Math.min(this.step + 1, 3); }
  prevStep(): void { this.step = Math.max(this.step - 1, 1); }

  canGoNext(): boolean {
    if (this.step === 1) return true;
    if (this.step === 2) return this.form.treatmentsIds.length > 0;
    return true;
  }

  submit(): void {
    const validationError = this.validateForm();
    if (validationError) {
      this.error = validationError;
      return;
    }

    this.loading = true;
    this.error = '';

    this.appointmentSvc.create(this.form as CreateAppointmentRequest).subscribe({
      next: () => {
        this.loading = false;
        this.created.emit();
      },
      error: err => {
        this.error = err.error?.message ?? 'Error al crear la cita';
        this.loading = false;
      },
    });
  }

  /** Formatea un Date al string que espera datetime-local input */
  private toLocalDatetime(date: Date): string {
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
  }

  /** Extrae HH:mm de un string datetime-local para mostrar en el resumen */
  formatTime(dt: string): string {
    return dt ? dt.slice(11, 16) : '';
  }

  private validateForm(): string | null {
    if (!this.form.clientId) return 'Debes seleccionar un cliente';
    if (!this.form.employeeId) return 'Debes seleccionar un empleado';
    if (!this.form.treatmentsIds || this.form.treatmentsIds.length === 0)
      return 'Debes seleccionar al menos un servicio';
    if (!this.form.startAt || !this.form.endAt)
      return 'Debes seleccionar fecha y hora';

    return null;
  }

  isFormValid(): boolean {
    return this.validateForm() === null;
  }
}
