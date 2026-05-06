package com.aans.resume.model;

import java.util.List;

public record ResumeProfile(
    String name,
    String headline,
    String summary,
    List<String> aboutMe,
    List<String> whatIBring,
    List<String> journeyNotes,
    String connectBlurb,
    Contact contact,
    List<SocialLink> socialLinks,
    Education education,
    List<PriorEducation> priorEducation,
    List<Experience> experience,
    List<SkillCategory> skillCategories,
    List<Project> projects,
    List<Certification> certifications) {

  public record Contact(String phone, String email, String github, String linkedin) {}

  public record SocialLink(String label, String url, Boolean includeInResume) {}

  public record Education(
      String school, String degree, String location, String period, List<String> courses) {}

  public record PriorEducation(
      String school, String credential, String period, Boolean includeInResume) {}

  public record Experience(
      String company,
      String clientOrNote,
      String role,
      String period,
      List<String> bullets,
      Boolean includeInResume) {}

  public record SkillCategory(String title, List<String> items, Boolean includeInResume) {}

  public record Project(
      String name,
      String stack,
      String period,
      List<String> bullets,
      String demoUrl,
      Boolean includeInResume) {}

  public record Certification(
      String title, String issuer, String date, String url, Boolean includeInResume) {}
}
