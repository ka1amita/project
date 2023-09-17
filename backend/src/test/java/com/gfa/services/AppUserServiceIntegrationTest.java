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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    public void registerUser_withValidInput_savesUser(){
        request.setUsername("testUser");
        request.setEmail("test@email.com");
        request.setPassword("Valid@1234");
        AppUser savedUser = appUserService.registerUser(request);

        Optional<AppUser> retrievedUser = appUserRepository.findByUsername("testUser");
        assertTrue(retrievedUser.isPresent());
        assertEquals("testUser", retrievedUser.get().getUsername());

        Optional<ActivationCode> retrievedCode = activationCodeRepository.findByAppUser(savedUser);
        assertTrue(retrievedCode.isPresent());
    }

    @Test
    public void registerUser_withExistingUsername_throwsException(){
        saveSampleUser("existingUser", "existing@email.com", "Valid@1234");
        request.setUsername("existingUser");
        assertThrows(UserAlreadyExistsException.class, () -> appUserService.registerUser(request));
    }

    @Test
    public void registerUser_withExistingEmail_throwsException() {
        saveSampleUser("existingUser", "existing@email.com", "Sample@1234");
        request.setEmail("existing@email.com");
        assertThrows(EmailAlreadyExistsException.class, () -> appUserService.registerUser(request));
    }

    @Test
    public void registerUser_withNullOrEmptyPassword_throwsException() {
        request.setPassword(null);
        assertThrows(IllegalArgumentException.class, () -> appUserService.registerUser(request));
    }

    @Test
    public void registerUser_withInvalidPasswordFormat_throwsException() {
        request.setPassword("invalidpassword");
        assertThrows(IllegalArgumentException.class, () -> appUserService.registerUser(request));
    }









}
