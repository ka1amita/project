package com.gfa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectApplicationTest {

  @Test
  void contextLoads() {}

  @Test
  void main_runs_without_exceptions() {

    org.junit.jupiter.api.Assertions.assertDoesNotThrow(
        () -> {
          ProjectApplication.main(
              new String[] {"--spring.main.web-environment=false", "--server.port=8082"});
        });
  }
}