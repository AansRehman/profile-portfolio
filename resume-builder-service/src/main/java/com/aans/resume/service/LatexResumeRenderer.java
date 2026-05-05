package com.aans.resume.service;

import com.aans.resume.model.ResumeProfile;
import com.aans.resume.model.ResumeProfile.Certification;
import com.aans.resume.model.ResumeProfile.Education;
import com.aans.resume.model.ResumeProfile.Experience;
import com.aans.resume.model.ResumeProfile.PriorEducation;
import com.aans.resume.model.ResumeProfile.Project;
import com.aans.resume.model.ResumeProfile.SkillCategory;
import com.aans.resume.model.ResumeProfile.SocialLink;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Emits LaTeX matching the bundled {@code russell} CV class and the user's Overleaf layout
 * (summary → education → experience → skills → projects → certifications).
 */
@Service
public class LatexResumeRenderer {
  private static final String SECTION_TIGHTEN = "\\vspace{0.35em}\n";
  private static final String ENTRY_GAP = "\\vspace{0.5em}\n";

  public String render(ResumeProfile p) {
    StringBuilder o = new StringBuilder(20_000);
    o.append("% Auto-generated from portfolio-api /api/resume/source (russell template)\n");
    o.append("\\documentclass[9pt, letterpaper]{russell}\n");
    o.append("\\usepackage{graphicx}\n");
    o.append("\\geometry{left=1.4cm, top=.8cm, right=1.4cm, bottom=0.5cm, footskip=.5cm}\n");
    o.append("\\fontdir[fonts/]\n");
    o.append("\\colorlet{russell}{russell-black}\n");
    o.append("\\setbool{acvSectionColorHighlight}{false}\n");
    o.append("\\renewcommand{\\acvHeaderSocialSep}{\\quad\\textbar\\quad}\n\n");
    // Balanced default spacing between major sections/content blocks.
    o.append("\\renewcommand{\\acvSectionTopSkip}{0.9mm}\n");
    o.append("\\renewcommand{\\acvSectionContentTopSkip}{0.8mm}\n\n");

    NameParts ns = splitName(p.name());
    o.append("\\name{\\bf{")
        .append(tex(ns.first()))
        .append("}}{\\bf{")
        .append(tex(ns.last()))
        .append("}}\n");

    String headline = nz(p.headline());
    if (!headline.isBlank()) {
      o.append("\\position{").append(tex(headline)).append("}\n");
    }

    ResumeProfile.Contact c = p.contact();
    if (c != null) {
      if (!nz(c.phone()).isBlank()) {
        o.append("\\mobile{").append(tex(c.phone())).append("}\n");
      }
      if (!nz(c.email()).isBlank()) {
        o.append("\\email{").append(tex(c.email())).append("}\n");
      }

      String gh = githubForHeader(p);
      String li = linkedinForHeader(p);
      if (!gh.isBlank()) {
        o.append("\\github{").append(tex(githubPathForRussell(gh))).append("}\n");
      }
      if (!li.isBlank()) {
        o.append("\\linkedin{").append(tex(linkedinPathForRussell(li))).append("}\n");
      }

      String extra = extraSocialHeader(p.socialLinks(), gh, li);
      if (!extra.isBlank()) {
        o.append("\\extrainfo{").append(extra).append("}\n");
      }
    }

    o.append("\n\\begin{document}\n\n");
    o.append("\\makecvheader\n");
    o.append("\\vspace{-0.2em}\n\n");

    appendSummary(o, p);
    appendEducation(o, p);
    appendExperience(o, p);
    appendSkills(o, p);
    appendProjects(o, p);
    appendCertifications(o, p);

    o.append("\\end{document}\n");
    return o.toString();
  }

  private static void appendSummary(StringBuilder o, ResumeProfile p) {
    String s = nz(p.summary());
    if (s.isBlank()) {
      return;
    }
    o.append("\\vspace{6pt}\n\\begin{cvparagraph}\n");
    o.append(tex(s));
    o.append("\n\\end{cvparagraph}\n\\vspace{2pt}\n");
  }

  private static void appendEducation(StringBuilder o, ResumeProfile p) {
    Education edu = p.education();
    List<PriorEducation> prior = p.priorEducation();
    boolean hasUni = edu != null;
    boolean hasPrior = prior != null && prior.stream().anyMatch(LatexResumeRenderer::hasRenderablePrior);
    if (!hasUni && !hasPrior) {
      return;
    }

    o.append("\\cvsection{EDUCATION}\n").append(SECTION_TIGHTEN);
    if (hasUni) {
      o.append("\\begin{cventries2}\n");
      o.append("\\cventrynew\n    {{\\bf ")
          .append(tex(nz(edu.school())))
          .append("}, \\textit{")
          .append(tex(nz(edu.degree())))
          .append("} | ")
          .append(tex(nz(edu.location())))
          .append("}\n    {")
          .append(tex(nz(edu.period())))
          .append("}\n    {}\n");
      o.append("\\end{cventries2}\n");
    }

    if (hasUni && edu.courses() != null && !edu.courses().isEmpty()) {
      o.append("\\begin{cvparagraph}\n{\\normalsize{ \\bf{Courses}:} ");
      o.append(
          edu.courses().stream()
              .map(LatexResumeRenderer::tex)
              .collect(Collectors.joining(", ")));
      o.append(".\\\\}\n\\end{cvparagraph}\n");
    }

    if (hasPrior) {
      o.append("\\begin{cventries2}\n");
      for (PriorEducation pe : prior) {
        if (!hasRenderablePrior(pe)) {
          continue;
        }
        o.append("\\cventrynew\n    {{\\bf ").append(tex(nz(pe.school())));
        String credential = nz(pe.credential()).trim();
        if (!credential.isBlank()) {
          o.append("}, \\textit{").append(tex(credential)).append("}");
        } else {
          o.append("}");
        }
        o.append("}\n    {")
            .append(tex(nz(pe.period())))
            .append("}\n    {}\n");
      }
      o.append("\\end{cventries2}\n");
    }
  }

  private static void appendExperience(StringBuilder o, ResumeProfile p) {
    List<Experience> rows = p.experience();
    if (rows == null || rows.isEmpty()) {
      return;
    }
    o.append("\\cvsection{EXPERIENCE}\n").append(SECTION_TIGHTEN).append("\\begin{cventries2}\n");
    for (int i = 0; i < rows.size(); i++) {
      Experience e = rows.get(i);
      String title = experienceTitleLine(e);
      List<String> bullets = normalizeLines(e.bullets());
      o.append("\\cventrynew\n    {")
          .append(title)
          .append("}\n    {")
          .append(tex(nz(e.period())))
          .append("}\n    ");
      if (bullets.isEmpty()) {
        o.append("{}\n");
      } else {
        o.append("{\n      \\begin{cvitems}\n");
        for (String b : bullets) {
          o.append("        \\item {").append(tex(b)).append("}\n");
        }
        o.append("      \\end{cvitems}\n    }\n");
      }
      // Hard boundary so the next \cventrynew never joins this entry's last line.
      o.append("\n");
//      o.append("\n");
      if (i < rows.size() - 1) {
        o.append("    ").append(ENTRY_GAP);
      }
    }
    o.append("\\end{cventries2}\n");
  }

  /** Matches template: {@code Company | client, \\textnormal{\\textit{role}}}. */
  private static String experienceTitleLine(Experience e) {
    String roleTex = "\\textnormal{\\textit{" + tex(nz(e.role())) + "}}";
    String companyTex = tex(nz(e.company()));
    String client = nz(e.clientOrNote());
    if (!client.isBlank()) {
      return companyTex + " | " + tex(client) + ", " + roleTex;
    }
    return companyTex + ", " + roleTex;
  }

  private static void appendSkills(StringBuilder o, ResumeProfile p) {
    List<SkillCategory> cats = p.skillCategories();
    if (cats == null || cats.isEmpty()) {
      return;
    }
    o.append("\\cvsection{SKILLS}\n").append(SECTION_TIGHTEN).append("\\begin{cvskills}\n");
    for (SkillCategory sc : cats) {
      String items =
          sc.items() == null
              ? ""
              : sc.items().stream().map(LatexResumeRenderer::tex).collect(Collectors.joining(", "));
      o.append("\\cvskill\n    {")
          .append(tex(nz(sc.title())))
          .append("}\n    {")
          .append(items)
          .append("}\n");
    }
    o.append("\\end{cvskills}\n");
  }

  private static void appendProjects(StringBuilder o, ResumeProfile p) {
    List<Project> rows = p.projects();
    if (rows == null || rows.isEmpty()) {
      return;
    }
    o.append("\\cvsection{PROJECTS}\n").append(SECTION_TIGHTEN).append("\\begin{cventries}\n");
    for (int i = 0; i < rows.size(); i++) {
      Project pr = rows.get(i);
      String title = tex(nz(pr.name())) + "\\textnormal{ | " + tex(nz(pr.stack())) + "}";
      List<String> items = normalizeLines(pr.bullets());
      o.append("\\cventry\n  {")
          .append(title)
          .append("}\n  {")
          .append(tex(nz(pr.period())))
          .append("}\n  ");
      String demoUrl = nz(pr.demoUrl()).trim();
      boolean hasDemo = !demoUrl.isBlank();
      if (items.isEmpty() && !hasDemo) {
        o.append("{}\n");
      } else {
        o.append("{\n    \\begin{cvitems}\n");
        if (hasDemo) {
          o.append("      \\item {Live demo: \\href{")
              .append(texUrl(demoUrl))
              .append("}{")
              .append(tex(demoUrl))
              .append("}}\n");
        }
        for (String b : items) {
          o.append("      \\item {").append(tex(b)).append("}\n");
        }
        o.append("    \\end{cvitems}\n  }\n");
      }
      o.append("\n");
//      o.append("\n");
      if (i < rows.size() - 1) {
        o.append("  ").append(ENTRY_GAP);
      }
    }
    o.append("\\end{cventries}\n");
  }

  private static List<String> normalizeLines(List<String> lines) {
    if (lines == null || lines.isEmpty()) {
      return new java.util.ArrayList<>();
    }
    List<String> out = new java.util.ArrayList<>();
    for (String line : lines) {
      String t = nz(line).trim();
      if (!t.isBlank()) {
        out.add(t);
      }
    }
    return out;
  }

  private static boolean hasRenderablePrior(PriorEducation pe) {
    if (pe == null) {
      return false;
    }
    return !nz(pe.school()).trim().isBlank()
        || !nz(pe.credential()).trim().isBlank()
        || !nz(pe.period()).trim().isBlank();
  }

  private static void appendCertifications(StringBuilder o, ResumeProfile p) {
    List<Certification> rows = p.certifications();
    if (rows == null || rows.isEmpty()) {
      return;
    }
    o.append("\\cvsection{Achievements \\& Certifications}\n")
        .append(SECTION_TIGHTEN)
        .append("\\begin{cventries}\n");
    for (int i = 0; i < rows.size(); i++) {
      Certification c = rows.get(i);
      String date = nz(c.date());
      if (date.isBlank()) {
        date = "—";
      }
      String line1 = certificationTitle(c);
      o.append("\\cventry\n    {")
          .append(line1)
          .append("}\n    {")
          .append(tex(date))
          .append("}\n    {}\n");
      if (i < rows.size() - 1) {
        o.append("    \\vspace{-0.35em}\n");
      }
    }
    o.append("\\end{cventries}\n");
  }

  private static NameParts splitName(String full) {
    if (full == null || full.isBlank()) {
      return new NameParts("", "");
    }
    String t = full.trim();
    int sp = t.indexOf(' ');
    if (sp < 0) {
      return new NameParts(t, "");
    }
    return new NameParts(t.substring(0, sp), t.substring(sp + 1).trim());
  }

  private static final class NameParts {
    private final String first;
    private final String last;

    NameParts(String first, String last) {
      this.first = first;
      this.last = last;
    }

    String first() {
      return first;
    }

    String last() {
      return last;
    }
  }

  private static String githubForHeader(ResumeProfile p) {
    if (p.contact() != null && !nz(p.contact().github()).isBlank()) {
      return p.contact().github().trim();
    }
    if (p.socialLinks() == null) {
      return "";
    }
    for (SocialLink sl : p.socialLinks()) {
      if (sl.url() != null && sl.url().toLowerCase(Locale.ROOT).contains("github.com")) {
        return sl.url().trim();
      }
    }
    return "";
  }

  private static String linkedinForHeader(ResumeProfile p) {
    if (p.contact() != null && !nz(p.contact().linkedin()).isBlank()) {
      return p.contact().linkedin().trim();
    }
    if (p.socialLinks() == null) {
      return "";
    }
    for (SocialLink sl : p.socialLinks()) {
      if (sl.url() != null && sl.url().toLowerCase(Locale.ROOT).contains("linkedin.com")) {
        return sl.url().trim();
      }
    }
    return "";
  }

  private static String extraSocialHeader(List<SocialLink> links, String ghUrl, String liUrl) {
    if (links == null || links.isEmpty()) {
      return "";
    }
    String ghKey = normalizeSocialKey(ghUrl);
    String liKey = normalizeSocialKey(liUrl);
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (SocialLink sl : links) {
      if (sl.url() == null || sl.url().isBlank()) {
        continue;
      }
      String u = sl.url().trim();
      String key = normalizeSocialKey(u);
      if (!ghKey.isEmpty() && key.equals(ghKey)) {
        continue;
      }
      if (!liKey.isEmpty() && key.equals(liKey)) {
        continue;
      }
      if (!first) {
        sb.append(" \\quad\\textbar\\quad ");
      }
      first = false;
      String href = u.matches("(?i)https?://.*") ? texUrl(u) : texUrl("https://" + u);
      sb.append("\\href{")
          .append(href)
          .append("}{")
          .append(tex(nz(sl.label())))
          .append("}");
    }
    return sb.toString();
  }

  private static String normalizeSocialKey(String url) {
    return stripScheme(nz(url)).toLowerCase(Locale.ROOT).replaceAll("/+$", "");
  }

  private static String stripScheme(String url) {
    String u = url.trim();
    if (u.regionMatches(true, 0, "https://", 0, 8)) {
      u = u.substring(8);
    } else if (u.regionMatches(true, 0, "http://", 0, 7)) {
      u = u.substring(7);
    }
    while (u.startsWith("/")) {
      u = u.substring(1);
    }
    return u;
  }

  private static String githubPathForRussell(String url) {
    String s = stripScheme(url);
    if (s.regionMatches(true, 0, "www.", 0, 4)) {
      s = s.substring(4);
    }
    return s;
  }

  private static String linkedinPathForRussell(String url) {
    String s = stripScheme(url);
    if (s.regionMatches(true, 0, "www.", 0, 4)) {
      s = s.substring(4);
    }
    return s;
  }

  private static String nz(String s) {
    return s == null ? "" : s;
  }

  private static String certificationTitle(Certification c) {
    String rawTitle = nz(c.title());
    String title = tex(rawTitle);
    String issuer = tex(nz(c.issuer()));
    String url = nz(c.url()).trim();
    if (!url.isBlank()) {
      title = "\\href{" + texUrl(url) + "}{" + title + "}";
    }
    return title + "\\textnormal{ | " + issuer + "}";
  }

  static String tex(String s) {
    if (s == null) {
      return "";
    }
    return s.replace("\\", "\\textbackslash{}")
        .replace("&", "\\&")
        .replace("%", "\\%")
        .replace("$", "\\$")
        .replace("#", "\\#")
        .replace("_", "\\_")
        .replace("{", "\\{")
        .replace("}", "\\}")
        .replace("~", "\\textasciitilde{}")
        .replace("^", "\\textasciicircum{}");
  }

  static String texUrl(String s) {
    if (s == null) {
      return "";
    }
    return s.replace("\\", "/")
        .replace("%", "\\%")
        .replace("#", "\\#")
        .replace("_", "\\_");
  }
}
