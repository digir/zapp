package com.levelUp2.project_scaffolding_server.lib.fs;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class File extends FSObject {
    private String content;

    public File(String name, String content) { super(name); this.content = content; }

    public String getName() { return name; }
    public String getContent() { return content; }

    @Override
    public void substituteVar(String var, String replacement) {
        if (content != null) {
            // Substitute all <<<var_name_here>>>
            String pattern = "<<<(" + Pattern.quote(var) + ")>>>";
            content = content.replaceAll(pattern, replacement);
        }
    }

    @Override
    public Set<String> getVars() {
        Set<String> res = new HashSet<>();
        Pattern pattern = Pattern.compile("<<<(\\W*)>>>");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            res.add(matcher.group(1));
        }
        return res;
    }
    
    @JsonValue
    public String toJson() {
        // Files are serialized with just their contents as a string
        return content;
    }
}
