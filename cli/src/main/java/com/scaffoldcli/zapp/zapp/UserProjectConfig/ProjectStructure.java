package com.scaffoldcli.zapp.zapp.UserProjectConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaffoldcli.zapp.zapp.ServerAccess.ServerAccessHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class ProjectStructure {
    public static Map<String, String> getScaffOptions(String scaffId) {
        String scaffOptions = ServerAccessHandler.getScaffServerRequest(scaffId + "/options");
        return getScaffIdNameMap(scaffOptions);
    }

    // Fetch rendered project from API, then construct the file system
    public static void executeFinalScaff(String projectName, String scaffId, Map<String, String> varSubs) {
        String scaffToCreateJson = ServerAccessHandler.getScaffServerRequest(scaffId + "/rendered");
        createFilesFromJson(projectName, scaffToCreateJson, varSubs);
    }

    public static void createFilesFromJson(String projectName, String jsonString, Map<String, String> varSubs) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;

        try {
            rootNode = objectMapper.readTree(jsonString);
        } catch (IOException e) {
            System.exit(1);
            return;
        }

        JsonNode filesNode = rootNode.path("files");
        processFilesNode(projectName, filesNode, varSubs);
    }

    // Substitute all <<<var_name_here>>> occurences in `content` with `replacement`
    private static String substituteVar(String content, String var, String replacement) {
        if (content != null) {
            String pattern = "<<<(" + Pattern.quote(var) + ")>>>";
            content = content.replaceAll(pattern, replacement);
        }
        return content;
    }

    private static void processFilesNode(String parentDir, JsonNode filesNode, Map<String, String> varSubs) {
        Iterator<Map.Entry<String, JsonNode>> fields = filesNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value = field.getValue();
            String objPath = parentDir + "/" + key;

            if (value.isObject()) { // Folder
                File dir = new File(objPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                processFilesNode(objPath, value, varSubs);
            } else {
                String fileContent = value.asText();
                for (var e : varSubs.entrySet()) {
                    fileContent = substituteVar(fileContent, e.getKey(), e.getValue());
                }

                try {
                    Files.write(Paths.get(objPath), fileContent.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, String> getScaffIdNameMap(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> scaffNames = new HashMap<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();

                JsonNode value = field.getValue();
                String name = value.get("name").asText();
                scaffNames.put(key, name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaffNames;
    }
}
