package com.scaffoldcli.zapp.zapp.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scaffoldcli.zapp.zapp.ServerAccess.ServerAccessHandler;
import com.scaffoldcli.zapp.zapp.lib.AITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class AICliCommand {

    private static final Map<String, Object> jsonMap = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    private final TerminalUIBuilder terminalUIBuilder;

    private AITemplateService aiService;

    @Autowired
    private ObjectMapper objectMapper;

    public AICliCommand(TerminalUIBuilder terminalUIBuilder) {
        this.terminalUIBuilder = terminalUIBuilder;
        this.aiService = new AITemplateService();
    }

    //    @ShellMethod(key = "init", value = "Initializes a project")
    public void init() {
        System.out.println(create());
    }


    //    @ShellMethod(key = "create", value = "Creates a new project from a template")
    public String create() {
        try {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            // 1. Construct Prompt (or Create a Function)
            String projectName = getProjectName();
            String databaseType = getDatabaseType();
            String projectType = getProjectType();

            jsonMap.put("databaseType", databaseType);
            jsonMap.put("projectName", projectName);
            jsonMap.put("projectType", projectType);

            String json = gson.toJson(jsonMap);


            // 2. Call the Gemini AI Service

            String geminiResponse = ServerAccessHandler.createAITemplate(json); //It needs the file from JSON

            return aiService.generateFilesFromGeminiResponse(geminiResponse, projectName);

            // Add error and more secure features in code, this is just to show that it calls to the AI and prints code
            // 3. Display or Store the Generated Template
        } catch (IOException e) {
            // Create specific message on how to proceed, or that action may be required.

            return "Failed to generate project: " + e.getMessage();
        }
    }

    public static String getProjectName() {
        System.out.print("Project name:");
        String name = "";
        while (name.isEmpty()) {
            name = scanner.nextLine();
        }
        return name;
    }

    public static String getDatabaseType() {
        System.out.print("Database type (e.g., PostgreSQL, MySQL)");
        String descr = "";
        while (descr.isEmpty()) {
            descr = scanner.nextLine();
        }
        return descr;
    }

    public static String getProjectType() {
        System.out.print("Project Type (e.g., Java, Python)");
        String descr = "";
        while (descr.isEmpty()) {
            descr = scanner.nextLine();
        }
        scanner.close();
        return descr;
    }
}
