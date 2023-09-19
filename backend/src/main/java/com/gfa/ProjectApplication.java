package com.gfa;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.services.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication()
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
    @Bean
    CommandLineRunner run(AppUserService appUserService) {
        return args -> {
            Role roleUser = new Role("ROLE_USER");
            AppUser user = new AppUser("user", "user", "user@gfa.com", new ArrayList<>());
            user.setActive(true);
            user.getRoles().add(roleUser);
            ActivationCode code = new ActivationCode("code", user);
            user.getActivationCodes().add(code);
            appUserService.saveUser(user);
        };
    }
}
