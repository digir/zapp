package com.scaffoldcli.zapp.ServerAccess;

public class AppUrls {
    private static final String client = "http://localhost:8001/";
    private static final String server="http://13.245.89.160:8080/";
    private static final String googleTokenInfo = "https://www.googleapis.com/oauth2/v3/userinfo";

    public static String getClient(){
        return client;
    }

    public static String getServer(){
        return server;
    }

    public static String getGoogleTokenInfoUrl(){
        return googleTokenInfo;
    }

}
