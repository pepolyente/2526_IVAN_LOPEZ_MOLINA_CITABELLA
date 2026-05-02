import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppointmentService } from '../../../core/services/appointment.service';
import {
  AppointmentResponse,
  AppointmentStatus,
} from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-appointment-list',
  standalone: false,
  templateUrl: './appointment-list.html',
  styleUrl: './appointment-list.css',
})
export class AppointmentList implements OnInit {

  appointments: AppointmentResponse[] = [];
  loading = true;
  error   = '';
  page          = 0;
  size          = 20;
  totalPages    = 0;
  totalElements = 0;

  filterStatus: AppointmentStatus | '' = '';

  changingStatusId: number | null = null;
  newStatus: AppointmentStatus | '' = '';

  readonly statuses: AppointmentStatus[] = [
    'PENDING', 'CONFIRMED', 'IN_PROGRESS', 'CANCELLED', 'COMPLETED', 'NO_SHOW',
  ];

  readonly transitions: Record<AppointmentStatus, AppointmentStatus[]> = {
    PENDING:     ['CONFIRMED', 'CANCELLED'],
    CONFIRMED:   ['IN_PROGRESS', 'CANCELLED', 'NO_SHOW'],
    IN_PROGRESS: ['COMPLETED', 'CANCELLED'],
    CANCELLED:   [],
    COMPLETED:   [],
    NO_SHOW:     [],
  };

  constructor(
    private svc: AppointmentService,
    private router: Router,
    private changeDetectorRef: ChangeDetectorRef,
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.error   = '';
    const status = this.filterStatus || undefined;

    this.svc.getAll({ page: this.page, size: this.size, status }).subscribe({
      next: page => {
        this.appointments  = page.content;
        this.totalPages    = page.totalPages;
        this.totalElements = page.totalElements;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.error   = 'Error al cargar las citas';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  applyFilter(): void {
    this.page = 0;
    this.load();
  }

  prevPage(): void { if (this.page > 0) { this.page--; this.load(); } }
  nextPage(): void { if (this.page < this.totalPages - 1) { this.page++; this.load(); } }

  newAppointment(): void { this.router.navigate(['/panel/appointments/new']); }

  startChangeStatus(id: number, current: AppointmentStatus): void {
    this.changingStatusId = id;
    this.newStatus = '';
  }

  cancelChangeStatus(): void {
    this.changingStatusId = null;
    this.newStatus = '';
  }

  confirmChangeStatus(id: number): void {
    if (!this.newStatus) return;
    this.svc.changeStatus(id, this.newStatus as AppointmentStatus).subscribe({
      next: updated => {
        const idx = this.appointments.findIndex(a => a.id === id);
        if (idx !== -1) this.appointments[idx] = updated;
        this.cancelChangeStatus();
        this.changeDetectorRef.detectChanges();
      },
      error: () => { alert('Error al cambiar el estado'); },
    });
  }

  cancelAppointment(id: number): void {
    if (!confirm('¿Cancelar esta cita?')) return;
    this.svc.cancel(id).subscribe({
      next: updated => {
        const idx = this.appointments.findIndex(a => a.id === id);
        if (idx !== -1) this.appointments[idx] = updated;
        this.changeDetectorRef.detectChanges();
      },
      error: () => { alert('Error al cancelar la cita'); },
    });
  }

  allowedTransitions(status: AppointmentStatus): AppointmentStatus[] {
    return this.transitions[status] ?? [];
  }

  isFinal(status: AppointmentStatus): boolean {
    return ['CANCELLED', 'COMPLETED', 'NO_SHOW'].includes(status);
  }
}
