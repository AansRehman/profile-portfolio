package com.aans.portfolio.service;

import com.aans.portfolio.entities.AboutLine;
import com.aans.portfolio.entities.AboutSection;
import com.aans.portfolio.entities.CertificationEntry;
import com.aans.portfolio.entities.ExperienceEntry;
import com.aans.portfolio.entities.PortfolioProject;
import com.aans.portfolio.entities.ProfileCore;
import com.aans.portfolio.entities.SocialLinkEntry;
import com.aans.portfolio.entities.SkillCategory;
import com.aans.portfolio.entities.SkillItem;
import com.aans.portfolio.entities.UniversityEducation;
import com.aans.portfolio.repo.*;
import com.aans.portfolio.models.CertificationDto;
import com.aans.portfolio.models.ContactDto;
import com.aans.portfolio.models.EducationDto;
import com.aans.portfolio.models.ExperienceDto;
import com.aans.portfolio.models.PriorEducationDto;
import com.aans.portfolio.models.ProfileResponse;
import com.aans.portfolio.models.SocialLinkDto;
import com.aans.portfolio.models.ProjectDto;
import com.aans.portfolio.models.SkillCategoryDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioReadService {

  private final ProfileCoreRepository profileCoreRepository;
  private final AboutLineRepository aboutLineRepository;
  private final UniversityEducationRepository universityEducationRepository;
  private final PriorEducationRepository priorEducationRepository;
  private final ExperienceEntryRepository experienceEntryRepository;
  private final SkillCategoryRepository skillCategoryRepository;
  private final PortfolioProjectRepository portfolioProjectRepository;
  private final CertificationRepository certificationRepository;
  private final SocialLinkRepository socialLinkRepository;

  public ProfileResponse getPublicProfile() {
    ProfileCore core =
        profileCoreRepository
            .findById(1L)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not seeded"));
    UniversityEducation uni =
        universityEducationRepository
            .findById(1L)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not seeded"));

    List<String> aboutMe = lines(AboutSection.ABOUT_ME);
    List<String> whatIBring = lines(AboutSection.WHAT_I_BRING);
    List<String> journey = lines(AboutSection.JOURNEY);

    ContactDto contact =
        new ContactDto(core.getPhone(), core.getEmail(), core.getGithub(), core.getLinkedin());
    EducationDto education =
        new EducationDto(
            uni.getSchool(), uni.getDegree(), uni.getLocation(), uni.getPeriod(), uni.getCourses());

    List<PriorEducationDto> prior =
        priorEducationRepository.findAllByOrderByIdAsc().stream()
            .map(p -> new PriorEducationDto(p.getSchool(), p.getCredential(), p.getPeriod()))
            .toList();

    List<ExperienceDto> experiences =
        experienceEntryRepository.findAllByOrderByIdAsc().stream().map(this::toExperienceDto).toList();

    List<SkillCategoryDto> skills =
        skillCategoryRepository.findAllByOrderBySortOrderAsc().stream().map(this::toSkillDto).toList();

    List<ProjectDto> projects =
        portfolioProjectRepository.findAllByOrderByIdAsc().stream().map(this::toProjectDto).toList();

    List<CertificationDto> certs =
        certificationRepository.findAllByOrderByIdAsc().stream().map(this::toCertDto).toList();

    List<SocialLinkDto> socialLinks = buildSocialLinks(core);

    return new ProfileResponse(
        core.getPersonName(),
        core.getHeadline(),
        core.getSummary(),
        aboutMe,
        whatIBring,
        journey,
        core.getConnectBlurb(),
        contact,
        socialLinks,
        education,
        prior,
        experiences,
        skills,
        projects,
        certs);
  }

  private List<SocialLinkDto> buildSocialLinks(ProfileCore core) {
    List<SocialLinkEntry> rows = socialLinkRepository.findAllByOrderByIdAsc();
    if (!rows.isEmpty()) {
      return rows.stream()
          .map(s -> new SocialLinkDto(s.getLabel().trim(), s.getUrl().trim()))
          .toList();
    }
    return fallbackSocialFromCore(core);
  }

  private static List<SocialLinkDto> fallbackSocialFromCore(ProfileCore core) {
    List<SocialLinkDto> list = new ArrayList<>();
    if (core.getGithub() != null && !core.getGithub().isBlank()) {
      list.add(new SocialLinkDto("GitHub", core.getGithub().trim()));
    }
    if (core.getLinkedin() != null && !core.getLinkedin().isBlank()) {
      list.add(new SocialLinkDto("LinkedIn", core.getLinkedin().trim()));
    }
    return List.copyOf(list);
  }

  private List<String> lines(AboutSection section) {
    return aboutLineRepository.findBySectionOrderBySortOrderAsc(section).stream()
        .map(AboutLine::getText)
        .toList();
  }

  private ExperienceDto toExperienceDto(ExperienceEntry e) {
    return new ExperienceDto(
        e.getCompany(),
        e.getClientNote(),
        e.getRoleTitle(),
        e.getPeriod(),
        List.copyOf(e.getBullets()));
  }

  private SkillCategoryDto toSkillDto(SkillCategory c) {
    List<String> items =
        c.getItems().stream().sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()))
            .map(SkillItem::getLabel)
            .toList();
    return new SkillCategoryDto(c.getTitle(), items);
  }

  private ProjectDto toProjectDto(PortfolioProject p) {
    String demo = p.getDemoUrl() != null && !p.getDemoUrl().isBlank() ? p.getDemoUrl() : null;
    return new ProjectDto(
        p.getName(), p.getStack(), p.getPeriod(), List.copyOf(p.getBullets()), demo);
  }

  private CertificationDto toCertDto(CertificationEntry c) {
    String d = c.getDateAwarded() != null && !c.getDateAwarded().isBlank() ? c.getDateAwarded() : null;
    String u =
        c.getCertificateUrl() != null && !c.getCertificateUrl().isBlank()
            ? c.getCertificateUrl().trim()
            : null;
    return new CertificationDto(c.getTitle(), c.getIssuer(), d, u);
  }
}
