package com.scaffoldcli.zapp.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scaffoldcli.zapp.ServerAccess.ServerAccessHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Delete {
    private static final Map<String, Object> jsonMap = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public void run() {
        String scaffName = getScaffName();

        jsonMap.put("scaffName", scaffName);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        String json = gson.toJson(jsonMap);

        ServerAccessHandler.deleteScaffServerRequest(json);

        System.out.println("Scaffold '" + scaffName + "' has been deleted.");
    }

    private static String getScaffName() {
        System.out.print("Enter the name of the scaffold to delete: ");
        String name = "";
        while (name.isEmpty()) {
            name = scanner.nextLine();
        }
        return name;
    }
}
