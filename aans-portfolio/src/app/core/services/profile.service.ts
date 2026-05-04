import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import type { Profile } from '../../models/resume.model';

@Injectable({ providedIn: 'root' })
export class ProfileService {
  readonly profile = signal<Profile | null>(null);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  constructor(private readonly http: HttpClient) {}

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<Profile>(`${environment.apiBase}/profile`).subscribe({
      next: (p) => {
        this.profile.set(p);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(
          'Could not load profile from the API. Start the Spring Boot app (port 8080) and run ng serve with proxy.',
        );
        this.loading.set(false);
      },
    });
  }
}
