package com.scaffoldcli.zapp.zapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

import com.scaffoldcli.zapp.zapp.UserProjectConfig.ProjectStructure;

@SpringBootApplication
@CommandScan
public class ZappApplication {
	public static String AccessToken = null;
	public static String ClientUrl = "http://localhost:8001/";
	public static String ServerUrl = "http://localhost:8002/";
	public static String AccessTokenFilePath = "cli\\src\\main\\java\\com\\scaffoldcli\\zapp\\zapp\\auth\\AccessToken.txt";
	public static String UserCreatedFilesDir = "cli\\src\\main\\java\\com\\scaffoldcli\\zapp\\zapp\\UserProjectConfig\\UserCreatedFiles";
	public static String StartingScaff = "00000000000000000000000000000000";


	public static void main(String[] args) throws Exception {
		//========== Spring init ==========//
		SpringApplication application = new SpringApplication(ZappApplication.class);
		application.setBannerMode(Mode.OFF);
		application.run(args);
		// SpringApplication.run(ZappApplication.class, args);

		ProjectStructure projectStuctureBuilder = new ProjectStructure(StartingScaff);
		if(projectStuctureBuilder.start()){
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
}