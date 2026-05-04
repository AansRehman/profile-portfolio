package com.aans.portfolio.controllers;

import com.aans.portfolio.entities.AboutSection;
import com.aans.portfolio.models.CertificationAdminDto;
import com.aans.portfolio.models.CertificationWriteRequest;
import com.aans.portfolio.models.ExperienceAdminDto;
import com.aans.portfolio.models.ExperienceWriteRequest;
import com.aans.portfolio.models.PriorEducationAdminDto;
import com.aans.portfolio.models.PriorEducationWriteRequest;
import com.aans.portfolio.models.ProfileCoreUpdateRequest;
import com.aans.portfolio.models.ProfileResponse;
import com.aans.portfolio.models.ProjectAdminDto;
import com.aans.portfolio.models.ProjectWriteRequest;
import com.aans.portfolio.models.SkillCategoryWriteRequest;
import com.aans.portfolio.models.SocialLinkAdminDto;
import com.aans.portfolio.models.SocialLinkWriteRequest;
import com.aans.portfolio.models.UniversityEducationUpdateRequest;
import com.aans.portfolio.service.PortfolioAdminService;
import com.aans.portfolio.service.PortfolioReadService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminPortfolioController {

  private final PortfolioReadService portfolioReadService;
  private final PortfolioAdminService portfolioAdminService;

  @GetMapping("/profile")
  public ProfileResponse getProfile() {
    return portfolioReadService.getPublicProfile();
  }

  @PutMapping("/core")
  public void updateCore(@Valid @RequestBody ProfileCoreUpdateRequest body) {
    portfolioAdminService.updateCore(body);
  }

  @PutMapping("/about/{section}")
  public void replaceAbout(
      @PathVariable AboutSection section, @RequestBody List<String> lines) {
    portfolioAdminService.replaceAboutSection(section, lines);
  }

  @PutMapping("/education/university")
  public void updateUniversity(@Valid @RequestBody UniversityEducationUpdateRequest body) {
    portfolioAdminService.updateUniversity(body);
  }

  @GetMapping("/experiences")
  public List<ExperienceAdminDto> listExperiences() {
    return portfolioAdminService.listExperiences();
  }

  @PostMapping("/experiences")
  public ExperienceAdminDto createExperience(@Valid @RequestBody ExperienceWriteRequest body) {
    return portfolioAdminService.createExperience(body);
  }

  @PutMapping("/experiences/{id}")
  public ExperienceAdminDto updateExperience(
      @PathVariable long id, @Valid @RequestBody ExperienceWriteRequest body) {
    return portfolioAdminService.updateExperience(id, body);
  }

  @DeleteMapping("/experiences/{id}")
  public void deleteExperience(@PathVariable long id) {
    portfolioAdminService.deleteExperience(id);
  }

  @GetMapping("/prior-education")
  public List<PriorEducationAdminDto> listPrior() {
    return portfolioAdminService.listPriorEducation();
  }

  @PostMapping("/prior-education")
  public PriorEducationAdminDto createPrior(@Valid @RequestBody PriorEducationWriteRequest body) {
    return portfolioAdminService.createPriorEducation(body);
  }

  @PutMapping("/prior-education/{id}")
  public PriorEducationAdminDto updatePrior(
      @PathVariable long id, @Valid @RequestBody PriorEducationWriteRequest body) {
    return portfolioAdminService.updatePriorEducation(id, body);
  }

  @DeleteMapping("/prior-education/{id}")
  public void deletePrior(@PathVariable long id) {
    portfolioAdminService.deletePriorEducation(id);
  }

  @GetMapping("/projects")
  public List<ProjectAdminDto> listProjects() {
    return portfolioAdminService.listProjects();
  }

  @PostMapping("/projects")
  public ProjectAdminDto createProject(@Valid @RequestBody ProjectWriteRequest body) {
    return portfolioAdminService.createProject(body);
  }

  @PutMapping("/projects/{id}")
  public ProjectAdminDto updateProject(
      @PathVariable long id, @Valid @RequestBody ProjectWriteRequest body) {
    return portfolioAdminService.updateProject(id, body);
  }

  @DeleteMapping("/projects/{id}")
  public void deleteProject(@PathVariable long id) {
    portfolioAdminService.deleteProject(id);
  }

  @GetMapping("/certifications")
  public List<CertificationAdminDto> listCerts() {
    return portfolioAdminService.listCertifications();
  }

  @PostMapping("/certifications")
  public CertificationAdminDto createCert(@Valid @RequestBody CertificationWriteRequest body) {
    return portfolioAdminService.createCertification(body);
  }

  @PutMapping("/certifications/{id}")
  public CertificationAdminDto updateCert(
      @PathVariable long id, @Valid @RequestBody CertificationWriteRequest body) {
    return portfolioAdminService.updateCertification(id, body);
  }

  @DeleteMapping("/certifications/{id}")
  public void deleteCert(@PathVariable long id) {
    portfolioAdminService.deleteCertification(id);
  }

  @PutMapping("/skills")
  public void replaceSkills(@Valid @RequestBody List<SkillCategoryWriteRequest> body) {
    portfolioAdminService.replaceSkillCategories(body);
  }

  @GetMapping("/social-links")
  public List<SocialLinkAdminDto> listSocialLinks() {
    return portfolioAdminService.listSocialLinks();
  }

  @PostMapping("/social-links")
  public SocialLinkAdminDto createSocialLink(@Valid @RequestBody SocialLinkWriteRequest body) {
    return portfolioAdminService.createSocialLink(body);
  }

  @PutMapping("/social-links/{id}")
  public SocialLinkAdminDto updateSocialLink(
      @PathVariable long id, @Valid @RequestBody SocialLinkWriteRequest body) {
    return portfolioAdminService.updateSocialLink(id, body);
  }

  @DeleteMapping("/social-links/{id}")
  public void deleteSocialLink(@PathVariable long id) {
    portfolioAdminService.deleteSocialLink(id);
  }
}
