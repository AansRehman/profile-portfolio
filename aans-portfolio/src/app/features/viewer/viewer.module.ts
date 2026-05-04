import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ViewerRoutingModule } from './viewer-routing.module';
import { ViewerHubComponent } from './viewer-hub.component';

@NgModule({
  declarations: [ViewerHubComponent],
  imports: [SharedModule, ViewerRoutingModule],
})
export class ViewerModule {}
