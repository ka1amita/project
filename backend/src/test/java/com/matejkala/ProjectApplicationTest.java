package com.matejkala;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.matejkala.services.AppUserServiceImpl;
import com.matejkala.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ProjectApplicationTest {

  @Autowired
  ApplicationContext applicationContext;

  @Test
  void contextLoads() {
  }

  @Test
  void context_has_beans() {
    assertNotNull(applicationContext.getBean(AppUserServiceImpl.class));
    assertNotNull(applicationContext.getBean(UserDetailsServiceImpl.class));
  }

  @Test
  void main_runs_without_exceptions() {

    org.junit.jupiter.api.Assertions.assertDoesNotThrow(
        () -> {
          ProjectApplication.main(
              new String[] {"--spring.main.web-environment=false", "--server.port=8082"});
        });
  }
}