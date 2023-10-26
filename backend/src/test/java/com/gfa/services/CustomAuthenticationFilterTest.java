package com.gfa.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.filters.CustomAuthenticationFilter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomAuthenticationFilterTest {

    // @Mock
    // private TokenServiceImpl tokenService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private CustomAuthenticationFilter customAuthenticationFilter;

    @BeforeEach
    void beforeEachSetUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void  Checks_For_User_If_Both_Fields_Are_Filled() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setContentType("application/json");

        Map<String,String> body = new HashMap<>();
        body.put("loginInput","any");
        body.put("password","any");

        request.setContent(new ObjectMapper().writeValueAsString(body).getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(messageSource.getMessage(any(String.class), any(), any(
            Locale.class))).thenReturn("Some error message");

        Authentication result = customAuthenticationFilter.attemptAuthentication(request,response);
    }

    @Test
    void Throws_Exception_When_Username_Is_Null_Or_Empty() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("loginInput", "");
        request.setParameter("password", "user");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(messageSource.getMessage(any(String.class), any(), any(
            Locale.class))).thenReturn("Some error message");


        assertThrows(AuthenticationException.class, () ->
                customAuthenticationFilter.attemptAuthentication(request, response));
    }

    @Test
    void Throws_Exception_When_Password_Is_Null_Or_Empty() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("loginInput", "user");
        request.setParameter("password", "");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(messageSource.getMessage(any(String.class), any(), any(
            Locale.class))).thenReturn("Some error message");

        assertThrows(AuthenticationException.class, () ->
                customAuthenticationFilter.attemptAuthentication(request, response));
    }
}
