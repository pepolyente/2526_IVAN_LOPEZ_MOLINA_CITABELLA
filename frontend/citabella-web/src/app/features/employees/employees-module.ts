import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeesRoutingModule } from './employees-routing-module';
import { EmployeeList } from './employee-list/employee-list';

@NgModule({
  declarations: [EmployeeList],
  imports: [CommonModule, EmployeesRoutingModule],
})
export class EmployeesModule {}
