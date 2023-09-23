package com.gfa.services;

import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.AppUserResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;

public interface AppUserService {

    AppUser registerUser(RegisterRequestDTO request) throws MessagingException;

    void activateAccount(String activationCode);

    ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO) throws MessagingException;

    ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, String resetCode);

    void addRoleToAppUser(AppUser appUser, String roleName);

    void addRoleToAppUser(String username, String roleName);

    AppUser encodePasswordAndSaveAppUser(AppUser user);

    AppUser findUserByUsername(String username);

    List<AppUser> getAllAppUsers();

    void removeAppUser(Long id);

    void setAppUserActive(AppUser user);

    AppUser findByUsernameOrEmail(String username);

    Page<AppUserResponseDTO> pageAppUserDtos(PageRequest pageRequest);
}