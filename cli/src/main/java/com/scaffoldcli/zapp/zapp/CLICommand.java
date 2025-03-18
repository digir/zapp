package com.scaffoldcli.zapp.zapp;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.standard.AbstractShellComponent;

// Much pain was suffered getting this bullshit to work
// For future reference, the holy comment which saved the day:
// [https://github.com/spring-projects/spring-shell/issues/1096#issuecomment-2210468543]

// Spring TUI docs here: [https://docs.spring.io/spring-shell/reference/tui/index.html]
// Reference repo here: [https://github.com/spring-projects/spring-shell]

@Command
public class CLICommand extends AbstractShellComponent {

	private final TerminalUIBuilder terminalUIBuilder;

	public CLICommand(TerminalUIBuilder terminalUIBuilder) {
		this.terminalUIBuilder = terminalUIBuilder;
	}

	@Command(command = "init")
	public void catalog() {
		CLI cli = new CLI(terminalUIBuilder);
		cli.run();
	}
}
