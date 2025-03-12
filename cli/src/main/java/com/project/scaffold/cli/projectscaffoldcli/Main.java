package com.project.scaffold.cli.projectscaffoldcli;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

enum ClippingMode {
    CLIP,
    WRAP
}

enum BlinkMode {
    TL, TR, BL, BR, CURSOR, HIDDEN
}

enum TerminalColor {
    RESET, GREY, RED, GREEN, YELLOW, BLUE, PINK, CYAN
}

abstract class Teller {
    protected static final Map<TerminalColor, String> COLORS = new HashMap<>();
    static {
        COLORS.put(TerminalColor.RESET, "\033[0m");
        COLORS.put(TerminalColor.GREY, "\033[90m");
        COLORS.put(TerminalColor.RED, "\033[91m");
        COLORS.put(TerminalColor.GREEN, "\033[92m");
        COLORS.put(TerminalColor.YELLOW, "\033[93m");
        COLORS.put(TerminalColor.BLUE, "\033[94m");
        COLORS.put(TerminalColor.PINK, "\033[95m");
        COLORS.put(TerminalColor.CYAN, "\033[96m");
    }

    protected double fps;
    protected String out;
    protected int[] dims;
    protected long frame;
    protected int[] cursor;
    protected boolean running;
    protected BlinkMode blinker;
    protected ClippingMode clipping;

    public Teller(double fps) {
        this.fps = fps;
        this.out = "";
        this.dims = new int[]{0, 0};
        this.frame = 0;
        this.cursor = new int[]{0, 0};
        this.running = false;
        this.blinker = BlinkMode.BR;
        this.clipping = ClippingMode.CLIP;
    }

    public void clip(ClippingMode mode) {
        this.clipping = mode;
    }

    public void go(int x, int y) {
        int safeX = Math.max(0, x) + 1;
        int safeY = Math.max(0, y) + 1;
        this.out += "\033[" + safeY + ";" + safeX + "H";
        this.cursor[0] = x;
        this.cursor[1] = y;
    }

    public void blink(BlinkMode mode) {
        this.blinker = mode;
    }

    public void ink(TerminalColor col) {
        this.out += COLORS.get(col);
    }

    public void write(String txt) {
        if (this.clipping == ClippingMode.CLIP) {
            int start = Math.max(0, -this.cursor[0]);
            int available = Math.max(0, this.dims[0] - this.cursor[0]);
            int end = Math.min(txt.length(), start + available);
            txt = (start >= txt.length() ? "" : txt.substring(start, end));
        }
        this.out += txt;
        this.cursor[0] += txt.length();
    }

    public void clear() {
        go(0, 0);
        this.out += "\033[2J";
    }

    public abstract void setup();
    public abstract void draw();

    public void pause() {
        this.running = false;
    }

    public void unpause() {
        this.running = true;
    }

    public void run() {
        this.running = true;
        this.frame = 0;
        System.out.print("\033[2J");
        try {
            setup();
            while (this.running) {
                this.dims = getTerminalSize();
                this.out = "\033[0;0H";
                draw();
                System.out.print(this.out);
                System.out.flush();
                Thread.sleep((long)(1000.0 / this.fps));
                this.frame++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.print(COLORS.get(TerminalColor.RESET));
            System.out.flush();
        }
    }

    protected int[] getTerminalSize() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty size </dev/tty"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String[] size = reader.readLine().split(" ");
            return new int[]{Integer.parseInt(size[1]), Integer.parseInt(size[0])};
        } catch (Exception e) {
            return new int[]{80, 24};
        }
    }
}

class CLI extends Teller {
    private String[] questions;
    private String[][] options;
    private Map<String, String> selectedOptions;
    private int currentQuestion;
    private int currentOption;

    public CLI(double fps, String[] questions, String[][] options) {
        super(fps);
        this.questions = questions;
        this.options = options;
        this.selectedOptions = new HashMap<>();
        this.currentQuestion = 0;
        this.currentOption = 0;
    }

    @Override
    public void setup() {
        clip(ClippingMode.CLIP);
        blink(BlinkMode.HIDDEN);
    }

    @Override
    public void draw() {
        clear();
        if (currentQuestion < questions.length) {
            go(0, 0);
            write(questions[currentQuestion]);
            for (int i = 0; i < options[currentQuestion].length; i++) {
                go(0, i + 1);
                if (i == currentOption) {
                    ink(TerminalColor.GREEN);
                    write("> " + options[currentQuestion][i]);
                    ink(TerminalColor.RESET);
                } else {
                    write("  " + options[currentQuestion][i]);
                }
            }
        } else {
            go(0, 0);
            write("Selected Options:");
            int line = 1;
            for (Map.Entry<String, String> entry : selectedOptions.entrySet()) {
                go(0, line++);
                write(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public void handleInput(int key) {
        try {
            if (key == 27 && System.in.available() > 0 && System.in.read() == 91) {
                switch (System.in.read()) {
                    case 65: // Up Arrow
                        currentOption = (currentOption - 1 + options[currentQuestion].length) % options[currentQuestion].length;
                        break;
                    case 66: // Down Arrow
                        currentOption = (currentOption + 1) % options[currentQuestion].length;
                        break;
                }
            } else if (key == 10 || key == 32) { // Enter or Space
                selectedOptions.put(questions[currentQuestion], options[currentQuestion][currentOption]);
                currentQuestion++;
                currentOption = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runCLI() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        run();
        while (currentQuestion < questions.length) {
            try {
                int key = reader.read();
                handleInput(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String[] questions = {
        };
        String[][] options = {
            {}
        };
        CLI cli = new CLI(12, questions, options);
        cli.runCLI();
    }
}



