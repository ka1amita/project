package com.gfa.services;

import com.gfa.models.Role;
import com.gfa.models.User;
import java.util.List;

public interface UserService {
  User saveUser(User user);

  Role saveRole(Role role);

  void addRoleToUser(String username, String roleName);

  User getUser(String username);

  List<User> getUsers();
}
