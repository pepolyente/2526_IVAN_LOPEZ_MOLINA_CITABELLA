import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppointmentList } from './appointment-list/appointment-list';
import { AppointmentForm } from './appointment-form/appointment-form';

const routes: Routes = [
  { path: '',    component: AppointmentList },
  { path: 'new', component: AppointmentForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AppointmentsRoutingModule {}
