package com.levelUp2.project_scaffolding_server.control;

import com.levelUp2.project_scaffolding_server.db.entity.GeminiModel;
import com.levelUp2.project_scaffolding_server.db.service.AiTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class AiTemplateController {

    @Autowired
    private AiTemplateService aiService;

    @PostMapping("/gemini/template")
    public String generateCode(@RequestBody GeminiModel userInput) throws IOException, InterruptedException, IOException {


        String projectName = userInput.projectName();

        if (projectName.isEmpty()){
            return "There is no project name";
        }

        String prompt = constructPrompt(userInput);

        String geminiResponse = aiService.generateCode(prompt);

        if (geminiResponse == null) {
            return "Gemini API failed to generate the code. Ensure that everything functions properly.";
        }


        return geminiResponse;
    }

    private String constructPrompt(GeminiModel userInput) {
        StringBuilder promptBuilder = new StringBuilder();


        //Start of Prompt:
        promptBuilder.append("You are a code generator. I need you to create a ").append(userInput.projectType()).append(" Project with the properties:");

        promptBuilder.append(" Project Name: ").append(userInput.projectName());

        if (!userInput.databaseType().isEmpty()) {
            promptBuilder.append(" Database Type: ").append(userInput.databaseType());
        }



        promptBuilder.append("The response must be a JSON object with a 'file_structure' property. The 'file_structure' should contain the keys: { 'file': content'}. ");
        promptBuilder.append("It should be 'file_structure' property, then each file needs to use filename as a key, the values to them need to follow the standard {'content': 'codes here','is_binary': boolean} with is_binary if any are images, and if not the value can be omitted. Please make the code safe to implement and please note any potential errors that may occur.\n");

        return promptBuilder.toString();
    }
}