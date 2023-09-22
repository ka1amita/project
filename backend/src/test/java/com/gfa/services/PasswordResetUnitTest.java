package com.gfa.services;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.responsedtos.PasswordResetResponseDTO;
import com.gfa.dtos.responsedtos.PasswordResetWithCodeResponseDTO;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class PasswordResetUnitTest {
    @Mock
    private ActivationCodeRepository activationCodeRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserServiceImpl appUserService;
    @Mock
    private EmailServiceImpl emailService;

    private AppUser appUser = new AppUser("Will Doe", "1234", "example2@mail.com", new HashSet<>());
    private ActivationCode activationCode = new ActivationCode("ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd", appUser);

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Password_Reset_Request_Is_Successful_With_Username() throws MessagingException {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("Will Doe", "");
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.of(appUser));
        when(activationCodeRepository.save(any())).thenReturn(activationCode);
        PasswordResetResponseDTO passwordResetResponseDTO = (PasswordResetResponseDTO) appUserService.reset(passwordResetRequestDTO).getBody();

        assert passwordResetResponseDTO != null;
        assert passwordResetResponseDTO.getUniqueResetCode() != null;
        assert passwordResetResponseDTO.getUniqueResetCode().length() == 48;
    }

    @Test
    void Password_Reset_Request_Is_Successful_With_Email() throws MessagingException {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("", "example2@mail.com");
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.of(appUser));
        when(activationCodeRepository.save(any())).thenReturn(activationCode);
        PasswordResetResponseDTO passwordResetResponseDTO = (PasswordResetResponseDTO) appUserService.reset(passwordResetRequestDTO).getBody();

        assert passwordResetResponseDTO != null;
        assert passwordResetResponseDTO.getUniqueResetCode() != null;
        assert passwordResetResponseDTO.getUniqueResetCode().length() == 48;
    }

    @Test
    void Password_Reset_Request_Is_Successful_With_Username_And_Email() throws MessagingException {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("Will Doe", "example2@mail.com");
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.of(appUser));
        when(activationCodeRepository.save(any())).thenReturn(activationCode);
        PasswordResetResponseDTO passwordResetResponseDTO = (PasswordResetResponseDTO) appUserService.reset(passwordResetRequestDTO).getBody();

        assert passwordResetResponseDTO != null;
        assert passwordResetResponseDTO.getUniqueResetCode() != null;
        assert passwordResetResponseDTO.getUniqueResetCode().length() == 48;
    }

    @Test
    void Password_Reset_Request_Is_Failed_With_Empty_Parameters() {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("", "");
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            appUserService.reset(passwordResetRequestDTO).getBody();
        });

        assertEquals(illegalArgumentException.getMessage(), "User not found!");
    }

    @Test
    void Password_Reset_Request_Is_Failed_With_Null_Parameters() {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO(null, null);
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            appUserService.reset(passwordResetRequestDTO).getBody();
        });

        assertEquals(illegalArgumentException.getMessage(), "User not found!");
    }


    @Test
    void Password_Reset_With_Code_Is_Successful_With_Code_And_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("1234");
        when(activationCodeRepository.findByActivationCode(anyString())).thenReturn(Optional.of(activationCode));
        PasswordResetWithCodeResponseDTO passwordResetWithCodeResponseDTO = (PasswordResetWithCodeResponseDTO) appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd").getBody();

        assert passwordResetWithCodeResponseDTO != null;
        assert passwordResetWithCodeResponseDTO.getMessage() != null;
        assertEquals(passwordResetWithCodeResponseDTO.getMessage(), "success");
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_Code_And_No_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("");
        when(activationCodeRepository.findByActivationCode(anyString())).thenReturn(Optional.of(activationCode));
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd").getBody();
        });

        assertEquals(illegalArgumentException.getMessage(), "Password can't be empty!");
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_No_Code_And_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("1234");
        when(activationCodeRepository.findByActivationCode(anyString())).thenReturn(Optional.empty());
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "").getBody();
        });

        assertEquals(illegalArgumentException.getMessage(), "Reset code doesn't exist!");
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_No_Code_And_No_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("");
        when(activationCodeRepository.findByActivationCode(anyString())).thenReturn(Optional.empty());
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "").getBody();
        });

        assertEquals(illegalArgumentException.getMessage(), "Reset code doesn't exist!");
    }
}