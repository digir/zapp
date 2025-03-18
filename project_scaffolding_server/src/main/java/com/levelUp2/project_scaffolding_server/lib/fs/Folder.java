package com.levelUp2.project_scaffolding_server.lib.fs;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.LinkedHashMap;
import java.util.Map;

public class Folder extends FSObject {
    private Map<String, FSObject> children = new LinkedHashMap<>();

    public Folder(String name) { super(name); }
    
    public String getName() { return name; }
    public Map<String, FSObject> getChildren() { return children; }
    public void addChild(String name, FSObject child) { children.put(name, child); }
    
    @Override
    public void substituteVar(String var, String replacement) {
        for (FSObject child : children.values()) {
            child.substituteVar(var, replacement);
        }
    }
    
    @JsonValue
    public Map<String, FSObject> toJson() {
        // Folders are serialized as JSON object with children as keys
        return children;
    }
}
