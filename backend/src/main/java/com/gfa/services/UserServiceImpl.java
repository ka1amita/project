package com.gfa.services;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.ActivationCodeRepo;
import com.gfa.repositories.RoleRepo;
import com.gfa.repositories.UserRepo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepo userRepo;
  private final RoleRepo roleRepo;
  private final ActivationCodeRepo activationCodeRepo;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo,
                         ActivationCodeRepo activationCodeRepo,
                         @Lazy PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.activationCodeRepo = activationCodeRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public AppUser saveUser(AppUser user) {
    user.setPassword(passwordEncoder.encode(user.getUsername()));
    return userRepo.save(user);
  }

  @Override
  public Role saveRole(Role role) {
    return roleRepo.save(role);
  }

  @Override
  public void addRoleToUser(String username, String roleName) {
    AppUser user = userRepo.findByUsername(username);
    Role role = roleRepo.findByName(roleName);
    user.getRoles()
        .add(role);
  }

  @Override
  public AppUser getUser(String username) {
    return userRepo.findByUsername(username);
  }

  @Override
  public List<AppUser> getUsers() {
    return userRepo.findAll();
  }

  @Override
  public ActivationCode saveActivationCode(ActivationCode code) {
    return activationCodeRepo.save(code);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser user = userRepo.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found in the database");
    }
    if (!user.isActive()) {
      throw new UsernameNotFoundException("User is not active");
    }

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getRoles()
        .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        authorities);
  }
}
