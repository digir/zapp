package com.scaffoldcli.zapp;

import com.scaffoldcli.zapp.commands.CreateScaffCommand;
import com.scaffoldcli.zapp.commands.HelpCommand;

import java.util.HashMap;
import java.util.Map;

public class NewCLIApplication {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("help", NewCLIApplication::help);
        commands.put("create", NewCLIApplication::create);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            help(args);
            return;
        }

        Command command = commands.getOrDefault(args[0].toLowerCase(), NewCLIApplication::help);
        command.execute(args);
    }

    private static void help(String[] args) {
        HelpCommand command = new HelpCommand();
        command.run(args);
    }

    private static void create(String[] args) {
        CreateScaffCommand command = new CreateScaffCommand();
        command.run(args);
    }

    @FunctionalInterface
    interface Command {
        void execute(String[] args);
    }
}
