import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import {SharedModule} from './shared/shared-module';
import {LayoutModule} from './layout/layout-module';
import { CoreModule } from './core/core-module';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';


@NgModule({
  declarations: [
    App
  ],
  imports: [
    BrowserModule,
    CoreModule,
    SharedModule,
    LayoutModule,
    AppRoutingModule

  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(withInterceptorsFromDi())
  ],
  bootstrap: [App]
})
export class AppModule { }
