import type { Profile, SocialLink } from '../../../models/resume.model';

/** Full profile JSON from GET /api/admin/profile (same shape as public). */
export type AdminProfile = Profile;

export type AboutSectionPath = 'ABOUT_ME' | 'WHAT_I_BRING' | 'JOURNEY';

export interface ExperienceAdmin {
  id: number;
  company: string;
  clientOrNote?: string | null;
  role: string;
  period: string;
  bullets: string[];
  includeInResume: boolean;
}

export interface PriorEducationAdmin {
  id: number;
  school: string;
  credential: string;
  period: string;
  includeInResume: boolean;
}

export interface ProjectAdmin {
  id: number;
  name: string;
  stack: string;
  period: string;
  demoUrl: string | null;
  bullets: string[];
  includeInResume: boolean;
}

export interface SocialLinkAdmin extends SocialLink {
  id: number;
  includeInResume: boolean;
}

export interface SocialLinkWriteBody {
  label: string;
  url: string;
  includeInResume: boolean;
}

export interface CertificationAdmin {
  id: number;
  title: string;
  issuer: string;
  date: string | null;
  url: string | null;
  includeInResume: boolean;
}

export interface ProfileCoreBody {
  name: string;
  headline: string;
  summary: string;
  connectBlurb: string;
  contact: {
    phone: string;
    email: string;
    github: string;
    linkedin: string;
  };
}

export interface UniversityEducationBody {
  school: string;
  degree: string;
  location: string;
  period: string;
  courses: string[];
}

export interface ExperienceWriteBody {
  company: string;
  clientOrNote: string | null;
  role: string;
  period: string;
  bullets: string[];
  includeInResume: boolean;
}

export interface PriorEducationWriteBody {
  school: string;
  credential: string;
  period: string;
  includeInResume: boolean;
}

export interface ProjectWriteBody {
  name: string;
  stack: string;
  period: string;
  demoUrl: string | null;
  bullets: string[];
  includeInResume: boolean;
}

export interface CertificationWriteBody {
  title: string;
  issuer: string;
  date: string | null;
  url: string | null;
  includeInResume: boolean;
}

export interface SkillCategoryWriteBody {
  title: string;
  items: string[];
}
