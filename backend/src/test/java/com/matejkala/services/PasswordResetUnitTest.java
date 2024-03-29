package com.matejkala.services;

import com.matejkala.dtos.requestdtos.PasswordResetRequestDTO;
import com.matejkala.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.matejkala.dtos.responsedtos.PasswordResetResponseDTO;
import com.matejkala.dtos.responsedtos.PasswordResetWithCodeResponseDTO;
import com.matejkala.exceptions.activation.InvalidActivationCodeException;
import com.matejkala.exceptions.user.InvalidPasswordFormatException;
import com.matejkala.exceptions.user.UserNotFoundException;
import com.matejkala.models.ActivationCode;
import com.matejkala.models.AppUser;
import com.matejkala.repositories.AppUserRepository;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PasswordResetUnitTest {
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private ActivationCodeServiceImp activationCodeServiceImp;
    @Mock
    private EmailServiceImpl emailService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private AppUserServiceImpl appUserService;

    private AppUser appUser = new AppUser("Will Doe", "newPassword1234@", "example2@mail.com", new HashSet<>());
    private ActivationCode activationCode = new ActivationCode("ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd", appUser);

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Password_Reset_Request_Is_Successful_With_Username() throws MessagingException {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("Will Doe");
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.of(appUser));
        when(activationCodeServiceImp.saveActivationCode(any())).thenReturn(activationCode);
        PasswordResetResponseDTO passwordResetResponseDTO = (PasswordResetResponseDTO) appUserService.reset(passwordResetRequestDTO).getBody();

        assertNotNull(passwordResetResponseDTO);
        assertNotNull(passwordResetResponseDTO.getUniqueResetCode());
        assertEquals(48, passwordResetResponseDTO.getUniqueResetCode().length());
    }

    @Test
    void Password_Reset_Request_Is_Successful_With_Email() throws MessagingException {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("example2@mail.com");
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.of(appUser));
        when(activationCodeServiceImp.saveActivationCode(any())).thenReturn(activationCode);
        PasswordResetResponseDTO passwordResetResponseDTO = (PasswordResetResponseDTO) appUserService.reset(passwordResetRequestDTO).getBody();

        assertNotNull(passwordResetResponseDTO);
        assertNotNull(passwordResetResponseDTO.getUniqueResetCode());
        assertEquals(48, passwordResetResponseDTO.getUniqueResetCode().length());
    }

    @Test
    void Password_Reset_Request_Is_Failed_With_Empty_Parameters() {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("");
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            appUserService.reset(passwordResetRequestDTO).getBody();
        });
    }

    @Test
    void Password_Reset_Request_Is_Failed_With_Null_Parameters() {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO(null);
        when(appUserRepository.findByEmailAndUsername(anyString(), anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            appUserService.reset(passwordResetRequestDTO).getBody();
        });
    }

    @Test
    void Password_Reset_With_Code_Is_Successful_With_Code_And_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("newPassword1234@");
        when(activationCodeServiceImp.findByActivationCodeContains(anyString())).thenReturn(Optional.of(activationCode));
        PasswordResetWithCodeResponseDTO passwordResetWithCodeResponseDTO = (PasswordResetWithCodeResponseDTO) appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd").getBody();

        assert passwordResetWithCodeResponseDTO != null;
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_Code_And_No_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("");
        when(activationCodeServiceImp.findByActivationCodeContains(anyString())).thenReturn(Optional.of(activationCode));

        assertThrows(InvalidPasswordFormatException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd").getBody();
        });
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_No_Code_And_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("newPassword1234@");
        when(activationCodeServiceImp.findByActivationCodeContains(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidActivationCodeException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "").getBody();
        });
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_No_Code_And_No_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("");
        when(activationCodeServiceImp.findByActivationCodeContains(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidActivationCodeException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "").getBody();
        });
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_Code_And_Invalid_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("1234");
        when(activationCodeServiceImp.findByActivationCodeContains(anyString())).thenReturn(Optional.of(activationCode));

        assertThrows(InvalidPasswordFormatException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "").getBody();
        });
    }
}
