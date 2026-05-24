import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FullCalendarModule } from '@fullcalendar/angular';
import { RouterModule, Routes } from '@angular/router';
import { MyAppointmentsCalendar } from './my-appointments-calendar/my-appointments-calendar.component';
import { MyAppointmentModal } from './my-appointment-modal/my-appointment-modal.component';

const routes: Routes = [
  { path: '', component: MyAppointmentsCalendar }
];

@NgModule({
  declarations: [MyAppointmentsCalendar, MyAppointmentModal],
  imports: [
    CommonModule,
    FormsModule,
    FullCalendarModule,
    RouterModule.forChild(routes)
  ]
})
export class MyAppointmentsModule {}
