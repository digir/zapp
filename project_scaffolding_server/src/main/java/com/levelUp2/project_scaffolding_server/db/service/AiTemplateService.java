package com.levelUp2.project_scaffolding_server.db.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiTemplateService {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.api-url}")
    private String apiUrl; //You would want to set this as some kind of secret

    @Autowired
    private ObjectMapper objectMapper;

    public String generateCode(String prompt) throws IOException, InterruptedException {
        // Construct the API request URL
        String apiUrlWithKey = apiUrl + "?key=" + apiKey;

        // Create the request body (JSON)
        Map<String, Object> requestBodyMap = new HashMap<>();
        Map<String, String> partsMap = new HashMap<>();
        partsMap.put("text", prompt);
        List<Map<String, String>> partsList = List.of(partsMap);
        Map<String, List<Map<String, String>>> contentsMap = new HashMap<>();
        contentsMap.put("parts", partsList);
        List<Map<String, List<Map<String, String>>>> contentsList = List.of(contentsMap);

        requestBodyMap.put("contents", contentsList);
        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        // Create an HTTP client
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrlWithKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Handle the response
        if (response.statusCode() == 200) {
//            logger.info("Gemini API Response: " + responseBody);
            System.out.println(response.body());
            return response.body(); // The generated code as a string
        } else {
            throw new IOException("Gemini API call failed with code: " + response.statusCode() + ", message: " + response.body());
        }
    }

}
