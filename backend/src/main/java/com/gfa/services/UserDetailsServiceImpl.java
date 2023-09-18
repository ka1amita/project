package com.gfa.services;

import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.AppUserRepository;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AppUserRepository appUserRepository;

  @Autowired
  public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = appUserRepository.findByUsernameOrEmail(username, username)
                                       .orElseThrow(() -> new UsernameNotFoundException(
                                           "User not found in the DB"));

    return new org.springframework.security.core.userdetails.User(
        appUser.getUsername(),
        appUser.getPassword(),
        appUser.isEnabled(),
        appUser.isAccountNonExpired(),
        appUser.isCredentialsNonExpired(),
        appUser.isAccountNonLocked(),
        appUser.getAuthorities());
  }
}