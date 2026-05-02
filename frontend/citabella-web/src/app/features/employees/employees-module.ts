import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EmployeesRoutingModule } from './employees-routing-module';
import { EmployeeList } from './employee-list/employee-list';
import { EmployeeForm } from './employee-form/employee-form';

@NgModule({
  declarations: [EmployeeList, EmployeeForm],
  imports: [CommonModule, FormsModule, EmployeesRoutingModule],
})
export class EmployeesModule {}
