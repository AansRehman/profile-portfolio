export interface ContactLinks {
  phone: string;
  email: string;
  github: string;
  linkedin: string;
}

export interface SocialLink {
  label: string;
  url: string;
}

export interface Education {
  school: string;
  degree: string;
  location: string;
  period: string;
  courses: string[];
}

export interface PriorEducation {
  school: string;
  credential: string;
  period: string;
}

export interface Experience {
  company: string;
  clientOrNote?: string;
  role: string;
  period: string;
  bullets: string[];
}

export interface SkillCategory {
  title: string;
  items: string[];
}

export interface Project {
  name: string;
  stack: string;
  period: string;
  bullets: string[];
  demoUrl?: string;
}

export interface Certification {
  title: string;
  issuer: string;
  date?: string;
  /** Credential / issuer page; opens in new tab when set */
  url?: string;
}

export interface Profile {
  name: string;
  headline: string;
  summary: string;
  aboutMe: string[];
  whatIBring: string[];
  journeyNotes: string[];
  connectBlurb: string;
  contact: ContactLinks;
  /** Shown after phone & email; from admin social links or legacy GitHub / LinkedIn on profile core when empty */
  socialLinks: SocialLink[];
  education: Education;
  priorEducation: PriorEducation[];
  experience: Experience[];
  skillCategories: SkillCategory[];
  projects: Project[];
  certifications: Certification[];
}
