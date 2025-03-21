package com.scaffoldcli.zapp.commands;

import com.scaffoldcli.zapp.lib.Text;

public class CreateScaffCommand implements Command {
    @Override
    public void run(String[] args) {
        Text.print("Creating you scaff\n", Text.Colour.yellow, true);
    }
}
