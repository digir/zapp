package com.scaffoldcli.zapp.zapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.scaffoldcli.zapp.zapp.auth.Controller;
import com.scaffoldcli.zapp.zapp.auth.OAuth2AuthenticationSuccessHandler;
import com.scaffoldcli.zapp.zapp.auth.SecurityConfig;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(Controller.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OAuth2AuthorizedClientService authorizedClientService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler successHandler;

    @Test
    @WithMockUser
    void testLoginSuccessEndpoint() throws Exception {
        mockMvc.perform(get("/login/success"))
            .andExpect(status().isOk())
            .andExpect(content().string("Login success!"));
    }

    @Test
    void testUnauthenticatedAccessToLoginSuccess() throws Exception {
        mockMvc.perform(get("/login/success"))
            .andExpect(status().is3xxRedirection());
    }
}