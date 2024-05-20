package com.example.cloudwatchlogsappender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CloudwatchlogsAppenderApplication {

  public static void main(String[] args) {
    SpringApplication.run(CloudwatchlogsAppenderApplication.class, args);
  }
}
