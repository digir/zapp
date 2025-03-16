package com.levelUp2.project_scaffolding_server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ScaffApiHandler {
    
    @GetMapping("/api/scaff/{scaff_id}/options")
    public ResponseEntity<String> getScaffOptions(@PathVariable("scaff_id") String scaffId, @RequestParam String access_token) {
        if(!AuthenticateUser.getUserInfo(access_token)){
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String response = "";
        if(scaffId.equals("00000000000000000000000000000000")){
            response = "{\n" +
                "\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\": {\n" +
                "    \"name\": \"My cool React scaff\",\n" +
                "    \"desc\": \"Scaff for a basic React template printing hello world to the console\",\n" +
                "    \"author\": \"conquer\"\n" +
                "},\n" +
                "\"BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB\": {\n" +
                "    \"name\": \"Folio\",\n" +
                "    \"desc\": \"A snazzy starter portfolio site written in Hugo\",\n" +
                "    \"author\": \"GreatLove\"\n" +
                "}\n" +
            "}";
        }
        if(scaffId.equals("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
            response = "{\n" +
                "\"CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC\": {\n" +
                "    \"name\": \"My cool React scaff\",\n" +
                "    \"desc\": \"Scaff for a basic React template printing hello world to the console\",\n" +
                "    \"author\": \"conquer\"\n" +
                "},\n" +
                "\"BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB\": {\n" +
                "    \"name\": \"Snazzy\",\n" +
                "    \"desc\": \"A snazzy starter portfolio site written in Hugo\",\n" +
                "    \"author\": \"GreatLove\"\n" +
                "}\n" +
            "}";
        }
        if(scaffId.equals("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")){
            response = "{}";
        }
        if (scaffId.equals("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC")){
            response = "{}";
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/scaff/{scaff_id}/genereted")
    public ResponseEntity<String> getGeneretedScaff(@PathVariable("scaff_id") String scaffId,  @RequestParam String access_token){
        if(!AuthenticateUser.getUserInfo(access_token)){
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String response = "";
        if(scaffId.equals("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")){
            response = "{\n" +
            "\"vars\": {\n" +
            "    \"message_var\": {\n" +
            "        \"type\": \"string\"\n" +
            "    },\n" +
            "    \"background_color\": {\n" +
            "        \"type\": \"option\",\n" +
            "        \"options\": [ \"red\", \"green\", \"blue\" ]\n" +
            "    }\n" +
            "},\n" +
            "\"files\": {\n" +
            "    \"src\": {\n" +
            "        \"newFolder\": {\n" +
            "            \"newFile.txt\": \"It works\"\n" +
            "         },\n" +
            "        \"newEmptyFolder\": {},\n" +
            "        \"index.html\": \"<body style='background-color: {{background_color}}'>\\n" + 
            "            <h1> Hello world </h1>\\n" +
            "        </body>\\n\",\n" +
            "        \"script.js\": \"console.log({{message_var}})\\n\"\n" +
            "    },\n" +
            "    \".gitignore\": \"node_modules/\\n\"\n" +
            "}\n" +
        "}";  
        }

        return ResponseEntity.ok(response);
    }
}
