import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing-module';
import { UserList } from './user-list/user-list';
import {FormsModule} from '@angular/forms';
import { UserForm } from './user-form/user-form';

@NgModule({
  declarations: [UserList, UserForm],
  imports: [CommonModule, AdminRoutingModule, FormsModule],
})
export class AdminModule {}
