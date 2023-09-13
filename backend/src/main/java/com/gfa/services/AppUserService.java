package com.gfa.services;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface AppUserService {
    ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO);

    ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, String resetCode);
}
