package com.scaffoldcli.zapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scaffoldcli.zapp.auth.AutheticateUser;

@ExtendWith(MockitoExtension.class)
class AutheticateUserTest {

    @Test
    void testIsUserAuthenticated() throws IOException {
        String tokenPath = ZappApplication.AccessTokenFilePath;

        if (tokenPath != null && !Files.exists(Paths.get(tokenPath))) {
            Files.createFile(Paths.get(tokenPath));
        }

        Files.writeString(Paths.get(tokenPath), "dummyAccessToken");
        try (MockedStatic<AutheticateUser> mockedStatic = mockStatic(AutheticateUser.class)) {
            mockedStatic.when(AutheticateUser::triggerUserAutheticationFlow).thenAnswer(invocation -> {
                return null;
            });
            mockedStatic.when(AutheticateUser::authenticateUser).thenReturn(true);

            boolean isAuthenticated = AutheticateUser.isUserAutheticated();

            assertFalse(isAuthenticated);
        }
    }
}