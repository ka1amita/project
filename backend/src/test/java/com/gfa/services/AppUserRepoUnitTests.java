package com.gfa.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.AppUserRepository;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppUserRepoUnitTests {
// TODO doesn't work - doesn't load the applicationContext
    @Autowired
    AppUserRepository appUserRepository;

    Long id = null;
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
    void injectedComponentsAreNotNull() {
        assertNotNull(appUserRepository);
    }

    @Test
    public void repository_is_empty() {
        List<AppUser> appUserList = appUserRepository.findAll();
        assertEquals(0, appUserList.size());
    }
    @Test
    public void repository_saves_and_returns_saved_user_correctly() {
        AppUser savedUser = appUserRepository.save(appUser);
        assertEquals(appUser, savedUser);
        assertEquals(1, savedUser.getId());
        assertEquals(appUser.getUsername(), savedUser.getUsername());
        assertEquals(appUser.getPassword(), savedUser.getPassword());
        assertEquals(appUser.isActive(), savedUser.isActive());
        assertEquals(appUser.getEmail(), savedUser.getEmail());
        assertEquals(appUser.getAuthorities(), savedUser.getAuthorities());
        assertEquals(appUser.getRoles(), savedUser.getRoles());
        assertEquals(appUser.getActivationCodes(), savedUser.getActivationCodes());
    }

    @Test
    public void doesnt_find_user_by_wrong_email_and_wrong_name() {
        AppUser userNotFound = appUserRepository.findByUsernameOrEmail("wrong_name", "wrong_email")
                                                .get();

        assertNull(userNotFound);
    }

    @Test
    public void finds_user_by_name() {
        AppUser foundUser = appUserRepository.findByUsernameOrEmail(username, "wrong_email")
                                                .get();
        assertNotNull(foundUser);
    }
    @Test
    public void finds_user_by_email() {
        AppUser foundUser = appUserRepository.findByUsernameOrEmail(username, "wrong_email")
                                                .get();
        assertNotNull(foundUser);
    }

}
