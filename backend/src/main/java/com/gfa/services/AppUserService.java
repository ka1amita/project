package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.models.Role;

import java.util.List;
import java.util.Optional;

public interface AppUserService {

    LoginResponseDTO userLogin(Optional<LoginRequestDTO> loginRequestDto);
    AppUser saveAppUser(AppUser appUser);
    Role saveRole(Role role);
    void addRoleToAppUser(String username,String roleName);
    AppUser getAppUser(String username);
    List<AppUser> getAppUsers();
}
