package com.matejkala.services;

import com.matejkala.dtos.requestdtos.PasswordResetRequestDTO;
import com.matejkala.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.matejkala.dtos.requestdtos.RegisterRequestDTO;
import com.matejkala.dtos.requestdtos.UpdateAppUserDTO;
import com.matejkala.dtos.responsedtos.AppUserResponseDTO;
import com.matejkala.dtos.responsedtos.ResponseDTO;
import com.matejkala.models.AppUser;
import javax.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

public interface AppUserService {

    AppUser registerUser(RegisterRequestDTO request) throws MessagingException;

    void activateAccount(String activationCode);

    ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO) throws MessagingException;

    ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, String resetCode);

    void addRoleToAppUser(AppUser appUser, String roleName);

    void addRoleToAppUser(String username, String roleName);

    AppUser encodePasswordAndSaveAppUser(AppUser user);

    AppUser findUserByUsername(String username);

    AppUser fetchAppUserById(Long id);

    AppUserResponseDTO fetchUserApi(Long id);

    AppUserResponseDTO updateAppUserApi(Long id, UpdateAppUserDTO request) throws MessagingException;

    Page<AppUserResponseDTO> pageDeletedAppUserDtos(PageRequest request);

    Page<AppUserResponseDTO> pageAppUserDtos(PageRequest pageRequest);

    void removeAppUser(Long id);

    void setAppUserActive(AppUser user);

    @Query("SELECT u FROM AppUser u JOIN u.activationCodes ac WHERE ac.activationCode = :activationCode")
    Optional<AppUser> findAppUserByActivationCode(@Param("activationCode") String activationCode);


    AppUser findByUsernameOrEmail(String username);
}