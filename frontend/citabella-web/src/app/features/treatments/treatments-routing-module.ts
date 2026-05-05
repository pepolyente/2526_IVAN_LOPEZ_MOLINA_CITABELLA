import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TreatmentList } from './treatment-list/treatment-list';
import { TreatmentForm } from './treatment-form/treatment-form';

const routes: Routes = [
  { path: '',    component: TreatmentList },
  { path: 'new', component: TreatmentForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TreatmentsRoutingModule {}
