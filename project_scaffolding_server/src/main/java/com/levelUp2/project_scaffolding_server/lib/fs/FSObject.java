package com.levelUp2.project_scaffolding_server.lib.fs;

import java.util.Set;

// Composite pattern for representing file system structure
// See concrete classes: { fs/File, fs/Folder }
public abstract class FSObject {
    protected String name = "";
    FSObject(String name) { this.name = name; }

    public abstract void substituteVar(String var, String replacement);

    public abstract Set<String> getVars();
}
