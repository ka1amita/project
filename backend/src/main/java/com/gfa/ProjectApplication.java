package com.gfa;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import com.gfa.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication()
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(UserService userService, ActivationCodeRepository activationCodeRepository) {
//        return args -> {
//            userService.saveRole(new Role("ROLE_USER"));
//            userService.saveRole(new Role("ROLE_MANAGER"));
//            userService.saveRole(new Role("ROLE_ADMIN"));
//            userService.saveRole(new Role("ROLE_SUPER_ADMIN"));
//
//            AppUser appUser1 = new AppUser("John Doe", "1234", "example1@mail.com", new ArrayList<>());
//            AppUser appUser2 = new AppUser("Will Doe", "1234", "example2@mail.com", new ArrayList<>());
//            AppUser appUser3 = new AppUser("Arnold Doe", "1234", "example3@mail.com", new ArrayList<>());
//            AppUser appUser4 = new AppUser("Jim Doe", "1234", "example4@mail.com", new ArrayList<>());
//
//            userService.saveUser(appUser1);
//            userService.saveUser(appUser2);
//            userService.saveUser(appUser3);
//            userService.saveUser(appUser4);
//
//            userService.addRoleToUser("John Doe", "ROLE_USER");
//            userService.addRoleToUser("Will Doe", "ROLE_MANAGER");
//            userService.addRoleToUser("Jim Doe", "ROLE_ADMIN");
//            userService.addRoleToUser("Arnold Doe", "ROLE_ADMIN");
//            userService.addRoleToUser("Arnold Doe", "ROLE_SUPER_ADMIN");
//            userService.addRoleToUser("Arnold Doe", "ROLE_MANAGER");
//
//            activationCodeRepository.save(new ActivationCode("ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd", appUser2));
//        };
//    }
}
