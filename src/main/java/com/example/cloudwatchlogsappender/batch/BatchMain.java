package com.example.cloudwatchlogsappender.batch;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class BatchMain implements CommandLineRunner {
  @Override
  public void run(String... args) {
    log.info("Cloud Watch Logs Appender Application Start");
    log.warn("Cloud Watch Logs Appender Application End");
  }
}
