package com.matejkala.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.matejkala.models.ActivationCode;
import com.matejkala.models.AppUser;
import com.matejkala.models.Role;
import com.matejkala.repositories.AppUserRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceUnitTests {
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock // necessary!
    AppUserRepository appUserRepository;
    @Mock // necessary for the appUserService.findByUsernameOrEmail()
    MessageSource messageSource;
    @InjectMocks // necessary!
    AppUserServiceImpl appUserService;

    AppUser appUser;
    Long id;
    String username;
    String password;
    String email;
    Role role;
    ActivationCode code;
    boolean active;
    boolean deleted;
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
        active = true;
        deleted = false;
        appUser.setActive(active);
        appUser.setDeleted(deleted);
    }
    @Test
    public void doesnt_find_user_neither_by_wrong_email_nor_name_and_throws_an_exception() {
        when(appUserRepository.findByUsernameOrEmail(any(String.class), any(String.class))).thenReturn(
            Optional.empty());

        AppUser foundUserByUsername = null;
        AppUser foundUserByEmail = null;
        try {
            foundUserByUsername = appUserService.findByUsernameOrEmail("wrong_name");
        } catch (Exception expected) {
        }

        try {
            foundUserByEmail = appUserService.findByUsernameOrEmail("wrong_email");
        } catch (Exception expected) {
        }
        assertNull(foundUserByUsername);
        assertNull(foundUserByEmail);

        // requires @Mock MessageSource messageSource;
        Exception exceptionFromName = assertThrows(UsernameNotFoundException.class,
                                                   () -> appUserService.findByUsernameOrEmail(
                                                       "wrong_name"));

        Exception exceptionFromEmail = assertThrows(UsernameNotFoundException.class,
                                                    () -> appUserService.findByUsernameOrEmail(
                                                        "wrong_email"));
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
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn(new BCryptPasswordEncoder().encode(appUser.getPassword()));

        AppUser savedUser = appUserService.encodePasswordAndSaveAppUser(appUser);

        assertNotNull(appUser);
        assertNotNull(savedUser);
        assertSame(appUser, savedUser);
        assertNull(savedUser.getId());
        assertNotNull(savedUser.getPassword());
        assertEquals(username, savedUser.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches(password, savedUser.getPassword()));
        assertEquals(active, savedUser.isActive());
        assertEquals(email, savedUser.getEmail());
        assertEquals(new HashSet(Arrays.asList(role)), savedUser.getAuthorities());
        assertEquals(new HashSet(Arrays.asList(role)), savedUser.getRoles());
        assertEquals(new HashSet(Arrays.asList(code)), savedUser.getActivationCodes());
    }
}
