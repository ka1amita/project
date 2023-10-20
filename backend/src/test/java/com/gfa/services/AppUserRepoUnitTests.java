package com.gfa.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.AppUserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppUserRepoUnitTests {

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
        appUser.setDeleted(false);
        appUser.setActive(true);
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
        assertEquals(appUser.getUsername(), savedUser.getUsername());
        assertEquals(appUser.getPassword(), savedUser.getPassword());
        assertEquals(appUser.isActive(), savedUser.isActive());
        assertEquals(appUser.getEmail(), savedUser.getEmail());
        assertEquals(appUser.getAuthorities(), savedUser.getAuthorities());
        assertEquals(appUser.getRoles(), savedUser.getRoles());
        assertEquals(appUser.getActivationCodes(), savedUser.getActivationCodes());
    }

    @Test
    // resets the context including the repo otherwise non-deterministic id is obtained
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void repository_saves_users_with_auto_incrementing_id() {
        AppUser savedUser = appUserRepository.save(appUser);

        assertEquals(1, savedUser.getId());

        AppUser differentAppUser =
            new AppUser(id, "different" + username, password, "different" + email, new HashSet<>(),
                        new HashSet<>());
        ActivationCode code1 = new ActivationCode("differentCode", differentAppUser);
        Role role1 = new Role("role");
        differentAppUser.getRoles()
                        .add(role1);
        differentAppUser.getActivationCodes()
                        .add(code1);
        differentAppUser.setDeleted(false);
        differentAppUser.setActive(true);

        AppUser savedUser1 = appUserRepository.save(differentAppUser);
        assertEquals(2, savedUser1.getId());
    }

    @Test
    public void doesnt_find_user_by_wrong_email_and_wrong_name() {
        Optional<AppUser> userNotFound =
            appUserRepository.findByUsernameOrEmail("wrong_name", "wrong_email");

        assertThrows(NoSuchElementException.class, userNotFound::get);
    }

    @Test
    public void finds_user_by_name() {
        appUserRepository.save(appUser);

        Optional<AppUser> foundUser =
            appUserRepository.findByUsernameOrEmail(username, "wrong_email");

        assertNotNull(foundUser);
    }

    @Test
    public void finds_user_by_email() {
        appUserRepository.save(appUser);

        AppUser foundUser = appUserRepository.findByUsernameOrEmail("wrong_name", email)
                                             .get();
        assertNotNull(foundUser);
    }
}
