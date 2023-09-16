package com.gfa.services;

import com.gfa.models.AppUser;
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
    AppUser appUser = appUserRepository.findByUsername(username)
                                       .orElseThrow(() -> new UsernameNotFoundException(
                                           "User not found in the DB"));
    if (!appUser.isActive()) {
      throw new UsernameNotFoundException("User is not active");
    }

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    appUser.getRoles()
        .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    return new org.springframework.security.core.userdetails.User(
        appUser.getUsername(),
        appUser.getPassword(),
        authorities);
  }
}