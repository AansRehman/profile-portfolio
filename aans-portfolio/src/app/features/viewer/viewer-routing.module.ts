import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewerAuthGuard } from '../../core/guards/viewer-auth.guard';
import { ViewerHubComponent } from './viewer-hub.component';

const routes: Routes = [
  { path: '', component: ViewerHubComponent, canActivate: [ViewerAuthGuard] },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ViewerRoutingModule {}
