package com.scaffoldcli.zapp.commands;

import com.scaffoldcli.zapp.lib.Text;

public class HelpCommand implements Command {
    @Override
    public void run(String[] args) {
        if (args.length < 2) {
            Text.print("NappingWhenWeShouldBeZapping\n", Text.Colour.bright_green, true);
            Text.print("Usage: zapp <command> <options>\n");
            Text.print("Commands:");
            Text.print("\tcreate");
            Text.print("\tscaff");
            Text.print("\tupdate\n");
            Text.print("Generate Options:");
            Text.print("\tscaff:");
        }
    }
}
