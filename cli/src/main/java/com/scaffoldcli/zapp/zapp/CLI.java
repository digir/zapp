package com.scaffoldcli.zapp.zapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.stringtemplate.v4.compiler.CodeGenerator.primary_return;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaffoldcli.zapp.zapp.UserProjectConfig.ProjectStructure;
import com.scaffoldcli.zapp.zapp.lib.Util.Pair;

// @ShellComponent
public class CLI {
    static final String ROOT_SCAFF = "00000000000000000000000000000000";
    public List<Map<String, String>> prompts;
    private final TerminalUIBuilder builder;
    private String currentScaffId = "";
    List<String> items = new ArrayList<String>();
    Map<String, String> itemToScaff;


    // Ref type helper for deep nested event generics
	private final static ParameterizedTypeReference<ListViewSelectedItemChangedEvent<String>> LISTVIEW_STRING_SELECT
		= new ParameterizedTypeReference<ListViewSelectedItemChangedEvent<String>>() {};
	private final static ParameterizedTypeReference<ListViewOpenSelectedItemEvent<String>> LISTVIEW_STRING_OPEN
		= new ParameterizedTypeReference<ListViewOpenSelectedItemEvent<String>>() {};

    public CLI(TerminalUIBuilder termUIBuilder) {
        this.builder = termUIBuilder;
	}

    // Load option values into this.items and this.itemToScaff
    // Return false if there are no options/we have reached the end
    boolean loadOptions(String scaffId) {
        // TODO: Fetch options from API
        this.items = new ArrayList<String>();
        this.itemToScaff = new HashMap<String, String>();

        // Scaff options
        List<String> childScaffIds = new ArrayList<String>();
        Map<String,String> scaffIdAndOptions = ProjectStructure.getScaffOptions(scaffId);
        childScaffIds.addAll(scaffIdAndOptions.keySet());

        for (String cid : childScaffIds) {
            // TODO: Present as <childscaffid>: <description>
            String itemName = cid.substring(0, 6);
            this.items.add(itemName + ": " + (int)(Math.random() * 1000));
            this.itemToScaff.put(itemName, cid);
        }
        if(this.items.size() == 0) { return false; }
        this.items.add("<HEAD>: Render at current scaff"); 
        this.itemToScaff.put("<HEAD>", scaffId);

        return true;
    }

    // This gets called at the end to generate the project
    boolean generateProjectFiles(String scaffId) {
        // TODO: Fetch rendered project from API, then construct the file system
        String scaff = ProjectStructure.getVarScaff(scaffId);
        updateScaff(scaff);

        ProjectStructure.createFilesFromJson(scaff);
        return true;
    }


    private static List<String> updateScaff(String jsonString){
        try {
            Files.write(Paths.get("ListVars.txt"), jsonString.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> varNames = new ArrayList<>();
        
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                if (key.equals("vars")){
                    Iterator<Map.Entry<String, JsonNode>> varFields = field.getValue().fields();
                    while (varFields.hasNext()) {
                        Map.Entry<String, JsonNode> varField = fields.next();
                        varNames.add(varField.getKey());              
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // try {
        //     Files.write(Paths.get("ListVars.txt"), jsonString.toString().getBytes());
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        return varNames;
    }

    // Extract item name and map to original scaff id
    // Returns (item name, scaff id)
    Pair<String, String> extractScaffIdFromItem(String item) {
        String name = item.split(":")[0].trim();
        return new Pair<String,String>(name, itemToScaff.get(name));
    }

    // @ShellMethod(key = "init")
    void run() {
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
		// eventLoop.onDestroy(eventLoop.viewEvents(LISTVIEW_STRING_SELECT, list).subscribe(event -> {
        //     if (event.args().item() != null) { String selected = event.args().item(); }
        // }));

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