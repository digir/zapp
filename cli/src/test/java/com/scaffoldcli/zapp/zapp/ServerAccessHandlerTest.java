package com.scaffoldcli.zapp.zapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaffoldcli.zapp.zapp.ServerAccess.ServerAccessHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

public class ServerAccessHandlerTest {

    @Test
    void testGetScaffServerRequestSuccess() {
        String mockResponse = "{\"files\": { \"file1.txt\": \"Hello World\" } }";
        try (MockedStatic<ServerAccessHandler> mockedServerAccess = mockStatic(ServerAccessHandler.class)) {
            mockedServerAccess.when(() -> ServerAccessHandler.getScaffServerRequest("scaff1"))
                              .thenReturn(mockResponse);
            
            String result = ServerAccessHandler.getScaffServerRequest("scaff1");
            assertEquals(mockResponse, result);
        }
    }

    @Test
    void testGetScaffServerRequestUnauthorized() {
        try (MockedStatic<ServerAccessHandler> mockedServerAccess = mockStatic(ServerAccessHandler.class)) {
            mockedServerAccess.when(() -> ServerAccessHandler.getScaffServerRequest("scaff1"))
                              .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
            
            Exception exception = assertThrows(HttpClientErrorException.class, () -> {
                ServerAccessHandler.getScaffServerRequest("scaff1");
            });
            assertEquals(HttpStatus.UNAUTHORIZED, ((HttpClientErrorException) exception).getStatusCode());
        }
    }

    @Test
    void testGetScaffServerRequestApiUnreachable() {
        try (MockedStatic<ServerAccessHandler> mockedServerAccess = mockStatic(ServerAccessHandler.class)) {
            mockedServerAccess.when(() -> ServerAccessHandler.getScaffServerRequest("scaff1"))
                              .thenThrow(new ResourceAccessException("API unreachable"));
            
            Exception exception = assertThrows(ResourceAccessException.class, () -> {
                ServerAccessHandler.getScaffServerRequest("scaff1");
            });
            assertEquals("API unreachable", exception.getMessage());
        }
    }
}
