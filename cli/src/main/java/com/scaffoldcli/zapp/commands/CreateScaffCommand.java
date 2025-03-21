package com.scaffoldcli.zapp.commands;

import com.scaffoldcli.zapp.lib.Text;

public class CreateScaffCommand implements Command {
    @Override
    public void run(String[] args) {
        String scaffName = Text.input("What do you want to call your scaff?");
        String scaffDescr = Text.input("Give you scaff a brief description.");
    }
}
