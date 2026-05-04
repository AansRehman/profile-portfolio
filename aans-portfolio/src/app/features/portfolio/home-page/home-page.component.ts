import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../../core/services/profile.service';

@Component({
  selector: 'app-home-page',
  standalone: false,
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss',
})
export class HomePageComponent implements OnInit {
  constructor(readonly ps: ProfileService) {}

  ngOnInit(): void {
    this.ps.load();
  }

  get phoneTel(): string {
    const p = this.ps.profile();
    if (!p) return '';
    return 'tel:' + p.contact.phone.replace(/\s/g, '');
  }
}
