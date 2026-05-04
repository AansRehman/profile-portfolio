import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { PortfolioRoutingModule } from './portfolio-routing.module';
import { HomeShellComponent } from './home-shell/home-shell.component';
import { SiteHeaderComponent } from './site-header/site-header.component';
import { HomePageComponent } from './home-page/home-page.component';

@NgModule({
  declarations: [HomeShellComponent, SiteHeaderComponent, HomePageComponent],
  imports: [SharedModule, PortfolioRoutingModule],
})
export class PortfolioModule {}
