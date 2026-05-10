import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppointmentForm } from './appointment-form/appointment-form';
import { AppointmentCalendar } from './appointment-calendar/appointment-calendar';

const routes: Routes = [
  { path: '',         component: AppointmentCalendar },
  { path: 'new',      component: AppointmentForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AppointmentsRoutingModule {}
