package com.gfa.services;

import com.gfa.dtos.responsedtos.RegisterRequestDTO;
import com.gfa.models.AppUser;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface AppUserService {

    AppUser registerUser(RegisterRequestDTO request);

    void activateAccount (String activationCode);

    ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO);

    ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, String resetCode);

    LoginResponseDTO userLogin(Optional<LoginRequestDTO> loginRequestDto);

}
