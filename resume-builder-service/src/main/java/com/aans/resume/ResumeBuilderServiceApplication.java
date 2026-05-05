package com.aans.resume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ResumeBuilderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResumeBuilderServiceApplication.class, args);
  }
}
