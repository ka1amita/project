package com.gfa.services;

import com.gfa.models.Role;
import com.gfa.models.User;
import com.gfa.repositories.RoleRepo;
import com.gfa.repositories.UserRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepo userRepo;
  private final RoleRepo roleRepo;

  @Autowired
  public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
  }

  @Override
  public User saveUser(User user) {
    return userRepo.save(user);
  }

  @Override
  public Role saveRole(Role role) {
    return roleRepo.save(role);
  }

  @Override
  public void addRoleToUser(String username, String roleName) {
    User user = userRepo.findByUsername(username);
    Role role = roleRepo.findByName(roleName);
    user.getRoles().add(role);
  }

  @Override
  public User getUser(String username) {
    return userRepo.findByUsername(username);
  }

  @Override
  public List<User> getUsers() {
    return userRepo.findAll();
  }
}
