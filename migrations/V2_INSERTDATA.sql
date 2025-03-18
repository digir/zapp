-- Insert users
INSERT INTO users (username, email) VALUES
('admin', 'admin@example.com'),
('user1', 'user1@example.com'),
('user2', 'user2@example.com');

-- Insert var types
INSERT INTO var_types ("name") VALUES
('string'),
('number'),
('select');

-- Insert root scaff (empty project)
INSERT INTO scaffs (id, parent_id, name, descr, author) VALUES
('00000000000000000000000000000000', '00000000000000000000000000000000', 'Empty Project', 'Root empty project', 'admin');

-- Insert project types (direct children of the empty scaff)
INSERT INTO scaffs (id, parent_id, name, descr, author) VALUES
('11111111111111111111111111111111', '00000000000000000000000000000000', 'Python Starter', 'A simple Python starter project with a main script', 'admin'),
('22222222222222222222222222222222', '00000000000000000000000000000000', 'HTML Boilerplate', 'Basic HTML structure for a static web page', 'user1'),
('33333333333333333333333333333333', '00000000000000000000000000000000', 'C++ CLI App', 'A starter template for a command-line C++ application', 'user2'),
('44444444444444444444444444444444', '00000000000000000000000000000000', 'Data Science Notebook', 'A Jupyter notebook setup for data analysis', 'admin'),
('55555555555555555555555555555555', '00000000000000000000000000000000', 'Unity Game Starter', 'A Unity game project with basic setup', 'user1');

-- Insert template files (insertions) for each project type
INSERT INTO insertion (scaff_id, filepath, value) VALUES
('11111111111111111111111111111111', 'main.py', '"""Main script"""
if __name__ == "__main__":
    <<<body>>>'),
('22222222222222222222222222222222', 'index.html', '<!DOCTYPE html>
<html>
<head>
    <title><<<title>>></title>
</head>
<body>
    <<<content>>>
</body>
</html>'),
('33333333333333333333333333333333', 'main.cpp', '#include <iostream>
<<<includes>>>
int main() {
    <<<body>>>
    return 0;
}'),
('44444444444444444444444444444444', 'notebook.ipynb', '{ "cells": [ { "cell_type": "markdown", "source": [ "# <<<title>>>" ] }, { "cell_type": "code", "source": [ "<<<code>>>" ] } ] }'),
('55555555555555555555555555555555', 'main.unity', 'using UnityEngine;
public class Main : MonoBehaviour {
    void Start() {
        <<<game_logic>>>
    }
}');

-- Insert variables for each project type
INSERT INTO vars (scaff_id, "name", "type", "default", descr) VALUES
('11111111111111111111111111111111', 'body', 1, 'print("Hello, World!")', 'Main script body'),
('22222222222222222222222222222222', 'title', 1, 'My Website', 'Page title'),
('22222222222222222222222222222222', 'content', 1, '<h1>Welcome</h1>', 'Page content'),
('33333333333333333333333333333333', 'includes', 1, '#include <vector>', 'Header includes'),
('33333333333333333333333333333333', 'body', 1, 'std::cout << "Hello, World!" << std::endl;', 'Main function body'),
('44444444444444444444444444444444', 'title', 1, 'Data Analysis', 'Notebook title'),
('44444444444444444444444444444444', 'code', 1, 'print("Analyzing data...")', 'Notebook code'),
('55555555555555555555555555555555', 'game_logic', 1, 'Debug.Log("Game Started");', 'Game initialization logic');

-- Insert deeper refinement scaffs (children of the project starters)
INSERT INTO scaffs (id, parent_id, name, descr, author) VALUES
('66666666666666666666666666666666', '11111111111111111111111111111111', 'Flask API Starter', 'A Python project with Flask pre-configured', 'user1'),
('77777777777777777777777777777777', '11111111111111111111111111111111', 'Django Web App', 'A Django starter project with basic setup', 'user2'),
('88888888888888888888888888888888', '22222222222222222222222222222222', 'Bootstrap Template', 'HTML boilerplate with Bootstrap included', 'admin'),
('99999999999999999999999999999999', '33333333333333333333333333333333', 'C++ Logging Framework', 'A CLI app with a logging framework', 'user1');

-- Substitutions to refine templates in deeper scaffs
INSERT INTO substitution (scaff_id, variable, value) VALUES
('66666666666666666666666666666666', 'body', 'from flask import Flask\napp = Flask(__name__)\n@app.route("/")\ndef hello():\n    return "Hello, Flask!"\nif __name__ == "__main__":\n    app.run()'),
('77777777777777777777777777777777', 'body', 'import django\nprint("Django project setup complete")'),
('88888888888888888888888888888888', 'content', '<h1>Welcome to My Bootstrap Page</h1>\n<p>This is a simple Bootstrap-based site.</p>'),
('99999999999999999999999999999999', 'includes', '#include <fstream>\n#include <ctime>'),
('99999999999999999999999999999999', 'body', 'std::ofstream log("log.txt");\nlog << "Logging initialized" << std::endl;');