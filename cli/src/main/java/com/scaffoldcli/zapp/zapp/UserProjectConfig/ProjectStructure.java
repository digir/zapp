package com.scaffoldcli.zapp.zapp.UserProjectConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaffoldcli.zapp.zapp.ServerAccess.ServerAccessHandler;

public class ProjectStructure {
    public static Map<String,String> getScaffOptions(String scaffId){
        String scaffOptions = ServerAccessHandler.getScaffServerRequest(scaffId+"/options");
        Map<String,String> scaffOptionNames = getScaffIdNameMap(scaffOptions);
        return scaffOptionNames;
    }

    public static void executeFinalScaff (String scaffId){
        String scaffToCreateJson = ServerAccessHandler.getScaffServerRequest(scaffId+"/genereted");
        createFilesFromJson(scaffToCreateJson);
    }

    private static void createFilesFromJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;

        try {
            rootNode = objectMapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode filesNode = rootNode.path("files");

        processFilesNode("MyProj", filesNode);
    }

    private static void processFilesNode(String parentDir, JsonNode filesNode) {
        Iterator<Map.Entry<String, JsonNode>> fields = filesNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value = field.getValue();

            if (value.isObject()) {
                String folderPath = parentDir + File.separator + key;

                File dir = new File(folderPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                processFilesNode(folderPath, value);
            } else {
                String filePath = parentDir + File.separator + key;
                String fileContent = value.asText();

                try {
                    Files.write(Paths.get(filePath), fileContent.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Map<String, String> getScaffIdNameMap(String jsonString){
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
