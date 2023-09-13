package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.models.Role;

import java.util.List;
import java.util.Optional;

public interface AppUserService {

    LoginResponseDTO userLogin(Optional<LoginRequestDTO> loginRequestDto);
//    AppUser saveUser(AppUser appUser);
//    Role saveRole(Role role);
//    void addRoleToUser(String username,String roleName);
//    AppUser getUser(String username);
//    List<AppUser> getUsers();
}
