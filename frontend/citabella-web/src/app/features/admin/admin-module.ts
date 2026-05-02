import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing-module';
import { UserList } from './user-list/user-list';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [UserList],
  imports: [CommonModule, AdminRoutingModule, FormsModule],
})
export class AdminModule {}
