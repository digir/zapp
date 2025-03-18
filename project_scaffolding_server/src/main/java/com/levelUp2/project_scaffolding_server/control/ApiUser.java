package com.levelUp2.project_scaffolding_server.control;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.levelUp2.project_scaffolding_server.ProjectScaffoldingServerApplication;;

@RestController
@RequestMapping("/api")
public class ApiUser {

    private final ProjectScaffoldingServerApplication projectScaffoldingServerApplication;

    int counter = 0;

    ApiUser(ProjectScaffoldingServerApplication projectScaffoldingServerApplication) {
        this.projectScaffoldingServerApplication = projectScaffoldingServerApplication;
    }

    // {
    //     "abc": {
    //         "name": "My cool React scaff",
    //         "desc": "Scaff for a basic React template printing hello world to the console",
    //         "author": "conquer"
    //     },
    //     "123": {
    //         "name": "Folio",
    //         "desc": "A snazzy starter portfolio site written in Hugo",
    //         "author": "GreatLove"
    //     },
    // }

    @GetMapping("options")
    // /api/scaff/<scaff id>/options
    public String getOptions(@RequestHeader(name="Authorization") String jwt, @RequestParam(name="scaff") String scaffId) {
        // Get all child scaffs for given scaff id
        return "";
    }

    @GetMapping("rendered")
    // /api/scaff/<scaff id>/rendered
    public String getRendered(@RequestHeader(name="Authorization") String jwt, @RequestParam(name="scaff") String scaffId) {
        // Recursive traversal through scaff tree, building file system

        return "";
    }


    @GetMapping("path")
    public String getMethodName(@RequestHeader(name="Authorization") String jwt) {
        return String.valueOf(counter++) + ": " + jwt;
    }
    

    @GetMapping("/userinfo")
    public ResponseEntity<String> getUserInfo(@RequestParam(value = "access_token") String accessToken) {
        System.out.println("Server: Received token, verifying with Google...");

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        RestTemplate restTemplate = new RestTemplate();
        try {
            String userInfo = restTemplate.getForObject(userInfoUrl + "?access_token=" + accessToken, String.class);
            System.out.println("Server: User details retrieved from Google.");
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or request error.");
        }
    }
}
