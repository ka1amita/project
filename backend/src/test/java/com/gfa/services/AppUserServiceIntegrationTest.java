package com.gfa.services;

import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class AppUserServiceIntegrationTest {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ActivationCodeRepository activationCodeRepository;

    @MockBean
    private EmailService emailService;

    private AppUser existingUser;
    private RegisterRequestDTO request;

    @BeforeEach
    public void setup(){
        existingUser = new AppUser();
        request = new RegisterRequestDTO();
    }

    private void saveSampleUser(String username, String email, String password){
        existingUser.setUsername(username);
        existingUser.setEmail(email);
        existingUser.setPassword(password);
        appUserRepository.save(existingUser);
    }

    @Test
    public void registerUser_with_valid_input_saves_user() throws MessagingException {
        request.setUsername("testUser");
        request.setEmail("test@email.com");
        request.setPassword("Valid@1234");

        doNothing().when(emailService).registerConfirmationEmail(anyString(), anyString(), anyString());

        AppUser savedUser = appUserService.registerUser(request);

        Optional<AppUser> retrievedUser = appUserRepository.findByUsername("testUser");
        assertTrue(retrievedUser.isPresent());
        assertEquals("testUser", retrievedUser.get().getUsername());

        Optional<ActivationCode> retrievedCode = activationCodeRepository.findByAppUser(savedUser);
        assertTrue(retrievedCode.isPresent());
    }

    @Test
    public void registerUser_with_existing_username_throws_exception(){
        saveSampleUser("existingUser", "existing@email.com", "Valid@1234");
        request.setUsername("existingUser");
        assertThrows(UserAlreadyExistsException.class, () -> appUserService.registerUser(request));
    }

    @Test
    public void registerUser_with_existing_email_throws_exception() {
        saveSampleUser("existingUser", "existing@email.com", "Sample@1234");
        request.setEmail("existing@email.com");
        assertThrows(EmailAlreadyExistsException.class, () -> appUserService.registerUser(request));
    }

    @Test
    public void registerUser_with_null_or_empty_password_throwsException() {
        request.setPassword(null);
        assertThrows(IllegalArgumentException.class, () -> appUserService.registerUser(request));
    }

    @Test
    public void registerUser_with_invalid_password_format_throws_exception() {
        request.setPassword("invalidpassword");
        assertThrows(IllegalArgumentException.class, () -> appUserService.registerUser(request));
    }

    @Test
    public void integrationTest_activate_account_with_expired_code() throws MessagingException {
        request.setUsername("sampleUser");
        request.setEmail("sample@email.com");
        request.setPassword("Valid@1234");

        AppUser savedUser = appUserService.registerUser(request);

        Optional<ActivationCode> optionalActivationCode = activationCodeRepository.findByAppUser(savedUser);
        assertTrue(optionalActivationCode.isPresent());
        ActivationCode activationCode = optionalActivationCode.get();

        activationCode.setCreatedAt(LocalDateTime.now().minusDays(2));
        activationCodeRepository.save(activationCode);

        assertThrows(IllegalStateException.class, () -> appUserService.activateAccount(activationCode.getActivationCode()));
    }
}
