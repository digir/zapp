package com.scaffoldcli.zapp.zapp.commands;

import com.scaffoldcli.zapp.zapp.UserProjectConfig.ProjectStructure;
import com.scaffoldcli.zapp.zapp.lib.Util.Pair;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.component.message.ShellMessageBuilder;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.control.AppView;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.ListView;
import org.springframework.shell.component.view.control.ListView.ItemStyle;
import org.springframework.shell.component.view.control.ListView.ListViewOpenSelectedItemEvent;
import org.springframework.shell.component.view.control.ListView.ListViewSelectedItemChangedEvent;
import org.springframework.shell.component.view.control.ProgressView;
import org.springframework.shell.component.view.control.ProgressView.ProgressViewItem;
import org.springframework.shell.component.view.event.EventLoop;
import org.springframework.shell.component.view.event.KeyEvent;
import org.springframework.shell.geom.HorizontalAlign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Init {
    static final String ROOT_SCAFF = "00000000000000000000000000000000";
    // Ref type helper for deep nested event generics
    private final static ParameterizedTypeReference<ListViewSelectedItemChangedEvent<String>> LISTVIEW_STRING_SELECT
        = new ParameterizedTypeReference<ListViewSelectedItemChangedEvent<String>>() {};
    private final static ParameterizedTypeReference<ListViewOpenSelectedItemEvent<String>> LISTVIEW_STRING_OPEN
        = new ParameterizedTypeReference<ListViewOpenSelectedItemEvent<String>>() {};

    private final TerminalUIBuilder terminalUIBuilder;
    private TerminalUI ui;
    private AppView app;
	private EventLoop eventLoop;

    public List<Map<String, String>> prompts;
    List<String> items = new ArrayList<String>();
    Map<String, String> itemToScaff;
    private String currentScaffId = "";
    private String projectName = "MyProj";

    public Init(TerminalUIBuilder termUIBuilder) {
        this.terminalUIBuilder = termUIBuilder;
    }

    // Load option values into this.items and this.itemToScaff
    // Return false if there are no options/we have reached the end
    boolean loadOptions(String scaffId) {
        // TODO: Fetch options from API
        this.items = new ArrayList<String>();
        this.itemToScaff = new HashMap<String, String>();

        Map<String, String> scaffIdAndOptions = ProjectStructure.getScaffOptions(scaffId);
        for (Map.Entry<String, String> entry : scaffIdAndOptions.entrySet()) {
            String cid = entry.getKey();
            String name = entry.getValue();

            this.items.add(String.format("%s: %s", name, ""));
            this.itemToScaff.put(name, cid);
        }

        if (this.items.size() == 0) {
            return false;
        }
        this.items.add("<HEAD>: Render at current scaff");
        this.itemToScaff.put("<HEAD>", scaffId);

        return true;
    }

    // This gets called at the end to generate the project
    boolean generateProjectFiles(String projectName, String scaffId) {
        // Fetch rendered project from API, then construct the file system
        ProjectStructure.executeFinalScaff(projectName, scaffId);
        return true;
    }

    // Extract item name and map to original scaff id
    // Returns (item name, scaff id)
    Pair<String, String> extractScaffIdFromItem(String item) {
        String name = item.split(":")[0].trim();
        return new Pair<String, String>(name, itemToScaff.get(name));
    }

    public void run() {
        //---------- Construct UI ----------//
        ui = terminalUIBuilder.build();
        eventLoop = ui.getEventLoop();

        // Fetch & load initial items
        this.currentScaffId = ROOT_SCAFF;
        loadOptions(ROOT_SCAFF);

        // Construct selection list
        ListView<String> list = new ListView<String>(items, ItemStyle.NOCHECK);
        list.setBorderPadding(1, 1, 1, 1);
        list.setTitle(" Select an option to scaffold ");
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
        app = new AppView(list, new BoxView(), new BoxView());
        ui.configure(app);

        //---------- Setup event listeners ----------//
        // Handle quit
		eventLoop.onDestroy(eventLoop.keyEvents()
			.doOnNext(m -> {
				if (m.getPlainKey() == KeyEvent.Key.q) {
                    eventLoop.dispatch(ShellMessageBuilder.ofInterrupt());
                    System.out.println("\n\n\t\u001B[94m> Zapp Init terminated - No project created\u001B[0m\n\n");
                    System.out.println("\u001B[?25h"); // Restore cursor visibility
                    System.out.flush();
                    System.exit(0);
				}
			})
			.subscribe());

        // Handle item chosen - move to next question
        eventLoop.onDestroy(eventLoop.viewEvents(LISTVIEW_STRING_OPEN, list).subscribe(event -> {
            String chosen = event.args().item();
            if (chosen == null) return;

            //----- Fetch new items list -----//
            String itemName = "";
            if (extractScaffIdFromItem(chosen) instanceof Pair(String x, String y)) {
                itemName = x;
                currentScaffId = y;
            }

            if (itemName == "<HEAD>" || !loadOptions(currentScaffId)) {
                // We have reached the end, enter construction mode for the current scaff
                generateProjectFiles(projectName, currentScaffId);
                return;
            }

            //----- Populate UI -----//
            list.setItems(items);
        }));
        // list.shortcut(KeyEvent.Key.Enter, () -> {});


        //---------- Run UI ----------//
        ui.setRoot(app, true);
        ui.setFocus(list);
        ui.run();
    }
}