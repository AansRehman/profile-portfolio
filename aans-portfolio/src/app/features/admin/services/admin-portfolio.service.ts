import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import type {
  AboutSectionPath,
  AdminUser,
  AdminUserCreateBody,
  AdminProfile,
  CertificationAdmin,
  CertificationWriteBody,
  ExperienceAdmin,
  ExperienceWriteBody,
  PriorEducationAdmin,
  PriorEducationWriteBody,
  ProfileCoreBody,
  ProjectAdmin,
  ProjectWriteBody,
  SkillCategoryWriteBody,
  SocialLinkAdmin,
  SocialLinkWriteBody,
  UniversityEducationBody,
  UserRole,
} from '../models/admin-api.model';

@Injectable({ providedIn: 'root' })
export class AdminPortfolioService {
  private readonly base = `${environment.apiBase}/admin`;
  private readonly resumeBase = environment.resumeBuilderBase;

  constructor(private readonly http: HttpClient) {}

  getProfile(): Observable<AdminProfile> {
    return this.http.get<AdminProfile>(`${this.base}/profile`);
  }

  updateCore(body: ProfileCoreBody): Observable<void> {
    return this.http.put<void>(`${this.base}/core`, body);
  }

  replaceAbout(section: AboutSectionPath, lines: string[]): Observable<void> {
    return this.http.put<void>(`${this.base}/about/${section}`, lines);
  }

  updateUniversity(body: UniversityEducationBody): Observable<void> {
    return this.http.put<void>(`${this.base}/education/university`, body);
  }

  listExperiences(): Observable<ExperienceAdmin[]> {
    return this.http.get<ExperienceAdmin[]>(`${this.base}/experiences`);
  }

  createExperience(body: ExperienceWriteBody): Observable<ExperienceAdmin> {
    return this.http.post<ExperienceAdmin>(`${this.base}/experiences`, body);
  }

  updateExperience(id: number, body: ExperienceWriteBody): Observable<ExperienceAdmin> {
    return this.http.put<ExperienceAdmin>(`${this.base}/experiences/${id}`, body);
  }

  deleteExperience(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/experiences/${id}`);
  }

  listPriorEducation(): Observable<PriorEducationAdmin[]> {
    return this.http.get<PriorEducationAdmin[]>(`${this.base}/prior-education`);
  }

  createPriorEducation(body: PriorEducationWriteBody): Observable<PriorEducationAdmin> {
    return this.http.post<PriorEducationAdmin>(`${this.base}/prior-education`, body);
  }

  updatePriorEducation(
    id: number,
    body: PriorEducationWriteBody,
  ): Observable<PriorEducationAdmin> {
    return this.http.put<PriorEducationAdmin>(`${this.base}/prior-education/${id}`, body);
  }

  deletePriorEducation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/prior-education/${id}`);
  }

  listProjects(): Observable<ProjectAdmin[]> {
    return this.http.get<ProjectAdmin[]>(`${this.base}/projects`);
  }

  createProject(body: ProjectWriteBody): Observable<ProjectAdmin> {
    return this.http.post<ProjectAdmin>(`${this.base}/projects`, body);
  }

  updateProject(id: number, body: ProjectWriteBody): Observable<ProjectAdmin> {
    return this.http.put<ProjectAdmin>(`${this.base}/projects/${id}`, body);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/projects/${id}`);
  }

  listCertifications(): Observable<CertificationAdmin[]> {
    return this.http.get<CertificationAdmin[]>(`${this.base}/certifications`);
  }

  createCertification(body: CertificationWriteBody): Observable<CertificationAdmin> {
    return this.http.post<CertificationAdmin>(`${this.base}/certifications`, body);
  }

  updateCertification(
    id: number,
    body: CertificationWriteBody,
  ): Observable<CertificationAdmin> {
    return this.http.put<CertificationAdmin>(`${this.base}/certifications/${id}`, body);
  }

  deleteCertification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/certifications/${id}`);
  }

  replaceSkills(body: SkillCategoryWriteBody[]): Observable<void> {
    return this.http.put<void>(`${this.base}/skills`, body);
  }

  listSocialLinks(): Observable<SocialLinkAdmin[]> {
    return this.http.get<SocialLinkAdmin[]>(`${this.base}/social-links`);
  }

  createSocialLink(body: SocialLinkWriteBody): Observable<SocialLinkAdmin> {
    return this.http.post<SocialLinkAdmin>(`${this.base}/social-links`, body);
  }

  updateSocialLink(id: number, body: SocialLinkWriteBody): Observable<SocialLinkAdmin> {
    return this.http.put<SocialLinkAdmin>(`${this.base}/social-links/${id}`, body);
  }

  deleteSocialLink(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/social-links/${id}`);
  }

  buildResumePdf(): Observable<Blob> {
    return this.http.post(`${this.resumeBase}/api/resume/build`, null, {
      responseType: 'blob',
    });
  }

  listUsers(): Observable<AdminUser[]> {
    return this.http.get<AdminUser[]>(`${this.base}/users`);
  }

  createUser(body: AdminUserCreateBody): Observable<AdminUser> {
    return this.http.post<AdminUser>(`${this.base}/users`, body);
  }

  updateUserRole(id: number, role: UserRole): Observable<AdminUser> {
    return this.http.put<AdminUser>(`${this.base}/users/${id}/role`, { role });
  }
}
