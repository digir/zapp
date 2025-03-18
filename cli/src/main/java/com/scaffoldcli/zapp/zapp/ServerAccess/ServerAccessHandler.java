package com.scaffoldcli.zapp.zapp.ServerAccess;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
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
    
    public static String getScaffServerRequest(String scaffId){
        String res = "";
        try { res = reqScaff(scaffId); }
        catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                AutheticateUser.triggerUserAutheticationFlow();
                for (int i = 0; i < 30; i++) { if (AutheticateUser.isUserAutheticated()) break; }
                res = reqScaff(scaffId);
            }
            else { System.exit(1); } // Unknown exception, sepuku
        }
        return res;
    }
}
