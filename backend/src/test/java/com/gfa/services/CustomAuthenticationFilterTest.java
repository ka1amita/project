package com.gfa.services;

import com.gfa.filters.CustomAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CustomAuthenticationFilterTest {

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
        request.setParameter("username", "user");
        request.setParameter("password", "user");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication result = customAuthenticationFilter.attemptAuthentication(request,response);
    }



    @Test
    void Throws_Exception_When_Username_Is_Null_Or_Empty() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", "");
        request.setParameter("password", "user");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThrows(AuthenticationException.class, () ->
                customAuthenticationFilter.attemptAuthentication(request, response));
    }

    @Test
    void Throws_Exception_When_Password_Is_Null_Or_Empty() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", "user");
        request.setParameter("password", "");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThrows(AuthenticationException.class, () ->
                customAuthenticationFilter.attemptAuthentication(request, response));
    }
}
