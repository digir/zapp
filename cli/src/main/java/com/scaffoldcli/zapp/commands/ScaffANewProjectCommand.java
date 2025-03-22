package com.scaffoldcli.zapp.commands;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scaffoldcli.zapp.lib.Text;
import com.scaffoldcli.zapp.lib.Text.Colour;
import com.scaffoldcli.zapp.models.GetScaffOptionsResponse;
import com.scaffoldcli.zapp.models.GetScaffsResponse;
import com.scaffoldcli.zapp.net.ZappAPIRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScaffANewProjectCommand implements Command {
    private final HashMap<Integer, String> choices = new HashMap<>();
    private List<GetScaffsResponse> items;

    public static void generateScaffFiles(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String outputDirectory = "output/";
        new File(outputDirectory).mkdirs();

        createFilesAndDirectories(outputDirectory, jsonObject.getAsJsonObject("files"));
        Text.print("Scaff generated. Happy coding...", Colour.bright_green);
    }

    private static void createFilesAndDirectories(String basePath, JsonObject filesObject) {
        for (Map.Entry<String, JsonElement> entry : filesObject.entrySet()) {
            String path = basePath + entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonObject()) {
                new File(path).mkdirs();
                createFilesAndDirectories(path + "/", value.getAsJsonObject());
            } else if (value.isJsonPrimitive()) {
                createFile(path, value.getAsString());
            }
        }
    }

    private static void createFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(content);
            }
        } catch (IOException e) {
            Text.print("Error writing file: " + filePath, Colour.red);
        }
    }

    private static void handleMoreOptions(String response) {
        Text.print("More options:", Colour.white, true);
        Map<String, GetScaffsResponse> options = GetScaffOptionsResponse.fromJson(response);

        for (Map.Entry<String, GetScaffsResponse> entry : options.entrySet()) {
            String optionKey = entry.getKey();
            GetScaffsResponse scaff = entry.getValue();
            Text.print(String.format("%s: %s - %s", optionKey, scaff.getName(), scaff.getDescription()), Colour.bright_white);
        }
    }

    private static void fetchRender(String scaffId) {
        HttpResponse<String> res = new ZappAPIRequest().get(String.format("/scaff/%s/rendered", scaffId));

        if (res.statusCode() == 200) {
            generateScaffFiles(res.body());
        } else {
            Text.print("Could not get scaff file structure. Please try again", Colour.bright_red, true);
        }
    }

    private void getScaffData(String scaffId) {
        HttpResponse<String> response = new ZappAPIRequest().get(String.format("/scaff/%s/options", scaffId));

        if (response.statusCode() == 200) {
            if (Objects.equals(response.body(), "204")) {
                fetchRender(scaffId);
            } else {
                handleMoreOptions(response.body());

                String input = Text.input("\nEnter option key to continue or type 'r' to render:");
                if ("r".equalsIgnoreCase(input.trim())) {
                    fetchRender(scaffId);
                } else {
                    Map<String, GetScaffsResponse> options = GetScaffOptionsResponse.fromJson(response.body());
                    GetScaffsResponse selected = options.get(input.trim());
                    if (selected != null) {
                        getScaffData(selected.getId());
                    } else {
                        Text.print("Invalid option, please try again.", Colour.red);
                        getScaffData(scaffId);
                    }
                }
            }
        } else {
            Text.print("Could not get scaff options. Please try again.", Colour.bright_red, true);
        }
    }

    @Override
    public void run(String[] args) {
        HttpResponse<String> response = new ZappAPIRequest().get("/scaff/");
        this.items = GetScaffsResponse.fromJson(response.body());

        for (int i = 0; i < this.items.size(); i++) {
            GetScaffsResponse scaff = this.items.get(i);
            this.choices.put(i + 1, scaff.getId());
            Text.print(String.format("%s %s", i + 1, scaff.getName()), Colour.bright_white);
        }

        String selection = Text.input("\nScaff id");
        try {
            Integer index = Integer.parseInt(selection);
            String scaffId = this.choices.get(index);
            if (scaffId != null) {
                getScaffData(scaffId);
            } else {
                Text.print("Invalid selection. Exiting.", Colour.red);
            }
        } catch (NumberFormatException e) {
            Text.print("Invalid selection. Exiting.", Colour.red);
        }
    }
}
