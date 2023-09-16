package com.gfa.services;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.PasswordResetResponseDTO;
import com.gfa.dtos.responsedtos.PasswordResetWithCodeResponseDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AppUserServiceTest {

    @Autowired
    private AppUserServiceImpl userService;

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private ActivationCodeRepository activationCodeRepository;

    @Test
    public void register_user_successful() throws MessagingException {
        String testUsername = "testUser";
        String testEmail = "testEmail@example.com";
        String testPassword = "S@ck4Dic";

        when(appUserRepository.existsByUsername(testUsername)).thenReturn(false);
        when(appUserRepository.existsByEmail(testEmail)).thenReturn(false);

        AppUser savedUser = new AppUser();
        savedUser.setUsername(testUsername);
        savedUser.setEmail(testEmail);
        savedUser.setPassword(testPassword);

        when(appUserRepository.save(any())).thenReturn(savedUser);

        RegisterRequestDTO request = new RegisterRequestDTO(testUsername, testEmail, testPassword);
        AppUser returnedUser = userService.registerUser(request);

        assertNotNull(returnedUser);
        assertEquals(testUsername, returnedUser.getUsername());
        assertEquals(testEmail, returnedUser.getEmail());
    }

    @Test
    public void register_user_null_username() {
        RegisterRequestDTO request = new RegisterRequestDTO(null, "testEmail@mail.com", "Valid@1234");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_existing_username() {
        when(appUserRepository.existsByUsername("testUser")).thenReturn(true);
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", "Valid@1234");
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_null_email() {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", null, "Valid@1234");
        Assertions.assertThrows(MethodArgumentNotValidException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_existing_email() {
        when(appUserRepository.existsByEmail("testEmail@example.com")).thenReturn(true);
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "Valid@1234");
        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_null_password() {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_invalid_password_pattern() {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", "invalid");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }
    @Test
    public void activateAccount_successful() {
        AppUser mockUser = new AppUser();
        mockUser.setActive(false);

        ActivationCode mockActivationCode = new ActivationCode();
        mockActivationCode.setActivationCode("validCode");
        mockActivationCode.setAppUser(mockUser);
        mockActivationCode.setCreatedAt(LocalDateTime.now().minusHours(10));

        when(activationCodeRepository.findByActivationCodeContains("validCode")).thenReturn(Optional.of(mockActivationCode));

        when(appUserRepository.save(any())).thenReturn(mockUser);

        userService.activateAccount("validCode");

        assertTrue(mockUser.isActive());

        verify(activationCodeRepository, times(1)).delete(mockActivationCode);
    }

    @Test
    public void activate_account_invalid_code() {
        when(activationCodeRepository.findByActivationCodeContains("invalidCode")).thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidActivationCodeException.class, () -> userService.activateAccount("invalidCode"));
    }


    @InjectMocks
    private AppUserServiceImpl appUserService;
    private AppUser appUser = new AppUser("Will Doe", "1234", "example2@mail.com", new ArrayList<>());
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

        assertEquals(illegalArgumentException.getMessage(), "You have to provide either the username or the email field!");
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

        assertEquals(illegalArgumentException.getMessage(), "You have to provide either the username or the email field!");
    }


    @Test
    void Password_Reset_With_Code_Is_Successful_With_Code_And_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("1234");
        when(activationCodeRepository.findByActivationCodeContains(anyString())).thenReturn(Optional.of(activationCode));
        PasswordResetWithCodeResponseDTO passwordResetWithCodeResponseDTO = (PasswordResetWithCodeResponseDTO) appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd").getBody();

        assert passwordResetWithCodeResponseDTO != null;
        assert passwordResetWithCodeResponseDTO.getMessage() != null;
        assertEquals(passwordResetWithCodeResponseDTO.getMessage(), "success");
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_Code_And_No_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("");
        when(activationCodeRepository.findByActivationCodeContains(anyString())).thenReturn(Optional.of(activationCode));
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd").getBody();
        });

        assertEquals(illegalArgumentException.getMessage(), "Password can't be empty!");
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_No_Code_And_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("1234");
        when(activationCodeRepository.findByActivationCodeContains(anyString())).thenReturn(Optional.empty());
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "").getBody();
        });

        assertEquals(illegalArgumentException.getMessage(), "Reset code doesn't exist!");
    }

    @Test
    void Password_Reset_With_Code_Is_Failed_With_No_Code_And_No_Password() {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("");
        when(activationCodeRepository.findByActivationCodeContains(anyString())).thenReturn(Optional.empty());
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            appUserService.resetWithCode(passwordResetWithCodeRequestDTO, "").getBody();
        });

        assertEquals(illegalArgumentException.getMessage(), "Reset code doesn't exist!");
    }
}