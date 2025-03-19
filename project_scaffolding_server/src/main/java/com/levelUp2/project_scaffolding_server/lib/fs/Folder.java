package com.levelUp2.project_scaffolding_server.lib.fs;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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

    @Override
    public Set<String> getVars() {
        Set<String> res = new HashSet<>();
        for (FSObject child : children.values()) {
            Set<String> childVars = child.getVars();
            for (String s : childVars) res.add(s);
        }
        return res;
    }
    
    @JsonValue
    public Map<String, FSObject> toJson() {
        // Folders are serialized as JSON object with children as keys
        return children;
    }
}
