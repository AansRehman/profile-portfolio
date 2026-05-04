import { HttpClient } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../core/services/auth.service';

interface MeResponse {
  username: string;
  role: string;
}

@Component({
  selector: 'app-viewer-hub',
  standalone: false,
  templateUrl: './viewer-hub.component.html',
  styleUrl: './viewer-hub.component.scss',
})
export class ViewerHubComponent implements OnInit {
  readonly me = signal<MeResponse | null>(null);
  readonly error = signal<string | null>(null);

  constructor(
    private readonly http: HttpClient,
    readonly auth: AuthService,
  ) {}

  ngOnInit(): void {
    this.http.get<MeResponse>(`${environment.apiBase}/me`).subscribe({
      next: (m) => this.me.set(m),
      error: () => this.error.set('Could not load account info.'),
    });
  }

  logout(): void {
    this.auth.logout();
  }
}
