package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import java.util.List;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AppUserService {
    ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO);

    ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, String resetCode);

    LoginResponseDTO userLogin(Optional<LoginRequestDTO> loginRequestDto);

    void addRoleToAppUser(AppUser appUser, String roleName);

    void addRoleToAppUser(String username, String roleName);

    Role saveRole(Role role);

    AppUser saveUser(AppUser user);

    ActivationCode saveActivationCode(ActivationCode code);

    AppUser getAppUser(String username);

    List<AppUser> getAllAppUsers();

    void setAppUserActive(AppUser user);
}
