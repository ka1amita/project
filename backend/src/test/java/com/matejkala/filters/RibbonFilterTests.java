package com.matejkala.filters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import com.matejkala.config.RibbonProperties;
import com.matejkala.services.EnvironmentService;
import com.matejkala.services.TokenService;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class) // Use this jupiter.* instead of similar junit.runner.RunWith
@WebMvcTest(TestController.class)
class RibbonFilterTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HttpServletRequest request;
    @MockBean
    RibbonProperties ribbonProperties;
    @MockBean
    EnvironmentService environmentService;
    @MockBean
    TokenService tokenService;

    @Test
    public void responseContainsHeadersWithInformationAboutEnvironment() throws Exception {
        // one must use the org.mockito.ArgumentMatchers.any!
        when(environmentService.getServerName(any(HttpServletRequest.class))).thenReturn("host");
        when(request.getServerName()).thenReturn("host");
        when(environmentService.getEnv()).thenReturn("env");
        when(ribbonProperties.isEnabled()).thenReturn(true);

        mockMvc.perform(get("/test"))
               .andExpect(header().exists("Environment"))
               .andExpect(header().exists("Hostname"))
               .andExpect(header().stringValues("Environment", "env"))
               .andExpect(header().stringValues("Hostname", "host"))
               .andDo(print());
    }

    @Test
    public void responseDoesntContainEnvironmentHeadersWhenPropertyNotSet() throws Exception {
        // one must use the org.mockito.ArgumentMatchers.any!
        when(environmentService.getServerName(any(HttpServletRequest.class))).thenReturn("host");
        when(environmentService.getEnv()).thenReturn(null);
        when(ribbonProperties.isEnabled()).thenReturn(true);

        mockMvc.perform(get("/test"))
               .andExpect(header().doesNotExist("Environment"))
               .andDo(print());
    }

    @Test
    public void responseDoesntContainHeadersWhenDisabledInGradleProperties() throws Exception {
        // one must use the org.mockito.ArgumentMatchers.any!
        when(environmentService.getServerName(any(HttpServletRequest.class))).thenReturn("host");
        when(request.getServerName()).thenReturn("host");
        when(environmentService.getEnv()).thenReturn("env");
        when(ribbonProperties.isEnabled()).thenReturn(false);

        mockMvc.perform(get("/test"))
               .andExpect(header().doesNotExist("Environment"))
               .andExpect(header().doesNotExist("Hostname"))
               .andDo(print());
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new TestController())
            .addFilter(new RibbonFilter(ribbonProperties, environmentService))
            .build();
    }
}