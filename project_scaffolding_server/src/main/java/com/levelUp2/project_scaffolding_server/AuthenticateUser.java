package com.levelUp2.project_scaffolding_server;

import org.springframework.web.client.RestTemplate;;

public class AuthenticateUser {
    public static Boolean getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject(userInfoUrl + "?access_token=" + accessToken, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
