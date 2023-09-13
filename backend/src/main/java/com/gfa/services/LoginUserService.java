package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;

import java.util.Optional;

public interface LoginUserService {

    LoginResponseDTO userLogin(Optional<LoginRequestDTO> loginRequestDto);
//    AppUser saveUser(AppUser appUser);
//    Role saveRole(Role role);
//    void addRoleToUser(String username,String roleName);
//    AppUser getUser(String username);
//    List<AppUser> getUsers();
}
