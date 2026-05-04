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
}

export interface PriorEducationAdmin {
  id: number;
  school: string;
  credential: string;
  period: string;
}

export interface ProjectAdmin {
  id: number;
  name: string;
  stack: string;
  period: string;
  demoUrl: string | null;
  bullets: string[];
}

export interface SocialLinkAdmin extends SocialLink {
  id: number;
}

export interface SocialLinkWriteBody {
  label: string;
  url: string;
}

export interface CertificationAdmin {
  id: number;
  title: string;
  issuer: string;
  date: string | null;
  url: string | null;
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
}

export interface PriorEducationWriteBody {
  school: string;
  credential: string;
  period: string;
}

export interface ProjectWriteBody {
  name: string;
  stack: string;
  period: string;
  demoUrl: string | null;
  bullets: string[];
}

export interface CertificationWriteBody {
  title: string;
  issuer: string;
  date: string | null;
  url: string | null;
}

export interface SkillCategoryWriteBody {
  title: string;
  items: string[];
}
