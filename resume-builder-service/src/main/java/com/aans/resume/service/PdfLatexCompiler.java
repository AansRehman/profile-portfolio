package com.aans.resume.service;

import com.aans.resume.config.ResumeBuilderProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfLatexCompiler {

  private static final int SLOW_LATEX_ENGINE_WARN_MS = 8_000;

  private final ResumeBuilderProperties properties;

  public byte[] compile(String latexSource) {
    Path dir = null;
    try {
      dir = Files.createTempDirectory("resume-latex-");
      RussellTemplateSupport.copyRussellClass(dir);
      Path tex = dir.resolve("resume.tex");
      Path logFile = dir.resolve("latex-engine-output.log");
      Files.writeString(tex, latexSource, StandardCharsets.UTF_8);

      List<String> command = latexProcessCommand(dir, tex);
      log.info(
          "Starting LaTeX engine (workDir={}, texBytes={}, russell.cls copied)",
          dir.toAbsolutePath(),
          latexSource.length());
      log.debug("LaTeX command: {}", command);

      ProcessBuilder pb = new ProcessBuilder(command);
      // Never leave stdout/stderr on PIPE without consuming: buffer can fill and block pdflatex.
      pb.redirectErrorStream(true);
      pb.redirectOutput(logFile.toFile());

      long tRun0 = System.nanoTime();
      Process p = pb.start();
      int exit = p.waitFor();
      long latexEngineMs = (System.nanoTime() - tRun0) / 1_000_000L;

      long logBytes = Files.exists(logFile) ? Files.size(logFile) : 0;
      log.info(
          "LaTeX engine finished exit={} in {}ms (logFile={} bytes, path={})",
          exit,
          latexEngineMs,
          logBytes,
          logFile.toAbsolutePath());

      if (latexEngineMs >= SLOW_LATEX_ENGINE_WARN_MS) {
        log.warn(
            "LaTeX took {}ms — MiKTeX first-run/auto-install fonts (roboto/fontawesome5/sourcesanspro), antivirus on temp dirs, or missing xelatex in PATH."
                + " Inspect {}.",
            latexEngineMs,
            logFile.toAbsolutePath());
      }

      if (exit != 0) {
        String logs =
            Files.exists(logFile)
                ? Files.readString(logFile, StandardCharsets.UTF_8)
                : "(no log file)";
        log.error(
            "LaTeX stderr/stdout tail (exit {}):\n{}",
            exit,
            tail(logs, 12_000));
        throw new IllegalStateException("LaTeX engine failed: " + tail(logs, 4000));
      }

      Path pdfPath = dir.resolve("resume.pdf");
      if (!Files.exists(pdfPath)) {
        log.error("LaTeX reported success but {} is missing. See {}", pdfPath, logFile);
        throw new IllegalStateException(
            "LaTeX produced no resume.pdf — ensure XeLaTeX is configured (latex-command): " + logFile);
      }
      byte[] pdf = Files.readAllBytes(pdfPath);
      log.debug("Read PDF {} bytes from {}", pdf.length, pdfPath.toAbsolutePath());
      return pdf;
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("LaTeX engine was interrupted.", ex);
    } catch (IOException ex) {
      String hint =
          isWindows() && isBareCommand(properties.effectiveLatexCompiler())
              ? " Install MiKTeX (XeLaTeX), add bin to PATH, or set LATEX_COMMAND to the full path of xelatex.exe (CreateProcess error=2 = executable not found)."
              : "";
      throw new IllegalStateException("Could not build resume PDF: " + ex.getMessage() + hint, ex);
    } finally {
      if (dir != null) {
        try (var stream = Files.walk(dir)) {
          stream.sorted((a, b) -> b.getNameCount() - a.getNameCount()).forEach(this::safeDelete);
        } catch (IOException ignored) {
          // best-effort cleanup
        }
      }
    }
  }

  /**
   * On Windows, {@code ProcessBuilder} does not apply {@code PATHEXT} the way {@code cmd.exe} does,
   * so {@code pdflatex} may fail while {@code pdflatex.cmd} works from a terminal. Spawning through
   * {@code cmd /c} fixes that for bare command names; full paths are started directly.
   */
  private List<String> latexProcessCommand(Path workDir, Path texFile) {
    String exe = properties.effectiveLatexCompiler().trim();
    List<String> latexArgs =
        List.of(
            "-interaction=nonstopmode",
            "-halt-on-error",
            "-output-directory",
            workDir.toAbsolutePath().toString(),
            texFile.toAbsolutePath().toString());

    if (isWindows() && isBareCommand(exe)) {
      List<String> cmd = new ArrayList<>(3 + latexArgs.size());
      cmd.add("cmd.exe");
      cmd.add("/c");
      cmd.add(exe);
      cmd.addAll(latexArgs);
      return cmd;
    }

    List<String> cmd = new ArrayList<>(1 + latexArgs.size());
    cmd.add(exe);
    cmd.addAll(latexArgs);
    return cmd;
  }

  private static boolean isWindows() {
    return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");
  }

  /** True when the configured value is a single PATH-style name (not a filesystem path). */
  private static boolean isBareCommand(String exe) {
    return !exe.contains("\\") && !exe.contains("/");
  }

  private static String tail(String s, int maxChars) {
    if (s == null || s.length() <= maxChars) {
      return s == null ? "" : s;
    }
    return "…" + s.substring(s.length() - maxChars);
  }

  private void safeDelete(Path p) {
    try {
      Files.deleteIfExists(p);
    } catch (IOException ignored) {
      // best-effort cleanup
    }
  }
}
