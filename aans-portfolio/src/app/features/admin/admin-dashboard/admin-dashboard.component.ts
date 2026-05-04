import {
  Component,
  DestroyRef,
  HostListener,
  OnInit,
  computed,
  inject,
  signal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  FormArray,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { AuthService } from '../../../core/services/auth.service';
import { AdminPortfolioService } from '../services/admin-portfolio.service';
import type {
  AboutSectionPath,
  AdminProfile,
  CertificationAdmin,
  ExperienceAdmin,
  PriorEducationAdmin,
  ProjectAdmin,
  SkillCategoryWriteBody,
  SocialLinkAdmin,
} from '../models/admin-api.model';

function linesFromText(raw: string): string[] {
  return raw
    .split(/\r?\n/)
    .map((s) => s.trim())
    .filter(Boolean);
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: false,
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss',
})
export class AdminDashboardComponent implements OnInit {
  private readonly destroyRef = inject(DestroyRef);

  readonly loading = signal(true);
  readonly apiMsg = signal<string | null>(null);

  /** True when categories differ from last load/save baseline. */
  readonly skillsDirty = signal(false);
  /** Matches an empty categories array until the first rebuild from profile. */
  private skillsSerializedBaseline = '[]';

  readonly coreDirty = signal(false);
  readonly universityDirty = signal(false);
  /** Prior/add-or-edit draft differs from baseline (empty or selected row snapshot). */
  readonly priorDraftDirty = signal(false);
  readonly aboutMeDirty = signal(false);
  readonly aboutWhatDirty = signal(false);
  readonly aboutJourneyDirty = signal(false);

  /** Suppress dirty updates while PATCHing controls from GET /profile. */
  private hydratingFromServer = false;

  private coreSerializedBaseline = '';
  private universitySerializedBaseline = '';
  private priorSerializedBaseline = '';
  private aboutBaselineAboutMe = '';
  private aboutBaselineWhatIBring = '';
  private aboutBaselineJourney = '';

  readonly experiences = signal<ExperienceAdmin[]>([]);
  readonly priorEducation = signal<PriorEducationAdmin[]>([]);
  readonly projects = signal<ProjectAdmin[]>([]);
  readonly certifications = signal<CertificationAdmin[]>([]);
  readonly socialLinksRows = signal<SocialLinkAdmin[]>([]);

  readonly experienceEditingId = signal<number | null>(null);
  readonly priorEditingId = signal<number | null>(null);
  readonly projectEditingId = signal<number | null>(null);
  readonly certEditingId = signal<number | null>(null);
  readonly socialEditingId = signal<number | null>(null);

  readonly experienceSubmitLabel = computed(() =>
    this.experienceEditingId() != null ? 'Save experience' : 'Add experience',
  );
  readonly priorSubmitLabel = computed(() =>
    this.priorEditingId() != null ? 'Save prior education' : 'Add prior education',
  );
  readonly projectSubmitLabel = computed(() =>
    this.projectEditingId() != null ? 'Save project' : 'Add project',
  );
  readonly certSubmitLabel = computed(() =>
    this.certEditingId() != null ? 'Save certification' : 'Add certification',
  );
  readonly socialSubmitLabel = computed(() =>
    this.socialEditingId() != null ? 'Save social link' : 'Add social link',
  );

  readonly experienceModalOpen = signal(false);
  readonly priorEducationModalOpen = signal(false);
  readonly projectModalOpen = signal(false);
  readonly certModalOpen = signal(false);
  readonly socialModalOpen = signal(false);
  readonly skillCategoryModalOpen = signal(false);

  readonly socialForm = this.fb.nonNullable.group({
    label: ['', Validators.required],
    url: ['', Validators.required],
  });

  /** Kept for PATCH /core when saving name/phone/email (GitHub / LinkedIn live under Social links + DB fallback). */
  private coreMirrorGithub = '';
  private coreMirrorLinkedin = '';

  readonly skillCategoryModalForm = this.fb.nonNullable.group({
    title: ['', Validators.required],
    itemsText: ['', Validators.required],
  });

  readonly aboutTextsForm = this.fb.nonNullable.group({
    aboutMe: [''],
    whatIBring: [''],
    journey: [''],
  });

  readonly coreForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    headline: ['', Validators.required],
    summary: ['', Validators.required],
    connectBlurb: ['', Validators.required],
    phone: ['', Validators.required],
    email: ['', Validators.required],
  });

  readonly universityForm = this.fb.nonNullable.group({
    school: ['', Validators.required],
    degree: ['', Validators.required],
    location: ['', Validators.required],
    period: ['', Validators.required],
    coursesText: ['', Validators.required],
  });

  readonly experienceForm = this.fb.nonNullable.group({
    company: ['', Validators.required],
    clientOrNote: [''],
    role: ['', Validators.required],
    period: ['', Validators.required],
    bulletsText: ['', Validators.required],
  });

  readonly priorForm = this.fb.nonNullable.group({
    school: ['', Validators.required],
    credential: ['', Validators.required],
    period: ['', Validators.required],
  });

  readonly projectForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    stack: ['', Validators.required],
    period: ['', Validators.required],
    demoUrl: [''],
    bulletsText: ['', Validators.required],
  });

  readonly certForm = this.fb.nonNullable.group({
    title: ['', Validators.required],
    issuer: ['', Validators.required],
    date: [''],
    url: [''],
  });

  readonly skillForm = this.fb.group({
    categories: this.fb.array<FormGroup>([]),
  });

  constructor(
    private readonly adminApi: AdminPortfolioService,
    private readonly fb: FormBuilder,
    readonly auth: AuthService,
  ) {
    this.capturePersistBaselines();
    this.capturePriorDraftBaseline();
    this.captureSkillsBaseline();

    this.skillForm.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshSkillsDirtyFlag());

    this.coreForm.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshPersistDirtyFlags());

    this.aboutTextsForm.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshPersistDirtyFlags());

    this.universityForm.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshPersistDirtyFlags());

    this.priorForm.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.refreshPriorDraftDirtyFlag());
  }

  ngOnInit(): void {
    this.loadAll();
  }

  @HostListener('document:keydown', ['$event'])
  onDocumentKeydown(event: KeyboardEvent): void {
    if (event.key !== 'Escape') return;
    if (this.skillCategoryModalOpen()) {
      this.cancelSkillCategoryModal();
      return;
    }
    if (this.socialModalOpen()) {
      this.cancelSocialEdit();
      return;
    }
    if (this.certModalOpen()) {
      this.cancelCertEdit();
      return;
    }
    if (this.projectModalOpen()) {
      this.cancelProjectEdit();
      return;
    }
    if (this.priorEducationModalOpen()) {
      this.cancelPriorEdit();
      return;
    }
    if (this.experienceModalOpen()) {
      this.cancelExperienceEdit();
    }
  }

  openExperienceAddModal(): void {
    this.cancelExperienceEdit();
    this.experienceModalOpen.set(true);
  }

  openPriorEducationAddModal(): void {
    this.cancelPriorEdit();
    this.priorEducationModalOpen.set(true);
  }

  openProjectAddModal(): void {
    this.cancelProjectEdit();
    this.projectModalOpen.set(true);
  }

  openCertAddModal(): void {
    this.cancelCertEdit();
    this.certModalOpen.set(true);
  }

  openSocialAddModal(): void {
    this.cancelSocialEdit();
    this.socialModalOpen.set(true);
  }

  openSkillCategoryModal(): void {
    this.skillCategoryModalForm.reset();
    this.skillCategoryModalOpen.set(true);
  }

  cancelSkillCategoryModal(): void {
    this.skillCategoryModalForm.reset();
    this.skillCategoryModalOpen.set(false);
  }

  submitSkillCategoryModal(): void {
    if (this.skillCategoryModalForm.invalid) return;
    const v = this.skillCategoryModalForm.getRawValue();
    this.skillCategoriesArray.push(
      this.fb.nonNullable.group({
        title: [v.title.trim(), Validators.required],
        itemsText: [v.itemsText, Validators.required],
      }),
    );
    this.cancelSkillCategoryModal();
  }

  backdropCloseSkills(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.cancelSkillCategoryModal();
    }
  }

  backdropCloseExperience(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.cancelExperienceEdit();
    }
  }

  backdropClosePrior(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.cancelPriorEdit();
    }
  }

  backdropCloseProject(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.cancelProjectEdit();
    }
  }

  backdropCloseSocial(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.cancelSocialEdit();
    }
  }

  backdropCloseCert(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.cancelCertEdit();
    }
  }

  get skillCategoriesArray(): FormArray<FormGroup> {
    return this.skillForm.get('categories') as FormArray<FormGroup>;
  }

  /** Scroll to an in-page target; stays on admin dashboard (no route or hash navigation). */
  scrollToSection(elementId: string): void {
    const el = document.getElementById(elementId);
    if (!el) {
      return;
    }
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  logout(): void {
    this.auth.logout();
  }

  loadAll(): void {
    this.loading.set(true);
    forkJoin({
      profile: this.adminApi.getProfile(),
      experiences: this.adminApi.listExperiences(),
      prior: this.adminApi.listPriorEducation(),
      projects: this.adminApi.listProjects(),
      certs: this.adminApi.listCertifications(),
      socialLinks: this.adminApi.listSocialLinks(),
    })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: ({ profile, experiences, prior, projects, certs, socialLinks }) => {
          this.apiMsg.set(null);
          this.patchFormsFromProfile(profile);
          this.experiences.set(experiences);
          this.priorEducation.set(prior);
          this.projects.set(projects);
          this.certifications.set(certs);
          this.socialLinksRows.set(socialLinks);
        },
        error: () =>
          this.apiMsg.set(
            'Failed to load admin data. Check that you are signed in as ADMIN and the API is running.',
          ),
      });
  }

  private patchFormsFromProfile(p: AdminProfile): void {
    this.hydratingFromServer = true;
    try {
      this.coreMirrorGithub = p.contact.github ?? '';
      this.coreMirrorLinkedin = p.contact.linkedin ?? '';
      this.coreForm.patchValue({
        name: p.name,
        headline: p.headline,
        summary: p.summary,
        connectBlurb: p.connectBlurb,
        phone: p.contact.phone,
        email: p.contact.email,
      });
      this.aboutTextsForm.patchValue({
        aboutMe: p.aboutMe.join('\n'),
        whatIBring: p.whatIBring.join('\n'),
        journey: p.journeyNotes.join('\n'),
      });
      this.universityForm.patchValue({
        school: p.education.school,
        degree: p.education.degree,
        location: p.education.location,
        period: p.education.period,
        coursesText: p.education.courses.join('\n'),
      });
      this.skillCategoriesArray.clear();
      for (const c of p.skillCategories) {
        this.skillCategoriesArray.push(
          this.fb.nonNullable.group({
            title: [c.title, Validators.required],
            itemsText: [c.items.join('\n'), Validators.required],
          }),
        );
      }
    } finally {
      this.hydratingFromServer = false;
    }
    this.captureSkillsBaseline();
    this.capturePersistBaselines();
  }

  private serializeCoreState(): string {
    const v = this.coreForm.getRawValue();
    return JSON.stringify({
      name: v.name.trim(),
      headline: v.headline.trim(),
      summary: v.summary.trim(),
      connectBlurb: v.connectBlurb.trim(),
      phone: v.phone.trim(),
      email: v.email.trim(),
    });
  }

  private serializeUniversityState(): string {
    const v = this.universityForm.getRawValue();
    return JSON.stringify({
      school: v.school.trim(),
      degree: v.degree.trim(),
      location: v.location.trim(),
      period: v.period.trim(),
      courses: linesFromText(v.coursesText ?? ''),
    });
  }

  private serializePriorFormState(): string {
    const v = this.priorForm.getRawValue();
    return JSON.stringify({
      school: (v.school ?? '').trim(),
      credential: (v.credential ?? '').trim(),
      period: (v.period ?? '').trim(),
    });
  }

  private capturePriorDraftBaseline(): void {
    this.priorSerializedBaseline = this.serializePriorFormState();
    this.priorDraftDirty.set(false);
  }

  private refreshPriorDraftDirtyFlag(): void {
    if (this.hydratingFromServer) return;
    this.priorDraftDirty.set(
      this.serializePriorFormState() !== this.priorSerializedBaseline,
    );
  }

  /** Normalized line list for about text areas (matches save payload). */
  private serializeAboutField(raw: string): string {
    return JSON.stringify(linesFromText(raw ?? ''));
  }

  private capturePersistBaselines(): void {
    this.coreSerializedBaseline = this.serializeCoreState();
    this.universitySerializedBaseline = this.serializeUniversityState();
    const a = this.aboutTextsForm.getRawValue();
    this.aboutBaselineAboutMe = this.serializeAboutField(a.aboutMe);
    this.aboutBaselineWhatIBring = this.serializeAboutField(a.whatIBring);
    this.aboutBaselineJourney = this.serializeAboutField(a.journey);
    this.coreDirty.set(false);
    this.universityDirty.set(false);
    this.aboutMeDirty.set(false);
    this.aboutWhatDirty.set(false);
    this.aboutJourneyDirty.set(false);
  }

  private refreshPersistDirtyFlags(): void {
    if (this.hydratingFromServer) return;
    const a = this.aboutTextsForm.getRawValue();
    this.coreDirty.set(this.serializeCoreState() !== this.coreSerializedBaseline);
    this.universityDirty.set(this.serializeUniversityState() !== this.universitySerializedBaseline);
    this.aboutMeDirty.set(this.serializeAboutField(a.aboutMe) !== this.aboutBaselineAboutMe);
    this.aboutWhatDirty.set(this.serializeAboutField(a.whatIBring) !== this.aboutBaselineWhatIBring);
    this.aboutJourneyDirty.set(this.serializeAboutField(a.journey) !== this.aboutBaselineJourney);
  }

  private serializeSkillsFormState(): string {
    const categories = this.skillCategoriesArray.controls.map((ctrl) => {
      const v = ctrl.getRawValue() as { title: string; itemsText: string };
      return {
        title: (v.title ?? '').trim(),
        items: linesFromText(v.itemsText ?? ''),
      };
    });
    return JSON.stringify(categories);
  }

  private captureSkillsBaseline(): void {
    this.skillsSerializedBaseline = this.serializeSkillsFormState();
    this.skillsDirty.set(false);
  }

  private refreshSkillsDirtyFlag(): void {
    if (this.hydratingFromServer) return;
    this.skillsDirty.set(this.serializeSkillsFormState() !== this.skillsSerializedBaseline);
  }

  isSkillsSaveDisabled(): boolean {
    if (this.loading()) return true;
    if (!this.skillsDirty()) return true;
    if (this.skillCategoriesArray.length === 0) return false;
    return this.skillForm.invalid;
  }

  isCoreSaveDisabled(): boolean {
    if (this.loading()) return true;
    if (!this.coreDirty()) return true;
    return this.coreForm.invalid;
  }

  isUniversitySaveDisabled(): boolean {
    if (this.loading()) return true;
    if (!this.universityDirty()) return true;
    return this.universityForm.invalid;
  }

  isPriorSaveDisabled(): boolean {
    if (this.loading()) return true;
    if (!this.priorDraftDirty()) return true;
    return this.priorForm.invalid;
  }

  isAboutSectionSaveDisabled(section: AboutSectionPath): boolean {
    if (this.loading()) return true;
    const dirty =
      section === 'ABOUT_ME'
        ? this.aboutMeDirty()
        : section === 'WHAT_I_BRING'
          ? this.aboutWhatDirty()
          : this.aboutJourneyDirty();
    return !dirty;
  }

  removeSkillCategoryRow(index: number): void {
    this.skillCategoriesArray.removeAt(index);
  }

  saveCore(): void {
    if (this.coreForm.invalid) {
      this.apiMsg.set('Core profile: fill all fields.');
      return;
    }
    const v = this.coreForm.getRawValue();
    this.adminApi
      .updateCore({
        name: v.name,
        headline: v.headline,
        summary: v.summary,
        connectBlurb: v.connectBlurb,
        contact: {
          phone: v.phone,
          email: v.email,
          github: this.coreMirrorGithub,
          linkedin: this.coreMirrorLinkedin,
        },
      })
      .subscribe({
        next: () => {
          this.apiMsg.set(null);
          this.loadAll();
        },
        error: () => this.apiMsg.set('Failed to save core profile.'),
      });
  }

  saveAbout(section: AboutSectionPath): void {
    const t = this.aboutTextsForm.getRawValue();
    const text =
      section === 'ABOUT_ME'
        ? t.aboutMe
        : section === 'WHAT_I_BRING'
          ? t.whatIBring
          : t.journey;
    this.adminApi.replaceAbout(section, linesFromText(text)).subscribe({
      next: () => {
        this.apiMsg.set(null);
        this.loadAll();
      },
      error: () => this.apiMsg.set(`Failed to save ${section.replaceAll('_', ' ')}.`),
    });
  }

  saveUniversity(): void {
    if (this.universityForm.invalid) {
      this.apiMsg.set('University: fill all fields.');
      return;
    }
    const v = this.universityForm.getRawValue();
    const courses = linesFromText(v.coursesText);
    if (!courses.length) {
      this.apiMsg.set('University: add at least one course (one per line).');
      return;
    }
    this.adminApi
      .updateUniversity({
        school: v.school,
        degree: v.degree,
        location: v.location,
        period: v.period,
        courses,
      })
      .subscribe({
        next: () => {
          this.apiMsg.set(null);
          this.loadAll();
        },
        error: () => this.apiMsg.set('Failed to save university education.'),
      });
  }

  saveSkills(): void {
    if (this.skillCategoriesArray.length > 0 && this.skillForm.invalid) {
      this.apiMsg.set('Skills: each category needs a title and items text.');
      return;
    }
    const body: SkillCategoryWriteBody[] = [];
    for (const g of this.skillCategoriesArray.controls) {
      const row = g.getRawValue();
      const title = (row['title'] as string).trim();
      const items = linesFromText(row['itemsText'] as string);
      if (!title || !items.length) {
        this.apiMsg.set(
          'Skills: every category must have a title and at least one item line. Remove empty rows or fix them.',
        );
        return;
      }
      body.push({ title, items });
    }
    this.adminApi.replaceSkills(body).subscribe({
      next: () => {
        this.apiMsg.set(null);
        this.loadAll();
      },
      error: () => this.apiMsg.set('Failed to save skills.'),
    });
  }

  submitExperience(): void {
    if (this.experienceForm.invalid) {
      this.apiMsg.set('Experience: fill required fields.');
      return;
    }
    const v = this.experienceForm.getRawValue();
    const bullets = linesFromText(v.bulletsText);
    if (!bullets.length) {
      this.apiMsg.set('Experience: add at least one bullet (one per line).');
      return;
    }
    const body = {
      company: v.company,
      clientOrNote: v.clientOrNote.trim() ? v.clientOrNote : null,
      role: v.role,
      period: v.period,
      bullets,
    };
    const id = this.experienceEditingId();
    const req =
      id != null
        ? this.adminApi.updateExperience(id, body)
        : this.adminApi.createExperience(body);
    req.subscribe({
      next: () => {
        this.apiMsg.set(null);
        this.cancelExperienceEdit();
        this.loadAll();
      },
      error: () =>
        this.apiMsg.set(id != null ? 'Failed to update experience.' : 'Failed to create experience.'),
    });
  }

  editExperience(row: ExperienceAdmin): void {
    this.experienceEditingId.set(row.id);
    this.experienceForm.patchValue(
      {
        company: row.company,
        clientOrNote: row.clientOrNote ?? '',
        role: row.role,
        period: row.period,
        bulletsText: row.bullets.join('\n'),
      },
      { emitEvent: false },
    );
    this.experienceModalOpen.set(true);
  }

  cancelExperienceEdit(): void {
    this.experienceEditingId.set(null);
    this.experienceForm.reset();
    this.experienceModalOpen.set(false);
  }

  deleteExperience(id: number): void {
    this.adminApi.deleteExperience(id).subscribe({
      next: () => {
        this.apiMsg.set(null);
        if (this.experienceEditingId() === id) {
          this.cancelExperienceEdit();
        }
        this.loadAll();
      },
      error: () => this.apiMsg.set('Failed to delete experience.'),
    });
  }

  submitPrior(): void {
    if (this.priorForm.invalid) return;
    const v = this.priorForm.getRawValue();
    const id = this.priorEditingId();
    const body = {
      school: v.school,
      credential: v.credential,
      period: v.period,
    };
    const req =
      id != null
        ? this.adminApi.updatePriorEducation(id, body)
        : this.adminApi.createPriorEducation(body);
    req.subscribe({
      next: () => {
        this.apiMsg.set(null);
        this.cancelPriorEdit();
        this.loadAll();
      },
      error: () =>
        this.apiMsg.set(id != null ? 'Failed to update prior education.' : 'Failed to create prior education.'),
    });
  }

  editPrior(row: PriorEducationAdmin): void {
    this.priorEditingId.set(row.id);
    this.priorForm.patchValue(row, { emitEvent: false });
    this.capturePriorDraftBaseline();
    this.priorEducationModalOpen.set(true);
  }

  cancelPriorEdit(): void {
    this.priorEditingId.set(null);
    this.priorForm.reset({ school: '', credential: '', period: '' }, { emitEvent: false });
    this.capturePriorDraftBaseline();
    this.priorEducationModalOpen.set(false);
  }

  deletePrior(id: number): void {
    this.adminApi.deletePriorEducation(id).subscribe({
      next: () => {
        this.apiMsg.set(null);
        if (this.priorEditingId() === id) this.cancelPriorEdit();
        this.loadAll();
      },
      error: () => this.apiMsg.set('Failed to delete prior education.'),
    });
  }

  submitProject(): void {
    if (this.projectForm.invalid) {
      this.apiMsg.set('Project: fill required fields.');
      return;
    }
    const v = this.projectForm.getRawValue();
    const bullets = linesFromText(v.bulletsText);
    if (!bullets.length) {
      this.apiMsg.set('Project: add at least one bullet line.');
      return;
    }
    const body = {
      name: v.name,
      stack: v.stack,
      period: v.period,
      demoUrl: v.demoUrl.trim() ? v.demoUrl.trim() : null,
      bullets,
    };
    const id = this.projectEditingId();
    const req =
      id != null ? this.adminApi.updateProject(id, body) : this.adminApi.createProject(body);
    req.subscribe({
      next: () => {
        this.apiMsg.set(null);
        this.cancelProjectEdit();
        this.loadAll();
      },
      error: () =>
        this.apiMsg.set(id != null ? 'Failed to update project.' : 'Failed to create project.'),
    });
  }

  editProject(row: ProjectAdmin): void {
    this.projectEditingId.set(row.id);
    this.projectForm.patchValue(
      {
        name: row.name,
        stack: row.stack,
        period: row.period,
        demoUrl: row.demoUrl ?? '',
        bulletsText: row.bullets.join('\n'),
      },
      { emitEvent: false },
    );
    this.projectModalOpen.set(true);
  }

  cancelProjectEdit(): void {
    this.projectEditingId.set(null);
    this.projectForm.reset();
    this.projectModalOpen.set(false);
  }

  deleteProject(id: number): void {
    this.adminApi.deleteProject(id).subscribe({
      next: () => {
        this.apiMsg.set(null);
        if (this.projectEditingId() === id) this.cancelProjectEdit();
        this.loadAll();
      },
      error: () => this.apiMsg.set('Failed to delete project.'),
    });
  }

  submitCert(): void {
    if (this.certForm.invalid) return;
    const v = this.certForm.getRawValue();
    const urlNorm = this.normalizeOptionalExternalUrl(v.url);
    const body = {
      title: v.title,
      issuer: v.issuer,
      date: v.date.trim() ? v.date.trim() : null,
      url: urlNorm,
    };
    const id = this.certEditingId();
    const req =
      id != null
        ? this.adminApi.updateCertification(id, body)
        : this.adminApi.createCertification(body);
    req.subscribe({
      next: () => {
        this.apiMsg.set(null);
        this.cancelCertEdit();
        this.loadAll();
      },
      error: () =>
        this.apiMsg.set(id != null ? 'Failed to update certification.' : 'Failed to create certification.'),
    });
  }

  editCert(row: CertificationAdmin): void {
    this.certEditingId.set(row.id);
    this.certForm.patchValue(
      {
        title: row.title,
        issuer: row.issuer,
        date: row.date ?? '',
        url: row.url ?? '',
      },
      { emitEvent: false },
    );
    this.certModalOpen.set(true);
  }

  private normalizeOptionalExternalUrl(raw: string): string | null {
    const t = raw.trim();
    if (!t) return null;
    return /^https?:\/\//i.test(t) ? t : `https://${t}`;
  }

  cancelCertEdit(): void {
    this.certEditingId.set(null);
    this.certForm.reset();
    this.certModalOpen.set(false);
  }

  deleteCert(id: number): void {
    this.adminApi.deleteCertification(id).subscribe({
      next: () => {
        this.apiMsg.set(null);
        if (this.certEditingId() === id) this.cancelCertEdit();
        this.loadAll();
      },
      error: () => this.apiMsg.set('Failed to delete certification.'),
    });
  }

  submitSocialLink(): void {
    if (this.socialForm.invalid) return;
    const v = this.socialForm.getRawValue();
    const urlNorm = this.normalizeOptionalExternalUrl(v.url);
    if (!urlNorm) {
      this.apiMsg.set('Enter a URL for the social link.');
      return;
    }
    const body = {
      label: v.label.trim(),
      url: urlNorm,
    };
    const id = this.socialEditingId();
    const req =
      id != null ? this.adminApi.updateSocialLink(id, body) : this.adminApi.createSocialLink(body);
    req.subscribe({
      next: () => {
        this.apiMsg.set(null);
        this.cancelSocialEdit();
        this.loadAll();
      },
      error: () =>
        this.apiMsg.set(id != null ? 'Failed to update social link.' : 'Failed to create social link.'),
    });
  }

  editSocialLink(row: SocialLinkAdmin): void {
    this.socialEditingId.set(row.id);
    this.socialForm.patchValue({ label: row.label, url: row.url }, { emitEvent: false });
    this.socialModalOpen.set(true);
  }

  cancelSocialEdit(): void {
    this.socialEditingId.set(null);
    this.socialForm.reset();
    this.socialModalOpen.set(false);
  }

  deleteSocialLink(id: number): void {
    this.adminApi.deleteSocialLink(id).subscribe({
      next: () => {
        this.apiMsg.set(null);
        if (this.socialEditingId() === id) this.cancelSocialEdit();
        this.loadAll();
      },
      error: () => this.apiMsg.set('Failed to delete social link.'),
    });
  }
}
