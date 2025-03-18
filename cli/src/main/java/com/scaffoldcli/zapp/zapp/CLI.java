package com.scaffoldcli.zapp.zapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.component.message.ShellMessageBuilder;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.control.AppView;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.ProgressView;
import org.springframework.shell.component.view.control.ListView.ItemStyle;
import org.springframework.shell.component.view.control.ListView.ListViewOpenSelectedItemEvent;
import org.springframework.shell.component.view.control.ListView.ListViewSelectedItemChangedEvent;
import org.springframework.shell.component.view.control.ProgressView.ProgressViewItem;
import org.springframework.shell.component.view.control.ListView;
import org.springframework.shell.component.view.event.EventLoop;
import org.springframework.shell.component.view.event.KeyEvent;
import org.springframework.shell.geom.HorizontalAlign;

import com.scaffoldcli.zapp.zapp.lib.*;

import lombok.RequiredArgsConstructor;

// @ShellComponent
public class CLI {
    static final String ROOT_SCAFF = "00000000000000000000000000000000";
    public List<Map<String, String>> prompts;
    private final TerminalUIBuilder builder;
    private String selected = "";
    List<String> items = new ArrayList<String>();


    // Ref type helper for deep nested event generics
	private final static ParameterizedTypeReference<ListViewSelectedItemChangedEvent<String>> LISTVIEW_STRING_SELECT
		= new ParameterizedTypeReference<ListViewSelectedItemChangedEvent<String>>() {};
	private final static ParameterizedTypeReference<ListViewOpenSelectedItemEvent<String>> LISTVIEW_STRING_OPEN
		= new ParameterizedTypeReference<ListViewOpenSelectedItemEvent<String>>() {};

    public CLI(TerminalUIBuilder termUIBuilder) {
        this.builder = termUIBuilder;
	}

    // Return null if there are no options/we have reached the end
    Pair<List<String>, String> getOptions(String scaffId) {
        List<String> resItems = new ArrayList<String>();
        resItems.add("Item A " + Math.random());
        resItems.add("Item B");
        resItems.add("Item C");
        resItems.add("Item D");
        return new Pair<List<String>, String>(resItems,"");
    }

    // @ShellMethod(key = "init")
    void run() {
        //---------- Construct UI ----------//
        TerminalUI ui = builder.build();
        EventLoop eventLoop = ui.getEventLoop();

        // Fetch initial items
        Pair<List<String>, String> options = getOptions(ROOT_SCAFF);
        this.items = options.x;

        // Construct selection list
        ListView<String> list = new ListView<String>(items, ItemStyle.NOCHECK);
        list.setTitle("Select an option to scaffold");
        list.setShowBorder(true);
        ui.configure(list);

        // Construct progress bar
        ProgressView progress = new ProgressView(
            ProgressViewItem.ofText(10, HorizontalAlign.LEFT),
            ProgressViewItem.ofSpinner(3, HorizontalAlign.LEFT),
            ProgressViewItem.ofPercent(0, HorizontalAlign.RIGHT));
        ui.configure(progress);
        progress.start();

        // Construct app view
        AppView app = new AppView(list, progress, new BoxView());
        ui.configure(app);
        ui.setRoot(app, true);

        //---------- Setup event listeners ----------//
        // Handle quit
        eventLoop.keyEvents().subscribe(e -> {
            if (e.getPlainKey() == KeyEvent.Key.q) {
                eventLoop.dispatch(ShellMessageBuilder.ofInterrupt());
                System.out.println("Zapp terminated - No project created");
            }
        });

        // Handle list selection changed
		eventLoop.onDestroy(eventLoop.viewEvents(LISTVIEW_STRING_SELECT, list).subscribe(event -> {
            if (event.args().item() != null) { this.selected = event.args().item(); }
        }));

        // Handle item chosen - move to next question
		eventLoop.onDestroy(eventLoop.viewEvents(LISTVIEW_STRING_OPEN, list).subscribe(event -> {
            if (event.args().item() != null) {}

            //----- Fetch new items list -----//
            Pair<List<String>, String> opts = getOptions(ROOT_SCAFF);
            if (opts == null) {
                // We have reached the end, enter construction mode
                return;
            }
            this.items = opts.x;

            //----- Populate UI -----//
            list.setItems(items);
        }));
        // list.shortcut(KeyEvent.Key.Enter, () -> {});


        //---------- Run UI ----------//
        // view.setDrawFunction((screen, rect) -> {
        //     screen.writerBuilder()
        //             .build()
        //             .text("Hello World", rect, HorizontalAlign.CENTER, VerticalAlign.CENTER);
        //     return rect;
        // });
        ui.setFocus(list);
        ui.run();
    }
}