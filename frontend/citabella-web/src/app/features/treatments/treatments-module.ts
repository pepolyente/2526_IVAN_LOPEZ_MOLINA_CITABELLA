import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TreatmentsRoutingModule } from './treatments-routing-module';
import { TreatmentList } from './treatment-list/treatment-list';
import { TreatmentForm } from './treatment-form/treatment-form';

@NgModule({
  declarations: [TreatmentList, TreatmentForm],
  imports: [CommonModule, FormsModule, TreatmentsRoutingModule],
})
export class TreatmentsModule {}
