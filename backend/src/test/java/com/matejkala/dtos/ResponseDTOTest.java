package com.matejkala.dtos;

import com.matejkala.dtos.responsedtos.*;
import com.matejkala.models.AppUser;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseDTOTest {

    @Test
    public void Testing_Valid_RegisterResponseDTO_Creation() {
        String message = "testUser";

        RegisterResponseDTO request = new RegisterResponseDTO(message);

        assertNotNull(request);
        assertEquals(message, request.getMessage());
    }

    @Test
    public void Testing_Valid_PasswordResetWithCodeResponseDTO_Creation() {
        String message = "testUser";

        PasswordResetWithCodeResponseDTO request = new PasswordResetWithCodeResponseDTO(message);

        assertNotNull(request);
        assertEquals(message, request.getMessage());
    }

    @Test
    public void Testing_Valid_PasswordResetResponseDTO_Creation() {
        String uniqueResetCode = "testUser";

        PasswordResetResponseDTO request = new PasswordResetResponseDTO(uniqueResetCode);

        assertNotNull(request);
        assertEquals(uniqueResetCode, request.getUniqueResetCode());
    }

    @Test
    public void Testing_Valid_ErrorResponseDTO_Creation() {
        String errorMessage = "testUser";

        ErrorResponseDTO request = new ErrorResponseDTO(errorMessage);

        assertNotNull(request);
        assertEquals(errorMessage, request.getErrorMessage());
    }

    @Test
    public void Testing_Valid_AppUserResponseDTO_Creation() {
        Long id = 1L;
        String username = "testUser";
        String email = "testemail@gmail.com";
        LocalDateTime verifiedAt = LocalDateTime.now();

        AppUser appUser = new AppUser(username,"testPass", email);
        appUser.setId(id);
        appUser.setVerifiedAt(verifiedAt);

        AppUserResponseDTO request = new AppUserResponseDTO(appUser);

        assertNotNull(request);
        assertEquals(id, request.getId());
        assertEquals(username, request.getUsername());
        assertEquals(email, request.getEmail());
        assertEquals(verifiedAt, request.getVerifiedAt());
    }

    @Test
    public void Testing_Valid_ResponseTokensDTO_Creation() {
        String accessToken = "demoToken";
        String responseToken = "demoToken";

        ResponseTokensDTO request = new ResponseTokensDTO(accessToken,responseToken);

        assertNotNull(request);
        assertEquals(accessToken, request.getAccessToken());
        assertEquals(responseToken, request.getRefreshToken());
    }

    @Test
    public void Testing_Valid_RefreshResponseTokenDTO_Creation() {
        String refreshToken = "demoToken";


        RefreshResponseTokenDTO request = new RefreshResponseTokenDTO(refreshToken);

        assertNotNull(request);
        assertEquals(refreshToken, request.getRefreshToken());
    }


    @Test
    public void Testing_Valid_MainHelloMessageDTO_Creation() {
        String message = "testUser";

        MainHelloMessageDTO request = new MainHelloMessageDTO(message);

        assertNotNull(request);
        assertEquals(message, request.getMessage());
    }
}
