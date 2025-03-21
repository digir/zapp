//package com.scaffoldcli.zapp;
//
//import org.springframework.boot.Banner.Mode;
//import org.springframework.boot.SpringApplication;
//
////@SpringBootApplication
////@CommandScan
//public class ZappApplication {
//
//    public static String AccessToken = null;
//    public static String ClientUrl = "http://localhost:8001/";
//    public static String ServerUrl = "http://localhost:8002/";
//    public static String AccessTokenFilePath = System.getProperty("java.io.tmpdir") + "AccessToken.txt";
//
//    public static void main(String[] args) throws Exception {
//        //========== Spring init ==========//
//        SpringApplication application = new SpringApplication(ZappApplication.class);
//        application.setBannerMode(Mode.OFF);
//        // application.setLogStartupInfo(false);
//        application.run(args);
//    }
//}