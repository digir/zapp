package com.scaffoldcli.zapp.zapp;

import com.scaffoldcli.zapp.zapp.UserProjectConfig.ProjectStructure;
import com.scaffoldcli.zapp.zapp.lib.Util.Pair;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.component.message.ShellMessageBuilder;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.control.AppView;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.InputView;
import org.springframework.shell.component.view.control.ProgressView;
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

// @ShellComponent
public class CLI {
    static final String ROOT_SCAFF = "00000000000000000000000000000000";
    // public List<Map<String, String>> prompts;
    public List<Map<String, String>> prompts = new ArrayList<>();
    // Ref type helper for deep nested event generics
    private final static ParameterizedTypeReference<ListViewSelectedItemChangedEvent<String>> LISTVIEW_STRING_SELECT
            = new ParameterizedTypeReference<ListViewSelectedItemChangedEvent<String>>() {
    };
    private final static ParameterizedTypeReference<ListViewOpenSelectedItemEvent<String>> LISTVIEW_STRING_OPEN
            = new ParameterizedTypeReference<ListViewOpenSelectedItemEvent<String>>() {
    };
    private final TerminalUIBuilder builder;
    List<String> items = new ArrayList<String>();
    Map<String, String> itemToScaff;
    private String currentScaffId = "";

    public CLI(TerminalUIBuilder termUIBuilder) {
        this.builder = termUIBuilder;
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
    boolean generateProjectFiles(String scaffId) {
        // TODO: Fetch rendered project from API, then construct the file system
        ProjectStructure.executeFinalScaff(scaffId);
        return true;
    }

    // Extract item name and map to original scaff id
    // Returns (item name, scaff id)
    Pair<String, String> extractScaffIdFromItem(String item) {
        String name = item.split(":")[0].trim();
        return new Pair<String, String>(name, itemToScaff.get(name));
    }

    // Example prompt
    public void definePrompts() {
        Map<String, String> prompt1 = new HashMap<>();
        prompt1.put("variable", "projectName");
        prompt1.put("message", "Please enter the project name:");
        prompts.add(prompt1);}


        public Map<String, String> collectUserInput(TerminalUI ui) {
            Map<String, String> userInput = new HashMap<>();
            for (Map<String, String> prompt : prompts) {
                String variable = prompt.get("variable");
                String message = prompt.get("message");
        
                // Use InputView to collect user input
                InputView inputView = new InputView();
                ui.configure(inputView);
                ui.setFocus(inputView);
                String input = inputView.getInputText();
                userInput.put(variable, input);
            }
            return userInput;
        }

    public String replaceVariables(String code, Map<String, String> userInput) {
        for (Map.Entry<String, String> entry : userInput.entrySet()) {
            String variable = entry.getKey();
            String value = entry.getValue();
            code = code.replace("${" + variable + "}", value);
        }
        return code;
    }

    // @ShellMethod(key = "init")
    void run() {
        // Define prompts
        definePrompts();

        //---------- Construct UI ----------//
        TerminalUI ui = builder.build();
        EventLoop eventLoop = ui.getEventLoop();

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
                generateProjectFiles(currentScaffId);
                // Collect user input for variable substitution
            Map<String, String> userInput = collectUserInput(ui);

            // Example code with placeholders
            String code = "public class ${projectName} { }";

            // Replace variables in the code
            String finalCode = replaceVariables(code, userInput);

            // Print the final code (or use it as needed)
            System.out.println(finalCode);

            return;
        }

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