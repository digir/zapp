package com.scaffoldcli.zapp.zapp.ServerAccess;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.scaffoldcli.zapp.zapp.ZappApplication;
import com.scaffoldcli.zapp.zapp.auth.AutheticateUser;

public class ServerAccessHandler {
    
    public static String getScaffServerRequest(String requestString){
        String apiResultString = null;
        RestTemplate restTemplate = new RestTemplate();
        try {
            apiResultString = restTemplate.getForObject(ZappApplication.ServerUrl+ "/scaff/"+requestString+"?access_token="+ZappApplication.AccessToken, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                AutheticateUser.triggerUserAutheticationFlow();
                Integer tryCount = 10;
                while (!AutheticateUser.isUserAutheticated()) {
                    tryCount--;
                }
                apiResultString = restTemplate.getForObject(ZappApplication.ServerUrl+ "/scaff/"+requestString+"?access_token="+ZappApplication.AccessToken, String.class);
            }
            else {
                System.exit(1);
            }
        }
        return apiResultString;
    }
}
