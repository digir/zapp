package com.scaffoldcli.zapp.zapp.ServerAccess;

import jakarta.annotation.Nullable;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.scaffoldcli.zapp.zapp.ZappApplication;
import com.scaffoldcli.zapp.zapp.auth.AutheticateUser;

public class ServerAccessHandler {

    private static String reqScaff(String scaffId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ZappApplication.ServerUrl + "/scaff/" + scaffId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ZappApplication.AccessToken);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        return response.getBody();
    }
    
    public static String getScaffServerRequest(String scaffId) {
        String res = "";
        try { res = reqScaff(scaffId); }
        catch (ResourceAccessException e) {
            System.out.println("\n\n\t\u001B[91m> API server unreachable - exiting\u001B[0m\n\n");
            System.exit(0);
        }
        catch (HttpClientErrorException e) {
            System.out.println("\n\t\u001B[93m> You are not authenticated - polling for auth\u001B[0m");
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                AutheticateUser.triggerUserAutheticationFlow();
                boolean authenticated = false;
                for (int i = 0; i < 30 && !authenticated; i++) { if (AutheticateUser.isUserAutheticated()) authenticated = true; }
                if (authenticated) {
                    System.out.println("\n\n\t\u001B[92m> Authenticated - launching CLI\u001B[0m\n\n");
                    res = reqScaff(scaffId);
                }
                else {
                    System.out.println("\n\n\t\u001B[91m> Failed to authenticate user - exiting\u001B[0m\n\n");
                    System.exit(0);
                }
            }
            else {
                System.out.println("\n\n\t\u001B[91m> Failed connecting to API - exiting\u001B[0m\n\n");
                System.exit(1);
            }
        }
        return res;
    }


    public static String createScaffServerRequest(String jsonBody) {
        String res = "";
        try {
            res = createScaff(jsonBody);
        }
        catch (ResourceAccessException e) {
            System.out.println("\n\n\t\u001B[91m> API server unreachable - exiting\u001B[0m\n\n");
            System.exit(0);
        }
        catch (HttpClientErrorException e) {
            System.out.println("\n\t\u001B[93m> You are not authenticated - polling for auth\u001B[0m");
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                AutheticateUser.triggerUserAutheticationFlow();
                boolean authenticated = false;
                for (int i = 0; i < 30 && !authenticated; i++) {
                    if (AutheticateUser.isUserAutheticated()) authenticated = true;
                }
                if (authenticated) {
                    System.out.println("\n\n\t\u001B[92m> Authenticated - launching CLI\u001B[0m\n\n");
                    res = createScaff(jsonBody);
                }
                else {
                    System.out.println("\n\n\t\u001B[91m> Failed to authenticate user - exiting\u001B[0m\n\n");
                    System.exit(0);
                }
            }
            else {
                System.out.println("\n\n\t\u001B[91m> Failed connecting to API - exiting\u001B[0m\n\n");
                System.exit(1);
            }
        }
        return res;
    }

    private static String createScaff(String jsonBody) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = ZappApplication.ServerUrl + "scaff/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        System.out.println(String.format("\n\n\tConnecting to server: %s\n\n", apiUrl));

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }
}
