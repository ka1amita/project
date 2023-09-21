package com.gfa.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.AppUserRepository;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppUserServiceAndAppUserRepoTests {

    @Autowired
    AppUserRepository appUserRepository;
    Long id = 9L;
    String password = "password";
    String encodedPassword = new BCryptPasswordEncoder().encode(password);
    Role role = new Role("role");
    String username = "username";
    String email = "email@gfa.com";
    AppUser appUser;
    ActivationCode code;


    @BeforeEach
    public void setup() {
        appUser = new AppUser(id,
                              username,
                              password,
                              email,
                              new HashSet<>(),
                              new HashSet<>());
        code = new ActivationCode("code", appUser);
        appUser.getRoles()
               .add(role);
        appUser.getActivationCodes()
               .add(code);
    }

    @Test
    public void repository_saves_correctly() {
        AppUser savedUser = appUserRepository.save(appUser);
        assertEquals(appUser, savedUser);
        assertEquals(appUser.getId(), savedUser.getId());
        assertEquals(appUser.getUsername(), savedUser.getUsername());
        assertEquals(appUser.getPassword(), savedUser.getPassword());
        assertEquals(appUser.isActive(), savedUser.isActive());
        assertEquals(appUser.getEmail(), savedUser.getEmail());
        assertEquals(appUser.getAuthorities(), savedUser.getAuthorities());
        assertEquals(appUser.getRoles(), savedUser.getRoles());
        assertEquals(appUser.getActivationCodes(), savedUser.getActivationCodes());

    }

    @Test
    public void finds_user_by_email() {
    }
}
