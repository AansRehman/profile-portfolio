package com.aans.resume.web;

import com.aans.resume.config.ResumeBuilderProperties;
import com.aans.resume.service.ResumeBuildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@Slf4j
public class ResumeBuilderController {

  private final ResumeBuildService resumeBuildService;
  private final ResumeBuilderProperties properties;

  @PostMapping("/build")
  public ResponseEntity<byte[]> buildResumePdf() {
    long t0 = System.nanoTime();
    log.info("POST /api/resume/build invoked");
    byte[] pdf = resumeBuildService.buildPdf();
    log.info(
        "POST /api/resume/build done: {}bytes in {}ms (see prior lines for pipeline breakdown)",
        pdf.length,
        (System.nanoTime() - t0) / 1_000_000L);
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + properties.defaultOutputName() + ".pdf\"")
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdf);
  }
}
