import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { AppointmentService } from '../../../core/services/appointment.service';
import { AppointmentResponse } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-appointment-list',
  standalone: false,
  templateUrl: './appointment-list.html',
  styleUrl: './appointment-list.css',
})
export class AppointmentList implements OnInit {

  appointments: AppointmentResponse[] = [];
  loading = true;

  constructor(
    private svc: AppointmentService,
    private router: Router,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.svc.getAll().subscribe({
      next: data => {
        this.appointments = data;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  newAppointment(): void {
    this.router.navigate(['/panel/appointments/new']);
  }
}
