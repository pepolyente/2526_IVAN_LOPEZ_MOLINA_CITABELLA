import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppointmentsRoutingModule } from './appointments-routing-module';
import { AppointmentList } from './appointment-list/appointment-list';


@NgModule({
  declarations: [
    AppointmentList
  ],
  imports: [
    CommonModule,
    AppointmentsRoutingModule
  ]
})
export class AppointmentsModule { }
