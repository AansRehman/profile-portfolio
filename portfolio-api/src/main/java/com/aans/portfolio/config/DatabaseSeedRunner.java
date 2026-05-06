package com.aans.portfolio.config;

import com.aans.portfolio.entities.AboutLine;
import com.aans.portfolio.entities.AboutSection;
import com.aans.portfolio.entities.AppUser;
import com.aans.portfolio.entities.CertificationEntry;
import com.aans.portfolio.entities.ExperienceEntry;
import com.aans.portfolio.entities.PortfolioProject;
import com.aans.portfolio.entities.PriorEducationEntry;
import com.aans.portfolio.entities.ProfileCore;
import com.aans.portfolio.entities.SocialLinkEntry;
import com.aans.portfolio.entities.Role;
import com.aans.portfolio.entities.SkillCategory;
import com.aans.portfolio.entities.SkillItem;
import com.aans.portfolio.entities.UniversityEducation;
import com.aans.portfolio.repo.AboutLineRepository;
import com.aans.portfolio.repo.AppUserRepository;
import com.aans.portfolio.repo.CertificationRepository;
import com.aans.portfolio.repo.ExperienceEntryRepository;
import com.aans.portfolio.repo.PortfolioProjectRepository;
import com.aans.portfolio.repo.PriorEducationRepository;
import com.aans.portfolio.repo.ProfileCoreRepository;
import com.aans.portfolio.repo.SocialLinkRepository;
import com.aans.portfolio.repo.SkillCategoryRepository;
import com.aans.portfolio.repo.UniversityEducationRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(1)
@RequiredArgsConstructor
public class DatabaseSeedRunner implements CommandLineRunner {

  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final ProfileCoreRepository profileCoreRepository;
  private final AboutLineRepository aboutLineRepository;
  private final UniversityEducationRepository universityEducationRepository;
  private final PriorEducationRepository priorEducationRepository;
  private final ExperienceEntryRepository experienceEntryRepository;
  private final SkillCategoryRepository skillCategoryRepository;
  private final PortfolioProjectRepository portfolioProjectRepository;
  private final CertificationRepository certificationRepository;
  private final SocialLinkRepository socialLinkRepository;

  @Override
  @Transactional
  public void run(String... args) {
    if (appUserRepository.count() > 0) {
      return;
    }
    appUserRepository.save(
        new AppUser(null, "admin", passwordEncoder.encode("ChangeMe_Admin!"), Role.ADMIN));
    appUserRepository.save(
        new AppUser(null, "viewer", passwordEncoder.encode("ChangeMe_Viewer!"), Role.VIEWER));

    ProfileCore core = new ProfileCore();
    core.setId(1L);
    core.setPersonName("Aans Rehman Khan");
    core.setHeadline("Java Software Engineer @ easypaisa · Tekrowe Digital");
    core.setSummary(
        "Software engineer focused on Java Spring Boot, secure APIs, and scalable backends. "
            + "I work across the SDLC—from design to delivery—with experience in DevOps, MLOps, and Python for ML-style problems. "
            + "Motivated by new tech, clear engineering, and teams that ship.");
    core.setConnectBlurb(
        "Open to unique projects, new experiences, and conversations with like-minded engineers. "
            + "Message on LinkedIn or email for collaboration or a casual chat.");
    core.setPhone("+92 318 5428090");
    core.setEmail("aansrehman10@gmail.com");
    core.setGithub("https://github.com/AansRehman");
    core.setLinkedin("https://www.linkedin.com/in/aans-rehman-khan-229061223");
    profileCoreRepository.save(core);

    socialLinkRepository.save(socialLink("GitHub", core.getGithub()));
    socialLinkRepository.save(socialLink("LinkedIn", core.getLinkedin()));

    addAbout(
        AboutSection.ABOUT_ME,
        List.of(
            "Motivated by technology and continuous learning; first in my family to pursue a career in IT.",
            "Problem-solving mindset—comfortable owning challenges end to end and shipping through all SDLC phases.",
            "Strong specialization in Java Spring Boot and the MERN stack, with practical exposure to DevOps, MLOps, and Python.",
            "Enjoy applying Python in data- and ML-oriented projects when the problem calls for it."));
    addAbout(
        AboutSection.WHAT_I_BRING,
        List.of(
            "Software engineering: modern web and backend development with Java Spring Boot and MERN, emphasizing secure, scalable applications.",
            "DevOps & MLOps: smoother integration and delivery through repeatable pipelines and automation-minded workflows.",
            "Machine learning: using Python to build practical, intelligent pieces of larger systems."));
    addAbout(
        AboutSection.JOURNEY,
        List.of(
            "Background that made university and early career a deliberate climb—turned that into drive in software and product work.",
            "Outside work: regular cricket and football; long tours and outdoor challenges when time allows."));

    UniversityEducation uni = new UniversityEducation();
    uni.setId(1L);
    uni.setSchool("National University of Computer and Emerging Sciences (FAST-NUCES)");
    uni.setDegree("BS Software Engineering");
    uni.setLocation("Islamabad, Pakistan");
    uni.setPeriod("Aug 2020 — Aug 2024");
    uni.getCourses()
        .addAll(
            List.of(
                "MLOps",
                "Natural Language Processing",
                "Process Mining",
                "Software Design and Analysis",
                "Artificial Intelligence",
                "Blockchain Development",
                "Software Construction and Development",
                "Data Structures",
                "Algorithms",
                "Software Requirement Engineering"));
    universityEducationRepository.save(uni);

    priorEducationRepository.save(
        pe(
            "Army Public School (APSACS)",
            "Intermediate · Computer Science",
            "Mar 2018 — Mar 2020"));
    priorEducationRepository.save(
        pe("Army Public School (APSACS)", "Matric · Computer Science", "Mar 2016 — Feb 2018"));

    experienceEntryRepository.save(
        ex(
            "Tekrowe Digital",
            "easypaisa",
            "Java Software Engineer",
            "Apr 2026 — Present",
            List.of(
                "Collaborating with easypaisa core engineering teams as an on-site vendor resource for fintech solutions.",
                "Contributing to high-traffic financial services and product development within the easypaisa ecosystem.",
                "Managing the full SDLC to ensure seamless integration between vendor services and client platforms.")));
    experienceEntryRepository.save(
        ex(
            "Elixir Technologies",
            null,
            "Software Engineer",
            "Sep 2025 — Apr 2026",
            List.of(
                "Developed and maintained RESTful APIs with Spring Boot on the backend team.",
                "Worked across key modules—bug fixes, small features, and learning production architecture and standards.",
                "Partnered with senior engineers on architecture, code quality, and deployment practices.")));
    experienceEntryRepository.save(
        ex(
            "Poshmaal Technologies",
            null,
            "Java Spring Boot Developer",
            "Oct 2024 — Sep 2025",
            List.of(
                "Delivered Java Spring Boot work on complex, enterprise-style applications.",
                "Helped translate product ideas into working software within the team.",
                "Explored and adopted newer tooling where it improved delivery and quality.")));
    experienceEntryRepository.save(
        ex(
            "Poshmaal Technologies",
            null,
            "Intern",
            "Jan 2024 — Oct 2024",
            List.of(
                "Hands-on experience with Java Spring Boot in complex and enterprise-level applications.",
                "Supported features and fixes while ramping up on team practices and codebase structure.")));
    experienceEntryRepository.save(
        ex(
            "Evamp & Saanga",
            null,
            "Intern",
            "May 2022 — Jul 2022",
            List.of(
                "Introduction to Java Spring Boot and building REST APIs with team guidance.")));

    saveSkillCategory(
        0,
        "Languages",
        List.of("Java", "React", "JavaScript", "Python", "Git", "Bash", "SQL"));
    saveSkillCategory(
        1,
        "Frameworks & stacks",
        List.of("Spring Boot", "Next.js", "Quarkus", "Micronaut", "Thymeleaf", "JavaFX", "MERN Stack"));
    saveSkillCategory(
        2,
        "DevOps & API tools",
        List.of("Git", "GitHub", "Docker", "Jenkins", "Kubernetes", "AirFlow", "Postman", "Swagger"));
    saveSkillCategory(3, "Databases", List.of("MySQL", "MySQL Server", "PostgreSQL", "MongoDB"));
    saveSkillCategory(
        4,
        "Other",
        List.of(
            "Apache Kafka",
            "Spring Security",
            "Hibernate",
            "Maven",
            "Eureka",
            "Load Balancer",
            "GraphQL",
            "API Gateway",
            "Microservices",
            "Design Patterns",
            "Redis"));

    portfolioProjectRepository.save(
        proj(
            "Elixir Cloud",
            "Java 8/17, Spring Boot, React",
            "Sept 2025 — Present",
            null,
            List.of(
                "Built and managed RESTful APIs using Spring Boot, integrating with frontend.",
                "Implemented complex Criteria Queries and optimized database schema using Hibernate ORM for data consistency.",
                "Worked on both backend (Java 8/17) and frontend (React), ensuring seamless integration and maintainable architecture.",
                "Wrote clean, scalable, and secure code following industry best practices.")));
    portfolioProjectRepository.save(
        proj(
            "Firefly (E-commerce Platform Migration)",
            "Java, Spring Boot, GraphQL, JWT",
            "Dec 2024 — Aug 2025",
            "https://prodcheck.firefly.online",
            List.of(
                "Migrated legacy JavaScript backend to Java Spring Boot, achieving 50% performance improvement.",
                "Implemented secure GraphQL APIs with JWT authentication.",
                "Built modular, production-grade codebase supporting scalability and enhanced maintainability.")));
    portfolioProjectRepository.save(
        proj(
            "ZoomedXL — Courier Services",
            "Java, Spring Boot, WebSocket, PostgreSQL",
            "Jan 2025 — Jun 2025",
            null,
            List.of(
                "Developed a RESTful API using Spring Boot to manage courier job creation, assignment, and tracking.",
                "Integrated WebSocket for real-time driver search updates and a bidirectional chat system between customers and drivers.",
                "Integrated WebSocket for real-time chatting between the driver and dispatcher.",
                "Collaborated with front-end developers to ensure seamless integration using Swagger API documentation.")));

    certificationRepository.save(
        cert(
            "IBM Applied Software Engineering Fundamentals Specialization",
            "Coursera",
            "Dec 2023",
            null));
    certificationRepository.save(
        cert(
            "Building Scalable Java Microservices with Spring Boot and Spring Cloud",
            "Google · Coursera",
            "Apr 2023",
            null));
    certificationRepository.save(
        cert("Python for Data Science, AI & Development", "IBM · Coursera", null, null));
    certificationRepository.save(
        cert("Java: Advanced Concepts for High-Performance Development", "LinkedIn Learning", null, null));
    certificationRepository.save(cert("Getting Started with Git and GitHub", "Coursera", null, null));
    certificationRepository.save(
        cert("Hands-on Introduction to Linux Commands and Shell Scripting", "Coursera", null, null));
    certificationRepository.save(
        cert("Developing AI Applications with Python and Flask", "Coursera", null, null));
  }

  private void addAbout(AboutSection section, List<String> lines) {
    int o = 0;
    for (String text : lines) {
      AboutLine line = new AboutLine();
      line.setSection(section);
      line.setSortOrder(o++);
      line.setText(text);
      aboutLineRepository.save(line);
    }
  }

  private PriorEducationEntry pe(String school, String credential, String period) {
    PriorEducationEntry p = new PriorEducationEntry();
    p.setSchool(school);
    p.setCredential(credential);
    p.setPeriod(period);
    p.setIncludeInResume(true);
    return p;
  }

  private ExperienceEntry ex(
      String company, String clientNote, String roleTitle, String period, List<String> bullets) {
    ExperienceEntry e = new ExperienceEntry();
    e.setCompany(company);
    e.setClientNote(clientNote);
    e.setRoleTitle(roleTitle);
    e.setPeriod(period);
    e.setBullets(new ArrayList<>(bullets));
    e.setIncludeInResume(true);
    return e;
  }

  private void saveSkillCategory(int sortOrder, String title, List<String> labels) {
    SkillCategory cat = new SkillCategory();
    cat.setTitle(title);
    cat.setSortOrder(sortOrder);
    cat.setIncludeInResume(true);
    int io = 0;
    for (String label : labels) {
      SkillItem it = new SkillItem();
      it.setCategory(cat);
      it.setLabel(label);
      it.setSortOrder(io++);
      cat.getItems().add(it);
    }
    skillCategoryRepository.save(cat);
  }

  private PortfolioProject proj(String name, String stack, String period, String demo, List<String> bullets) {
    PortfolioProject p = new PortfolioProject();
    p.setName(name);
    p.setStack(stack);
    p.setPeriod(period);
    p.setDemoUrl(demo);
    p.setBullets(new ArrayList<>(bullets));
    p.setIncludeInResume(true);
    return p;
  }

  private CertificationEntry cert(
      String title, String issuer, String date, String certificateUrl) {
    CertificationEntry c = new CertificationEntry();
    c.setTitle(title);
    c.setIssuer(issuer);
    c.setDateAwarded(date);
    c.setCertificateUrl(certificateUrl);
    c.setIncludeInResume(true);
    return c;
  }

  private SocialLinkEntry socialLink(String label, String url) {
    SocialLinkEntry s = new SocialLinkEntry();
    s.setLabel(label);
    s.setUrl(url);
    s.setIncludeInResume(true);
    return s;
  }
}
