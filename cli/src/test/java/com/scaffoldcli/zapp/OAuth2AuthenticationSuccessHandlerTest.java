package com.scaffoldcli.zapp.zapp;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import com.scaffoldcli.zapp.zapp.auth.OAuth2AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationSuccessHandlerTest {

    @Mock
    private OAuth2AuthorizedClientService authorizedClientService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private OAuth2AuthenticationToken oauthToken;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    @Mock
    private OAuth2AccessToken accessToken;

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler successHandler;

    @Test
    void testOnAuthenticationSuccess() throws IOException, ServletException {
        String tokenValue = "dummyAccessToken";
        String clientRegistrationId = "google";
        String principalName = "user";

        when(oauthToken.getAuthorizedClientRegistrationId()).thenReturn(clientRegistrationId);
        when(oauthToken.getName()).thenReturn(principalName);
        when(authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName))
            .thenReturn(authorizedClient);
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);
        when(accessToken.getTokenValue()).thenReturn(tokenValue);

        successHandler.onAuthenticationSuccess(request, response, oauthToken);

        assertEquals(tokenValue, ZappApplication.AccessToken);

        String fileContent = Files.readString(Paths.get(ZappApplication.AccessTokenFilePath));
        assertEquals(tokenValue, fileContent);
    }
}