import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';

/** Any logged-in user (admin or viewer). */
@Injectable({ providedIn: 'root' })
export class ViewerAuthGuard implements CanActivate {
  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
  ) {}

  canActivate(): boolean | UrlTree {
    if (this.auth.isAuthenticated()) {
      return true;
    }
    return this.router.createUrlTree(['/admin/login']);
  }
}
