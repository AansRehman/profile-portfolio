package com.aans.resume.service;

import com.aans.resume.model.ResumeProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeBuildService {

  private final PortfolioApiClient portfolioApiClient;
  private final LatexResumeRenderer latexResumeRenderer;
  private final PdfLatexCompiler pdfLatexCompiler;

  public byte[] buildPdf() {
    long t0 = System.nanoTime();
    ResumeProfile profile = portfolioApiClient.fetchResumeProfile();
    long t1 = System.nanoTime();
    String latex = latexResumeRenderer.render(profile);
    long t2 = System.nanoTime();
    byte[] pdf = pdfLatexCompiler.compile(latex);
    long t3 = System.nanoTime();

    log.info(
        "Resume PDF pipeline: fetch={}ms, latexRender={}ms, latexEngine={}ms, total={}ms, pdfSize={}bytes, latexChars={}",
        ms(t0, t1),
        ms(t1, t2),
        ms(t2, t3),
        ms(t0, t3),
        pdf.length,
        latex.length());
    return pdf;
  }

  private static long ms(long startNanos, long endNanos) {
    return (endNanos - startNanos) / 1_000_000L;
  }
}
