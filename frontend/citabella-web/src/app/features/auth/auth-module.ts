import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthRoutingModule } from './auth-routing-module';
import { Login } from './login/login';
import {SharedModule} from '../../shared/shared-module';

@NgModule({
  declarations: [Login],
  imports: [CommonModule, FormsModule, AuthRoutingModule,SharedModule,],
})
export class AuthModule {}
