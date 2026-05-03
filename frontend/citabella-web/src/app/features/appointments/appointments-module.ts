import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FullCalendarModule } from '@fullcalendar/angular';
import { AppointmentsRoutingModule } from './appointments-routing-module';
import { AppointmentList } from './appointment-list/appointment-list';
import { AppointmentForm } from './appointment-form/appointment-form';
import { AppointmentCalendar } from './appointment-calendar/appointment-calendar';
import { AppointmentModal } from './appointment-modal/appointment-modal';
import { AppointmentCreateModal } from './appointment-create-modal/appointment-create-modal';

@NgModule({
  declarations: [
    AppointmentList,
    AppointmentForm,
    AppointmentCalendar,
    AppointmentModal,
    AppointmentCreateModal,
  ],
  imports: [
    CommonModule,
    FormsModule,
    FullCalendarModule,
    AppointmentsRoutingModule,
  ],
})
export class AppointmentsModule {}
