package com.aans.resume.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Config for resume-builder. Prefer {@code latex-command} (XeLaTeX/LuaLaTeX); {@code pdflatex-command} is legacy. */
@ConfigurationProperties(prefix = "resume-builder")
public record ResumeBuilderProperties(
    String portfolioApiBaseUrl,
    String latexCommand,
    String pdflatexCommand,
    String defaultOutputName) {

  public String effectiveLatexCompiler() {
    if (latexCommand != null && !latexCommand.isBlank()) {
      return latexCommand.trim();
    }
    if (pdflatexCommand != null && !pdflatexCommand.isBlank()) {
      return pdflatexCommand.trim();
    }
    return "xelatex";
  }
}
