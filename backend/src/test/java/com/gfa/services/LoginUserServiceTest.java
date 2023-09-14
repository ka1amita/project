package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginUserServiceTest {

    @InjectMocks
    private LoginUserServiceImp appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void Login_Is_Successful() {
        LoginRequestDTO mockRequest = new LoginRequestDTO();
        mockRequest.setLoginInput("John Doe");
        mockRequest.setPassword("1234");

        AppUser mockUser = new AppUser();
        mockUser.setUsername("John Doe");
        mockUser.setPassword("1234");

        when(appUserRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(mockUser));

        LoginResponseDTO mockResponse = appUserService.userLogin(Optional.of(mockRequest));

        assertEquals("Demo token", mockResponse.getJwtToken());
    }

    @Test
    public void Login_Failed_Due_To_Missing_Body() {
        assertThrows(NullPointerException.class, () -> {
            appUserService.userLogin(Optional.empty());
        });
    }

    @Test
    public void Login_Failed_Due_To_Empty_Username_Or_Email() {
        LoginRequestDTO mockRequest = new LoginRequestDTO();
        mockRequest.setLoginInput("");
        mockRequest.setPassword("1234");

        assertThrows(IllegalArgumentException.class, () ->
                appUserService.userLogin(Optional.of(mockRequest)));
    }

    @Test
    public void Login_Failed_Due_To_Empty_Password() {
        LoginRequestDTO mockRequest = new LoginRequestDTO();
        mockRequest.setLoginInput("John Doe");
        mockRequest.setPassword("");

        assertThrows(IllegalArgumentException.class, () ->
                appUserService.userLogin(Optional.of(mockRequest)));
    }

    @Test
    public void Login_Failed_Due_To_Username_Not_Found() {
        LoginRequestDTO mockRequest = new LoginRequestDTO();
        mockRequest.setLoginInput("John Doe");
        mockRequest.setPassword("1234");

        when(appUserRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            appUserService.userLogin(Optional.of(mockRequest));
        });
    }

    @Test
    public void testUserLoginIncorrectPassword() {
        LoginRequestDTO mockRequest = new LoginRequestDTO();
        mockRequest.setLoginInput("John Doe");
        mockRequest.setPassword("12345");

        AppUser mockUser = new AppUser();
        mockUser.setUsername("John Doe");
        mockUser.setPassword("1234");

        when(appUserRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(mockUser));

        assertThrows(IllegalArgumentException.class, () -> {
            appUserService.userLogin(Optional.of(mockRequest));
        });
    }

}
