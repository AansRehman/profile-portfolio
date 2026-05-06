import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

const TOKEN_KEY = 'portfolio_jwt';
const ROLE_KEY = 'portfolio_role';
const USER_KEY = 'portfolio_user';

export interface LoginResult {
  token: string;
  username: string;
  role: string;
}

export type AppRole = 'ADMIN' | 'EDITOR' | 'VIEWER';

@Injectable({ providedIn: 'root' })
export class AuthService {
  readonly username = signal<string | null>(null);

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router,
  ) {
    if (typeof localStorage !== 'undefined') {
      this.username.set(localStorage.getItem(USER_KEY));
    }
  }

  login(username: string, password: string) {
    return this.http.post<LoginResult>(`${environment.apiBase}/auth/login`, {
      username,
      password,
    });
  }

  saveSession(result: LoginResult): void {
    localStorage.setItem(TOKEN_KEY, result.token);
    localStorage.setItem(ROLE_KEY, result.role);
    localStorage.setItem(USER_KEY, result.username);
    this.username.set(result.username);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
    localStorage.removeItem(USER_KEY);
    this.username.set(null);
    this.router.navigateByUrl('/admin/login');
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getRole(): string | null {
    return localStorage.getItem(ROLE_KEY);
  }

  getRoleTyped(): AppRole | null {
    const role = this.getRole();
    return role === 'ADMIN' || role === 'EDITOR' || role === 'VIEWER' ? role : null;
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  isEditor(): boolean {
    return this.getRole() === 'EDITOR';
  }

  isViewer(): boolean {
    return this.getRole() === 'VIEWER';
  }

  canEditProfile(): boolean {
    return this.isAdmin() || this.isEditor();
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
