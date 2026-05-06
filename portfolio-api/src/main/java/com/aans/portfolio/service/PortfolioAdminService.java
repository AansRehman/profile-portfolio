package com.aans.portfolio.service;

import com.aans.portfolio.entities.AboutLine;
import com.aans.portfolio.entities.AboutSection;
import com.aans.portfolio.entities.CertificationEntry;
import com.aans.portfolio.entities.ExperienceEntry;
import com.aans.portfolio.entities.PortfolioProject;
import com.aans.portfolio.entities.PriorEducationEntry;
import com.aans.portfolio.entities.ProfileCore;
import com.aans.portfolio.entities.SocialLinkEntry;
import com.aans.portfolio.entities.SkillCategory;
import com.aans.portfolio.entities.SkillItem;
import com.aans.portfolio.entities.UniversityEducation;
import com.aans.portfolio.repo.AboutLineRepository;
import com.aans.portfolio.repo.CertificationRepository;
import com.aans.portfolio.repo.ExperienceEntryRepository;
import com.aans.portfolio.repo.PortfolioProjectRepository;
import com.aans.portfolio.repo.PriorEducationRepository;
import com.aans.portfolio.repo.ProfileCoreRepository;
import com.aans.portfolio.repo.SocialLinkRepository;
import com.aans.portfolio.repo.SkillCategoryRepository;
import com.aans.portfolio.repo.UniversityEducationRepository;
import com.aans.portfolio.models.CertificationAdminDto;
import com.aans.portfolio.models.CertificationWriteRequest;
import com.aans.portfolio.models.ExperienceAdminDto;
import com.aans.portfolio.models.ExperienceWriteRequest;
import com.aans.portfolio.models.PriorEducationAdminDto;
import com.aans.portfolio.models.PriorEducationWriteRequest;
import com.aans.portfolio.models.ProfileCoreUpdateRequest;
import com.aans.portfolio.models.ProjectAdminDto;
import com.aans.portfolio.models.ProjectWriteRequest;
import com.aans.portfolio.models.SkillCategoryWriteRequest;
import com.aans.portfolio.models.SocialLinkAdminDto;
import com.aans.portfolio.models.SocialLinkWriteRequest;
import com.aans.portfolio.models.UniversityEducationUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PortfolioAdminService {

  private final ProfileCoreRepository profileCoreRepository;
  private final AboutLineRepository aboutLineRepository;
  private final UniversityEducationRepository universityEducationRepository;
  private final PriorEducationRepository priorEducationRepository;
  private final ExperienceEntryRepository experienceEntryRepository;
  private final SkillCategoryRepository skillCategoryRepository;
  private final PortfolioProjectRepository portfolioProjectRepository;
  private final CertificationRepository certificationRepository;
  private final SocialLinkRepository socialLinkRepository;

  @Transactional
  public void updateCore(ProfileCoreUpdateRequest req) {
    ProfileCore c =
        profileCoreRepository
            .findById(1L)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    c.setPersonName(req.name());
    c.setHeadline(req.headline());
    c.setSummary(req.summary());
    c.setConnectBlurb(req.connectBlurb());
    c.setPhone(req.contact().phone());
    c.setEmail(req.contact().email());
    c.setGithub(req.contact().github());
    c.setLinkedin(req.contact().linkedin());
    profileCoreRepository.save(c);
  }

  @Transactional
  public void replaceAboutSection(AboutSection section, List<String> lines) {
    aboutLineRepository.deleteBySection(section);
    int order = 0;
    for (String text : lines) {
      AboutLine line = new AboutLine();
      line.setSection(section);
      line.setSortOrder(order++);
      line.setText(text);
      aboutLineRepository.save(line);
    }
  }

  @Transactional
  public void updateUniversity(UniversityEducationUpdateRequest req) {
    UniversityEducation u =
        universityEducationRepository
            .findById(1L)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    u.setSchool(req.school());
    u.setDegree(req.degree());
    u.setLocation(req.location());
    u.setPeriod(req.period());
    u.getCourses().clear();
    u.getCourses().addAll(req.courses());
    universityEducationRepository.save(u);
  }

  public List<ExperienceAdminDto> listExperiences() {
    return experienceEntryRepository.findAllByOrderByIdAsc().stream().map(this::toExpAdmin).toList();
  }

  @Transactional
  public ExperienceAdminDto createExperience(ExperienceWriteRequest req) {
    ExperienceEntry e = fromWrite(req);
    return toExpAdmin(experienceEntryRepository.save(e));
  }

  @Transactional
  public ExperienceAdminDto updateExperience(long id, ExperienceWriteRequest req) {
    ExperienceEntry e =
        experienceEntryRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    e.setCompany(req.company());
    e.setClientNote(req.clientOrNote());
    e.setRoleTitle(req.role());
    e.setPeriod(req.period());
    e.getBullets().clear();
    e.getBullets().addAll(req.bullets());
    e.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
    return toExpAdmin(experienceEntryRepository.save(e));
  }

  @Transactional
  public void deleteExperience(long id) {
    if (!experienceEntryRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    experienceEntryRepository.deleteById(id);
  }

  public List<PriorEducationAdminDto> listPriorEducation() {
    return priorEducationRepository.findAllByOrderByIdAsc().stream()
        .map(
            p ->
                new PriorEducationAdminDto(
                    p.getId(),
                    p.getSchool(),
                    p.getCredential(),
                    p.getPeriod(),
                    includeFlagOrDefault(p.getIncludeInResume())))
        .toList();
  }

  @Transactional
  public PriorEducationAdminDto createPriorEducation(PriorEducationWriteRequest req) {
    PriorEducationEntry p = new PriorEducationEntry();
    p.setSchool(req.school());
    p.setCredential(req.credential());
    p.setPeriod(req.period());
    p.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
    PriorEducationEntry saved = priorEducationRepository.save(p);
    return new PriorEducationAdminDto(
        saved.getId(),
        saved.getSchool(),
        saved.getCredential(),
        saved.getPeriod(),
        includeFlagOrDefault(saved.getIncludeInResume()));
  }

  @Transactional
  public PriorEducationAdminDto updatePriorEducation(long id, PriorEducationWriteRequest req) {
    PriorEducationEntry p =
        priorEducationRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    p.setSchool(req.school());
    p.setCredential(req.credential());
    p.setPeriod(req.period());
    p.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
    PriorEducationEntry saved = priorEducationRepository.save(p);
    return new PriorEducationAdminDto(
        saved.getId(),
        saved.getSchool(),
        saved.getCredential(),
        saved.getPeriod(),
        includeFlagOrDefault(saved.getIncludeInResume()));
  }

  @Transactional
  public void deletePriorEducation(long id) {
    if (!priorEducationRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    priorEducationRepository.deleteById(id);
  }

  public List<ProjectAdminDto> listProjects() {
    return portfolioProjectRepository.findAllByOrderByIdAsc().stream().map(this::toProjectAdmin).toList();
  }

  @Transactional
  public ProjectAdminDto createProject(ProjectWriteRequest req) {
    PortfolioProject p = fromProjectWrite(req);
    return toProjectAdmin(portfolioProjectRepository.save(p));
  }

  @Transactional
  public ProjectAdminDto updateProject(long id, ProjectWriteRequest req) {
    PortfolioProject p =
        portfolioProjectRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    applyProject(p, req);
    return toProjectAdmin(portfolioProjectRepository.save(p));
  }

  @Transactional
  public void deleteProject(long id) {
    if (!portfolioProjectRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    portfolioProjectRepository.deleteById(id);
  }

  public List<CertificationAdminDto> listCertifications() {
    return certificationRepository.findAllByOrderByIdAsc().stream().map(this::toCertAdmin).toList();
  }

  @Transactional
  public CertificationAdminDto createCertification(CertificationWriteRequest req) {
    CertificationEntry c = fromCertWrite(req);
    return toCertAdmin(certificationRepository.save(c));
  }

  @Transactional
  public CertificationAdminDto updateCertification(long id, CertificationWriteRequest req) {
    CertificationEntry c =
        certificationRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    c.setTitle(req.title());
    c.setIssuer(req.issuer());
    c.setDateAwarded(req.date());
    c.setCertificateUrl(normalizeCertUrl(req.url()));
    c.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
    return toCertAdmin(certificationRepository.save(c));
  }

  @Transactional
  public void deleteCertification(long id) {
    if (!certificationRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    certificationRepository.deleteById(id);
  }

  public List<SocialLinkAdminDto> listSocialLinks() {
    return socialLinkRepository.findAllByOrderByIdAsc().stream().map(this::toSocialAdmin).toList();
  }

  @Transactional
  public SocialLinkAdminDto createSocialLink(SocialLinkWriteRequest req) {
    SocialLinkEntry e = new SocialLinkEntry();
    e.setLabel(req.label().trim());
    e.setUrl(normalizeSocialUrl(req.url()));
    e.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
    return toSocialAdmin(socialLinkRepository.save(e));
  }

  @Transactional
  public SocialLinkAdminDto updateSocialLink(long id, SocialLinkWriteRequest req) {
    SocialLinkEntry e =
        socialLinkRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    e.setLabel(req.label().trim());
    e.setUrl(normalizeSocialUrl(req.url()));
    e.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
    return toSocialAdmin(socialLinkRepository.save(e));
  }

  @Transactional
  public void deleteSocialLink(long id) {
    if (!socialLinkRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    socialLinkRepository.deleteById(id);
  }

  @Transactional
  public void replaceSkillCategories(List<SkillCategoryWriteRequest> categories) {
    skillCategoryRepository.deleteAll();
    int catOrder = 0;
    for (SkillCategoryWriteRequest catReq : categories) {
      SkillCategory cat = new SkillCategory();
      cat.setTitle(catReq.title());
      cat.setSortOrder(catOrder++);
      cat.setIncludeInResume(includeFlagOrDefault(catReq.includeInResume()));
      int itemOrder = 0;
      List<String> labels = catReq.items() != null ? catReq.items() : List.of();
      for (String label : labels) {
        SkillItem it = new SkillItem();
        it.setCategory(cat);
        it.setLabel(label);
        it.setSortOrder(itemOrder++);
        cat.getItems().add(it);
      }
      skillCategoryRepository.save(cat);
    }
  }

  private ExperienceEntry fromWrite(ExperienceWriteRequest req) {
    ExperienceEntry e = new ExperienceEntry();
    e.setCompany(req.company());
    e.setClientNote(req.clientOrNote());
    e.setRoleTitle(req.role());
    e.setPeriod(req.period());
    e.setBullets(new ArrayList<>(req.bullets()));
    e.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
    return e;
  }

  private ExperienceAdminDto toExpAdmin(ExperienceEntry e) {
    return new ExperienceAdminDto(
        e.getId(),
        e.getCompany(),
        e.getClientNote(),
        e.getRoleTitle(),
        e.getPeriod(),
        List.copyOf(e.getBullets()),
        includeFlagOrDefault(e.getIncludeInResume()));
  }

  private void applyProject(PortfolioProject p, ProjectWriteRequest req) {
    p.setName(req.name());
    p.setStack(req.stack());
    p.setPeriod(req.period());
    p.setDemoUrl(req.demoUrl());
    p.getBullets().clear();
    p.getBullets().addAll(req.bullets());
    p.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
  }

  private PortfolioProject fromProjectWrite(ProjectWriteRequest req) {
    PortfolioProject p = new PortfolioProject();
    applyProject(p, req);
    return p;
  }

  private ProjectAdminDto toProjectAdmin(PortfolioProject p) {
    return new ProjectAdminDto(
        p.getId(),
        p.getName(),
        p.getStack(),
        p.getPeriod(),
        p.getDemoUrl(),
        List.copyOf(p.getBullets()),
        includeFlagOrDefault(p.getIncludeInResume()));
  }

  private CertificationEntry fromCertWrite(CertificationWriteRequest req) {
    CertificationEntry c = new CertificationEntry();
    c.setTitle(req.title());
    c.setIssuer(req.issuer());
    c.setDateAwarded(req.date());
    c.setCertificateUrl(normalizeCertUrl(req.url()));
    c.setIncludeInResume(includeFlagOrDefault(req.includeInResume()));
    return c;
  }

  private CertificationAdminDto toCertAdmin(CertificationEntry c) {
    String u =
        c.getCertificateUrl() != null && !c.getCertificateUrl().isBlank()
            ? c.getCertificateUrl().trim()
            : null;
    return new CertificationAdminDto(
        c.getId(),
        c.getTitle(),
        c.getIssuer(),
        c.getDateAwarded(),
        u,
        includeFlagOrDefault(c.getIncludeInResume()));
  }

  private static String normalizeCertUrl(String raw) {
    if (raw == null || raw.isBlank()) return null;
    String t = raw.trim();
    if (!t.regionMatches(true, 0, "http://", 0, 7)
        && !t.regionMatches(true, 0, "https://", 0, 8)) {
      t = "https://" + t;
    }
    return t;
  }

  private SocialLinkAdminDto toSocialAdmin(SocialLinkEntry s) {
    return new SocialLinkAdminDto(
        s.getId(),
        s.getLabel().trim(),
        s.getUrl().trim(),
        includeFlagOrDefault(s.getIncludeInResume()));
  }

  private static String normalizeSocialUrl(String raw) {
    String t = raw == null ? "" : raw.trim();
    if (!t.regionMatches(true, 0, "http://", 0, 7)
        && !t.regionMatches(true, 0, "https://", 0, 8)) {
      t = "https://" + t;
    }
    return t;
  }

  private static boolean includeFlagOrDefault(Boolean includeInResume) {
    return includeInResume == null || includeInResume;
  }
}
