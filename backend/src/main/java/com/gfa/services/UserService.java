package com.gfa.services;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import java.util.List;

public interface UserService {
  AppUser saveUser(AppUser user);

  Role saveRole(Role role);

  void addRoleToUser(String username, String roleName);

  AppUser getUser(String username);

  List<AppUser> getUsers();

  ActivationCode saveActivationCode(ActivationCode code);
}
