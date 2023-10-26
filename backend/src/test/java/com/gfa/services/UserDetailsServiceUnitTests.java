package com.gfa.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gfa.models.AppUser;
import com.gfa.repositories.AppUserRepository;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceUnitTests {

  @Mock // necessary!
  AppUserRepository appUserRepository;

  @Mock
  MessageSource messageSource;

  @InjectMocks // necessary!
  UserDetailsServiceImpl userDetailsService;

  private AppUser appUser = new AppUser(null,
                                        "username",
                                        new BCryptPasswordEncoder().encode("password"),
                                        "email",
                                        new HashSet<>(),
                                        new HashSet<>());
  // not necessary necessary any more with @ExtendWith(MockitoExtension.class)!
  // @BeforeEanch
  // static void setup() {
  //   MockitoAnnotations.openMocks(this);
  // }

  @Test
  public void check_setup() {
    assertNotNull(appUserRepository);
    assertNotNull(userDetailsService);
  }
  @Test
  public void matching_user_details_are_returned_when_finds_a_user() {

    when(appUserRepository.findByUsernameOrEmail(any(String.class), any(String.class))).thenReturn(
        Optional.of(appUser));

    assertEquals(appUser.getUsername(), userDetailsService.loadUserByUsername("any")
                                                          .getUsername());
    assertEquals(appUser.getPassword(), userDetailsService.loadUserByUsername("any")
                                                          .getPassword());
    assertEquals(appUser.getAuthorities(), userDetailsService.loadUserByUsername("any")
                                                             .getAuthorities());
    assertEquals(appUser.isEnabled(), userDetailsService.loadUserByUsername("any")
                                                             .isEnabled());
    appUser.setActive(true);

    assertEquals(appUser.isEnabled(), userDetailsService.loadUserByUsername("any")
                                                        .isEnabled());
  }

  @Test
  public void throws_exception_if_doesnt_find_any_user() {
    when(appUserRepository.findByUsernameOrEmail(any(String.class), any(String.class))).thenReturn(
        Optional.empty());
    // wasn't passing because Junit 4 use: import static org.junit.Assert.assertThrows;
    assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(
        "any"));
    when(messageSource.getMessage(any(String.class), any(), any(
        Locale.class))).thenReturn("User not found in the DB");

    String message = null;
    try {
      userDetailsService.loadUserByUsername(
          "any");
    } catch (Exception ex) {
      message = ex.getMessage();
    }
    assertEquals("User not found in the DB", message);
  }
}