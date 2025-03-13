package com.scaffoldcli.zapp.zapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.shell.component.message.ShellMessageBuilder;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.control.AppView;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.GridView;
import org.springframework.shell.component.view.control.ListView.ItemStyle;
import org.springframework.shell.component.view.control.ListView;
import org.springframework.shell.component.view.event.EventLoop;
import org.springframework.shell.component.view.event.KeyEvent;

import lombok.RequiredArgsConstructor;

// @ShellComponent
public class CLI {
    private final TerminalUIBuilder builder;

	// @ShellMethod(key = "hello-world")
	// public String helloWorld( @ShellOption(defaultValue = "spring") String arg) {
	// 	return "Hello world " + arg;
	// }

    public CLI(TerminalUIBuilder termUIBuilder) {
        this.builder = termUIBuilder;
	}

    // @ShellMethod(key = "init")
    void run() {
        TerminalUI ui = builder.build();
        EventLoop eventLoop = ui.getEventLoop();

        List<String> items = new ArrayList<String>();
        items.add("Item A");
        items.add("Item B");
        items.add("Item C");
        items.add("Item D");
        ListView<String> list = new ListView<String>(items, ItemStyle.RADIO);
        ui.configure(list);


        // GridView grid = new GridView();
		// ui.configure(grid);
		// grid.setRowSize(0);
		// grid.setColumnSize(30, 0);

		// grid.addItem(list, 0, 0, 1, 1, 0, 0);
		// grid.addItem(scenarios, 0, 1, 1, 1, 0, 0);



        AppView app = new AppView(list, new BoxView(), new BoxView());
        ui.configure(app);

        ui.setRoot(app, true);
        // view.setDrawFunction((screen, rect) -> {
        //     screen.writerBuilder()
        //             .build()
        //             .text("Hello World", rect, HorizontalAlign.CENTER, VerticalAlign.CENTER);
        //     return rect;
        // });
        ui.setFocus(list);

        eventLoop.keyEvents()
            .subscribe(e -> {
                if (e.getPlainKey() == KeyEvent.Key.q) { eventLoop.dispatch(ShellMessageBuilder.ofInterrupt()); }
                if (e.getPlainKey() == KeyEvent.Key.e) { eventLoop.dispatch(ShellMessageBuilder.ofInterrupt()); }
            });
        ui.run();
    }
    
    
}