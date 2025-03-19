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
('55555555555555555555555555555555', '00000000000000000000000000000000', 'Unity Game Starter', 'A Unity game project with basic setup', 'user1'),
('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', '00000000000000000000000000000000', 'Maven barebones', 'Barebones Java project with Maven', 'user1');


-- Insert template files (insertions) for each project type
INSERT INTO insertion (scaff_id, filepath, value) VALUES
('11111111111111111111111111111111', 'main.py', '"""Main script"""
if __name__ == "__main__":
    <<<body>>>'),
('22222222222222222222222222222222', 'src/index.html', '<!DOCTYPE html>
<html>
<head>
    <title><<<title>>></title>
    <link rel="stylesheet" href="../lib/style.css">
    <script lang="text/javascript" src="../lib/script.js"></script>
    <<<headers>>>
</head>
<body>
    <<<content>>>
</body>
</html>'),
('22222222222222222222222222222222', 'lib/script.js', 'window.onload = () => { console.log("Hello from <<<title>>>"); }'),
('22222222222222222222222222222222', 'lib/style.css', 'body { background-color: white; padding: 1em; }'),
('33333333333333333333333333333333', 'src/main.cpp', '#include <iostream>
<<<includes>>>
int main() {
    <<<body>>>
    return 0;
}'),
('33333333333333333333333333333333', '.gitignore', '*.out
<<<ignores>>>'),
('44444444444444444444444444444444', 'notebook.ipynb', '{ "cells": [ { "cell_type": "markdown", "source": [ "# <<<title>>>" ] }, { "cell_type": "code", "source": [ "<<<code>>>" ] } ] }'),
('55555555555555555555555555555555', 'Assets/Scripts/Main.cs', 'using UnityEngine;
public class Main : MonoBehaviour {
    <<<top_definitions>>>

    void Start() {
        <<<initial_game_logic>>>
    }

    void Update() {
        <<<loop_logic>>>
    }

    <<<btm_definitions>>>
}'),
('55555555555555555555555555555555', 'Assets/Main.cs', 'using UnityEngine;
public class Main : MonoBehaviour {
    <<<top_definitions>>>

    void Start() {
        <<<initial_game_logic>>>
    }

    void Update() {
        <<<loop_logic>>>
    }

    <<<btm_definitions>>>
}'),
('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'src/main/java/com/mvnStarter/starterProj', '
package com.mvnStarter.<<<app_name>>>;

<<<main_imports>>

<<<main_annotations>>>
public class Main {
	public static void main(String[] args) {
        <<<main_logic>>>
	}
}
'),
('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'src/pom.xml', '<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/<<<model_version>>>" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/<<<model_version>>> https://maven.apache.org/xsd/maven-<<<model_version>>>.xsd">
	<modelVersion><<<pom_model_version>>></modelVersion>
	<parent>
        <<<pom_parent>>>

		-- <groupId>org.springframework.boot</groupId>
		-- <artifactId>spring-boot-starter-parent</artifactId>
		-- <version>3.4.3</version>
		-- <relativePath/>

	</parent>
	<groupId>com.mvnStarter.starterProj</groupId>
	<artifactId><<<pom_app_name>>></artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name><<<pom_app_name>>></name>
	<description>Maven barebones starter</description>

	<url/>
	<licenses><license/></licenses>
	<developers><developer/></developers>
	<scm><connection/><developerConnection/><tag/><url/></scm>
	<properties>
        <java.version>21</java.version>
        <<<pom_properties>>>
	</properties>
	<dependencies>
        <<<pom_dependencies>>>
	</dependencies>

	<dependencyManagement>
		<dependencies>
            <<<pom_management_dependencies>>>
		</dependencies>
	</dependencyManagement>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

    <<<pom_additional_tags>>>

</project>

'),
('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'README.md', '# Maven barebones starter
> Quick starter project to get you up and running with Maven in no time

<<<readme_content>>>
');


-- Insert variables for each project type
INSERT INTO vars (scaff_id, "name", "type", "default", descr) VALUES
('11111111111111111111111111111111', 'body', 1, 'print("Hello, World!")', 'Main script body'),
('22222222222222222222222222222222', 'title', 1, 'My Website', 'Page title'),
('22222222222222222222222222222222', 'content', 1, '<h1>Welcome</h1>', 'Page content'),
('33333333333333333333333333333333', 'includes', 1, '#include <vector>', 'Header includes'),
('33333333333333333333333333333333', 'body', 1, 'std::cout << "Hello, World!" << std::endl;', 'Main function body'),
('44444444444444444444444444444444', 'title', 1, 'Data Analysis', 'Notebook title'),
('44444444444444444444444444444444', 'code', 1, 'print("Analyzing data...")', 'Notebook code'),
('55555555555555555555555555555555', 'initial_game_logic', 1, 'Debug.Log("Game Started");', 'Game initialization logic');

-- Insert deeper refinement scaffs (children of the project starters)
INSERT INTO scaffs (id, parent_id, name, descr, author) VALUES
('66666666666666666666666666666666', '11111111111111111111111111111111', 'Flask API Starter', 'A Python project with Flask pre-configured', 'user1'),
('77777777777777777777777777777777', '11111111111111111111111111111111', 'Django Web App', 'A Django starter project with basic setup', 'user2'),
('88888888888888888888888888888888', '22222222222222222222222222222222', 'Bootstrap Template', 'HTML boilerplate with Bootstrap included', 'admin'),
('99999999999999999999999999999999', '33333333333333333333333333333333', 'C++ Logging Framework', 'A CLI app with a logging framework', 'user1'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'Spring Boot barebones', 'Barebones Java project using Spring Boot', 'user2');

-- Substitutions to refine templates in deeper scaffs
INSERT INTO substitution (scaff_id, variable, value) VALUES
('66666666666666666666666666666666', 'body', 'from flask import Flask\napp = Flask(__name__)\n@app.route("/")\ndef hello():\n    return "Hello, Flask!"\nif __name__ == "__main__":\n    app.run()'),
('77777777777777777777777777777777', 'body', 'import django\nprint("Django project setup complete")'),
('88888888888888888888888888888888', 'headers', '<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">    <!-- Latest compiled and minified CSS -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>                <!-- jQuery library -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>             <!-- Latest compiled JavaScript -->
<<<headers>>>
'),
('88888888888888888888888888888888', 'content', '<h1>Welcome to My Bootstrap Page</h1>\n<p>This is a simple Bootstrap-based site.</p>
<<<content>>>
'),
('99999999999999999999999999999999', 'includes', '#include <fstream>\n#include <ctime>'),
('99999999999999999999999999999999', 'body', 'std::ofstream log("log.txt");\nlog << "Logging initialized" << std::endl;'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'main_imports', 'import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'main_annotations', '@SpringBootApplication'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'main_logic', 'SpringApplication.run(ProjectScaffoldingServerApplication.class, args);'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'pom_parent', '<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-parent</artifactId>
<version>3.4.3</version>
<relativePath/>
'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'pom_model_version', ''), -- default 3.4.4
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'pom_properties', '<spring-shell.version>3.4.0</spring-shell.version>
'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'pom_dependencies', '<dependency> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-oauth2-client</artifactId> </dependency>
<dependency> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-test</artifactId> <scope>test</scope> </dependency>
<dependency> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-web</artifactId> </dependency>
<dependency> <groupId>org.springframework.security</groupId> <artifactId>spring-security-test</artifactId> <scope>test</scope> </dependency>
<dependency> <groupId>org.springframework.shell</groupId> <artifactId>spring-shell-starter-jna</artifactId> </dependency>
<dependency> <groupId>org.springframework.shell</groupId> <artifactId>spring-shell-starter</artifactId> </dependency>
<dependency> <groupId>org.projectlombok</groupId> <artifactId>lombok</artifactId> <version>1.18.36</version> </dependency>
'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'pom_management_dependencies', '<dependency>
    <groupId>org.springframework.shell</groupId>
    <artifactId>spring-shell-dependencies</artifactId>
    "<version>${spring-shell.version}</version>"
    <type>pom</type>
    <scope>import</scope>
</dependency>
'),
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'pom_additional_tags', '');

INSERT INTO insertion (scaff_id, filepath, value) VALUES
('bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', 'src/pom.xml', '"""Main script"""
if __name__ == "__main__":
    <<<body>>>');