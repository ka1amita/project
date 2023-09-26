package com.gfa.dtos;

import com.gfa.dtos.requestdtos.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RequestDTOTest {

    @Test
    public void Testing_Valid_RegisterRequestDTO_Creation() {
        String username = "testUser";
        String email = "testuser@gmail.com";
        String password = "@TestPassword1234";

        RegisterRequestDTO request = new RegisterRequestDTO(username, email, password);

        assertNotNull(request);
        assertEquals(username, request.getUsername());
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
    }

    @Test
    public void Testing_Valid_UpdateAppUserDTO_Creation() {
        String username = "testUser";
        String email = "testuser@gmail.com";
        String password = "@TestPassword1234";

        RegisterRequestDTO request = new RegisterRequestDTO(username, email, password);

        assertNotNull(request);
        assertEquals(username, request.getUsername());
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
    }

    @Test
    public void Testing_Valid_PasswordResetWithCodeRequestDTO_Creation() {
        String password = "@TestPassword1234";

        PasswordResetWithCodeRequestDTO request = new PasswordResetWithCodeRequestDTO(password);

        assertNotNull(request);
        assertEquals(password, request.getPassword());
    }

    @Test
    public void Testing_Valid_PasswordResetRequestDTO_Creation() {
        String usernameOrEmail = "testUser";

        PasswordResetRequestDTO request = new PasswordResetRequestDTO(usernameOrEmail, usernameOrEmail);

        assertNotNull(request);
        assertEquals(usernameOrEmail, request.getUsernameOrEmail());
    }

    @Test
    public void Testing_Valid_IdRequestDTO_Creation() {
        Long id = 1L;

        IdRequestDTO request = new IdRequestDTO(id);

        assertNotNull(request);
        assertEquals(id, request.getId());
    }

    @Test
    public void Testing_Valid_EmailRequestDTO_Creation() {
        String email = "testuser@gmail.com";

        EmailRequestDTO request = new EmailRequestDTO(email);

        assertNotNull(request);
        assertEquals(email, request.getEmail());
    }

    @Test
    public void Testing_Valid_RequestTokenDTO_Creation() {
        String token = "demoToken";
        String issuer = "Issuer";

        RequestTokenDTO request = new RequestTokenDTO(token,issuer);

        assertNotNull(request);
        assertEquals(token, request.getToken());
        assertEquals(issuer, request.getIssuer());
    }
}
