package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDto;
import com.gfa.dtos.responsedtos.LoginResponseDto;
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

public class AppUserServiceTest {

    @InjectMocks
    private AppUserServiceImp appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void Login_Is_Successful() {
        LoginRequestDto mockRequest = new LoginRequestDto();
        mockRequest.setLoginInput("John Doe");
        mockRequest.setPassword("1234");

        AppUser mockUser = new AppUser();
        mockUser.setUsername("John Doe");
        mockUser.setPassword("1234");

        when(appUserRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(mockUser));

        LoginResponseDto mockResponse = appUserService.userLogin(Optional.of(mockRequest));

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
        LoginRequestDto mockRequest = new LoginRequestDto();
        mockRequest.setLoginInput("");
        mockRequest.setPassword("1234");

        assertThrows(IllegalArgumentException.class, () ->
                appUserService.userLogin(Optional.of(mockRequest)));
    }

    @Test
    public void Login_Failed_Due_To_Empty_Password() {
        LoginRequestDto mockRequest = new LoginRequestDto();
        mockRequest.setLoginInput("John Doe");
        mockRequest.setPassword("");

        assertThrows(IllegalArgumentException.class, () ->
                appUserService.userLogin(Optional.of(mockRequest)));
    }

    @Test
    public void Login_Failed_Due_To_Username_Not_Found() {
        LoginRequestDto mockRequest = new LoginRequestDto();
        mockRequest.setLoginInput("John Doe");
        mockRequest.setPassword("1234");

        when(appUserRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            appUserService.userLogin(Optional.of(mockRequest));
        });
    }

    @Test
    public void testUserLoginIncorrectPassword() {
        LoginRequestDto mockRequest = new LoginRequestDto();
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
