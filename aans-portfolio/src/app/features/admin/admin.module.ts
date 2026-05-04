import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';

@NgModule({
  declarations: [AdminLoginComponent, AdminDashboardComponent],
  imports: [SharedModule, ReactiveFormsModule, AdminRoutingModule],
})
export class AdminModule {}
