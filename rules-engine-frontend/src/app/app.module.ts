import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './components/app/app.component';
import { ApiService } from './services/api/api.service';
import { HttpClientModule } from '@angular/common/http';
import { RulesComponent } from './components/rules/rules.component';
import {BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {DragDropModule } from '@angular/cdk/drag-drop';
import {
  MatButtonModule,
  MatListModule,
  MatToolbarModule,
  MatSlideToggleModule,
  MatSidenavModule,
  MatExpansionModule,
  MatIconModule,
  MatStepperModule,
  MatCheckboxModule,
  MatFormFieldModule, 
  MatInputModule} from '@angular/material';

import { AdvancedComponent } from './components/advanced/advanced.component';
import { SimpleComponent } from './components/simple/simple.component';
import { EventsService } from './services/events/events.service';
import { RulesService } from './services/rule/rules.service';


const appRoutes: Routes = [
  { path: 'rules', component: RulesComponent },
  { path: '', redirectTo: '/rules', pathMatch: 'full' },
];

@NgModule({
  declarations: [
    AppComponent,
    RulesComponent,
    AdvancedComponent,
    SimpleComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true }
    ),
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSlideToggleModule,
    MatCheckboxModule,
    MatSidenavModule,
    DragDropModule,
    MatButtonModule,
    MatListModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    FormsModule,
    MatInputModule
  ],
  providers: [
    ApiService,
    EventsService,
    RulesService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
