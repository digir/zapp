package com.scaffoldcli.zapp.zapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class ZappApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication application = new SpringApplication(ZappApplication.class);
		application.setBannerMode(Mode.OFF);
		application.run(args);
		// SpringApplication.run(ZappApplication.class, args);
	}
}