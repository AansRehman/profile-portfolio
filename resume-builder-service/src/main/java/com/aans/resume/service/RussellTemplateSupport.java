package com.aans.resume.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Bundled {@code russell.cls} (same as Resume_Template__Anas_Rehman hierarchy). */
final class RussellTemplateSupport {

  static final String RESOURCE = "/resume-template/russell.cls";

  private RussellTemplateSupport() {}

  static void copyRussellClass(Path targetDirectory) throws IOException {
    Path out = targetDirectory.resolve("russell.cls");
    try (InputStream in = RussellTemplateSupport.class.getResourceAsStream(RESOURCE)) {
      if (in == null) {
        throw new IOException("Missing classpath resource " + RESOURCE);
      }
      Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
    }
  }
}
