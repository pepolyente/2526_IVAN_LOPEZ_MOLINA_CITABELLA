import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentsRoutingModule } from './appointments-routing-module';
import { AppointmentList } from './appointment-list/appointment-list';
import { AppointmentForm } from './appointment-form/appointment-form';

@NgModule({
  declarations: [AppointmentList, AppointmentForm],
  imports: [CommonModule, FormsModule, AppointmentsRoutingModule],
})
export class AppointmentsModule {}
