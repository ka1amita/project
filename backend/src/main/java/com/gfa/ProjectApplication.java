package com.gfa;

import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.services.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AppUserService appUserService) {
        return args -> {
            appUserService.saveRole(new Role("ROLE_USER"));
            appUserService.saveRole(new Role("ROLE_MANAGER"));
            appUserService.saveRole(new Role("ROLE_ADMIN"));
            appUserService.saveRole(new Role("ROLE_SUPER_ADMIN"));

            appUserService.saveAppUser(new AppUser("John Doe", "1234", "example1@mail.com", new ArrayList<>()));
            appUserService.saveAppUser(new AppUser("Will Doe", "1234", "example2@mail.com", new ArrayList<>()));
            appUserService.saveAppUser(new AppUser("Arnold Doe", "1234", "example3@mail.com", new ArrayList<>()));
            appUserService.saveAppUser(new AppUser("Jim Doe", "1234", "example4@mail.com", new ArrayList<>()));

            appUserService.addRoleToAppUser("John Doe", "ROLE_USER");
            appUserService.addRoleToAppUser("Will Doe", "ROLE_MANAGER");
            appUserService.addRoleToAppUser("Jim Doe", "ROLE_ADMIN");
            appUserService.addRoleToAppUser("Arnold Doe", "ROLE_ADMIN");
            appUserService.addRoleToAppUser("Arnold Doe", "ROLE_SUPER_ADMIN");
            appUserService.addRoleToAppUser("Arnold Doe", "ROLE_MANAGER");
        };
    }
}
