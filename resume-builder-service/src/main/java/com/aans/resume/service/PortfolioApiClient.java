package com.aans.resume.service;

import com.aans.resume.config.ResumeBuilderProperties;
import com.aans.resume.model.ResumeProfile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioApiClient {

  private final RestTemplate restTemplate;
  private final ResumeBuilderProperties properties;

  public ResumeProfile fetchResumeProfile() {
    String url = properties.portfolioApiBaseUrl() + "/api/resume/source";
    log.debug("GET resume source: {}", url);
    long t0 = System.nanoTime();
    ResumeProfile body = restTemplate.getForObject(url, ResumeProfile.class);
    long ms = (System.nanoTime() - t0) / 1_000_000L;
    if (body == null) {
      log.error("GET {} returned null body after {}ms — check portfolio-api", url, ms);
      throw new IllegalStateException("portfolio-api returned no JSON for resume source");
    }
    log.info("Fetched resume profile JSON in {}ms (name={})", ms, abbrev(body.name(), 48));
    log.debug(
        "Resume source counts: experience={} projects={} certs={} socialLinks={}",
        sized(body.experience()),
        sized(body.projects()),
        sized(body.certifications()),
        sized(body.socialLinks()));
    return body;
  }

  private static String abbrev(String s, int max) {
    if (s == null) return "";
    String t = s.trim();
    if (t.length() <= max) return t;
    return t.substring(0, max - 1) + "…";
  }

  private static int sized(List<?> list) {
    return list == null ? 0 : list.size();
  }
}
