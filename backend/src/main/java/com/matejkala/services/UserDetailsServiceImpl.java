package com.matejkala.services;

import com.matejkala.models.AppUser;
import com.matejkala.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AppUserRepository appUserRepository;

  private final MessageSource messageSource;

  @Autowired
  public UserDetailsServiceImpl(AppUserRepository appUserRepository, MessageSource messageSource) {
    this.appUserRepository = appUserRepository;
    this.messageSource = messageSource;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = appUserRepository.findByUsernameOrEmail(username, username)
                                       .orElseThrow(() -> new UsernameNotFoundException(
                                           messageSource.getMessage("error.username.not.found", null, LocaleContextHolder.getLocale())));
    return new User(
        appUser.getUsername(),
        appUser.getPassword(),
        appUser.isEnabled(),
        appUser.isAccountNonExpired(),
        appUser.isCredentialsNonExpired(),
        appUser.isAccountNonLocked(),
        appUser.getAuthorities());
  }
}