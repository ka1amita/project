package com.gfa.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.AppUserRepository;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceUnitTests {

    @Mock
    PasswordEncoder passwordEncoder; // necessary for the appUserService.encodePasswordAndSaveAppUser
    @Mock // necessary!
    AppUserRepository appUserRepository;
    @InjectMocks // necessary!
    AppUserServiceImpl appUserService;
    AppUser appUser;
    Long id;
    String username;
    String password;
    String email;
    Role role;
    ActivationCode code;
    @BeforeEach
    public void setup() {
        id = null;
        password = "password";
        username = "username";
        email = "email@gfa.com";
        appUser = new AppUser(id,
                              username,
                              password,
                              email,
                              new HashSet<>(),
                              new HashSet<>());
        role = new Role("User");
        code = new ActivationCode("code", appUser);
        appUser.getRoles()
               .add(role);
        appUser.getActivationCodes()
               .add(code);
    }
    @Test
    public void doesnt_find_user_neither_by_wrong_email_nor_name_and_throws_an_exception() {
        when(appUserRepository.findByUsernameOrEmail(any(String.class), any(String.class))).thenReturn(
            Optional.ofNullable(null));

        AppUser foundUserByUsername = null;
        AppUser foundUserByEmail = null;
        try {
            foundUserByUsername = appUserService.findByUsernameOrEmail("any");
        } catch (Exception expected) {
        }

        try {
            foundUserByEmail = appUserService.findByUsernameOrEmail("any");
        } catch (Exception expected) {
        }
        assertNull(foundUserByUsername);
        assertNull(foundUserByEmail);
        Exception exceptionFromName = assertThrows(UsernameNotFoundException.class,
                                                   () -> appUserService.findByUsernameOrEmail(
                                                       "wrong_name"));
        assertEquals("User not found in the DB", exceptionFromName.getMessage());

        Exception exceptionFromEmail = assertThrows(UsernameNotFoundException.class,
                                                    () -> appUserService.findByUsernameOrEmail(
                                                        "wrong_email"));
        assertEquals("User not found in the DB", exceptionFromEmail.getMessage());
    }

    @Test
    public void finds_user() {
        when(appUserRepository.findByUsernameOrEmail(any(String.class), any(String.class))).thenReturn(
            Optional.of(appUser));

        AppUser foundUser = appUserService.findByUsernameOrEmail("any");

        assertNotNull(foundUser);
    }

    @Test
    public void encodes_password_and_saves_user() {
        appUser.setId(1L);

        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);

        AppUser savedUser = appUserService.encodePasswordAndSaveAppUser(appUser);

        assertNotNull(appUser);
        assertNotNull(savedUser);
        assertSame(appUser, savedUser);
        assertEquals(1, savedUser.getId());
        assertEquals(appUser.getUsername(), savedUser.getUsername());
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(appUser.isActive(), savedUser.isActive());
        assertEquals(appUser.getEmail(), savedUser.getEmail());
        assertEquals(appUser.getAuthorities(), savedUser.getAuthorities());
        assertEquals(appUser.getRoles(), savedUser.getRoles());
        assertEquals(appUser.getActivationCodes(), savedUser.getActivationCodes());
    }
}
