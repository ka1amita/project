package com.gfa.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gfa.models.AppUser;
import com.gfa.repositories.AppUserRepository;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceUnitTests {

  @Mock // necessary!
  AppUserRepository appUserRepository;
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
        Optional.ofNullable(null));

    assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(
        "any"));
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