import { Component, OnInit } from '@angular/core';
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
    private router: Router
  ) {}

  ngOnInit(): void {
    this.svc.getAll().subscribe({
      next: data => {
        this.appointments = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  newAppointment(): void {
    this.router.navigate(['/panel/appointments/new']);
  }
}
