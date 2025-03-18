package com.levelUp2.project_scaffolding_server.lib.fs;

public class FileSystem {
    private Folder root; // File hierarchy tree

    public FileSystem() {
        // Root folder name is arbitrary - it is never serialized
        root = new Folder("#");
    }

    // Insert file into the hierarchy at a specific path
    // Non-preexisting folders will be dynamically generated as required
    public void insertFile(String path, String content) {
        String[] parts = path.split("/");
        Folder current = root;

        // Scaffold folder hierarchy
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            FSObject child = current.getChildren().get(part);

            if (child == null) {
                // Create new folder
                Folder newFolder = new Folder(part);
                current.addChild(part, newFolder);
                current = newFolder;
            } else if (child instanceof Folder) {
                // Use existing folder
                current = (Folder) child;
            } else {
                // Name conflict: File & folder have same name in same directory
                throw new IllegalArgumentException("A file already exists with the folder name: " + part);
            }
        }

        // Render file
        String fileName = parts[parts.length - 1];
        File file = new File(fileName, content);
        current.addChild(fileName, file);
    }

    // Substitute all occurrences of the template <<<var_name_here>>> with the replacement text in every file
    public void substituteVar(String var, String replacement) {
        root.substituteVar(var, replacement);
    }

    // Expose file structure for JSON serialization
    public Folder getFiles() { return root; }
}
