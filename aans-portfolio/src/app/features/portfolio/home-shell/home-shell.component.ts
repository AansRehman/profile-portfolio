import { Component } from '@angular/core';
import { ProfileService } from '../../../core/services/profile.service';

@Component({
  selector: 'app-home-shell',
  standalone: false,
  templateUrl: './home-shell.component.html',
  styleUrl: './home-shell.component.scss',
})
export class HomeShellComponent {
  readonly currentYear = new Date().getFullYear();

  constructor(readonly ps: ProfileService) {}

  get footerName(): string {
    return this.ps.profile()?.name ?? 'Portfolio';
  }
}
