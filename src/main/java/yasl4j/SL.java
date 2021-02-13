/*
 * Copyright (c) 2021 Yoshiyuki Karezaki(y.karezaki@gmail.com)
 * Released under the MIT license
 * https://github.com/ykare/yasl4j/blob/master/LICENSE.txt
 */
package yasl4j;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import yasl4j.sound.SoundPlayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static yasl4j.Constants.*;

public class SL {
    public static final int REFRESH_RATE_MILLS = 40;
    public static final String GOOD_BYE_MESSAGE = "Good bye (^^)/~~";
    Terminal terminal;
    Screen screen;
    TextGraphics textGraphics;
    int COLUMNS;
    int ROWS;
    boolean flyMode;
    boolean accidentMode;
    boolean playSound = true;
    String soundFile;
    SoundPlayer player;

    enum MODE {LOGO, D51, C51}

    MODE runMode = MODE.D51;

    static final String[][] SL = {
            {LOGO1, LOGO2, LOGO3, LOGO4, LWHL11, LWHL12, DELLN},
            {LOGO1, LOGO2, LOGO3, LOGO4, LWHL21, LWHL22, DELLN},
            {LOGO1, LOGO2, LOGO3, LOGO4, LWHL31, LWHL32, DELLN},
            {LOGO1, LOGO2, LOGO3, LOGO4, LWHL41, LWHL42, DELLN},
            {LOGO1, LOGO2, LOGO3, LOGO4, LWHL51, LWHL52, DELLN},
            {LOGO1, LOGO2, LOGO3, LOGO4, LWHL61, LWHL62, DELLN}
    };
    static final String[] SL_COAL = {
            LCOAL1, LCOAL2, LCOAL3, LCOAL4, LCOAL5, LCOAL6, DELLN
    };
    static final String[] SL_CAR = {
            LCAR1, LCAR2, LCAR3, LCAR4, LCAR5, LCAR6, DELLN
    };
    static final String[][] D51 = {
            {D51STR1, D51STR2, D51STR3, D51STR4, D51STR5, D51STR6, D51STR7, D51WHL11, D51WHL12, D51WHL13, D51DEL},
            {D51STR1, D51STR2, D51STR3, D51STR4, D51STR5, D51STR6, D51STR7, D51WHL21, D51WHL22, D51WHL23, D51DEL},
            {D51STR1, D51STR2, D51STR3, D51STR4, D51STR5, D51STR6, D51STR7, D51WHL31, D51WHL32, D51WHL33, D51DEL},
            {D51STR1, D51STR2, D51STR3, D51STR4, D51STR5, D51STR6, D51STR7, D51WHL41, D51WHL42, D51WHL43, D51DEL},
            {D51STR1, D51STR2, D51STR3, D51STR4, D51STR5, D51STR6, D51STR7, D51WHL51, D51WHL52, D51WHL53, D51DEL},
            {D51STR1, D51STR2, D51STR3, D51STR4, D51STR5, D51STR6, D51STR7, D51WHL61, D51WHL62, D51WHL63, D51DEL}
    };
    static final String[] D51_COAL = {
            COAL01, COAL02, COAL03, COAL04, COAL05, COAL06, COAL07, COAL08, COAL09, COAL10, COALDEL
    };
    static final String[][] C51 = {
            {C51STR1, C51STR2, C51STR3, C51STR4, C51STR5, C51STR6, C51STR7, C51WH11, C51WH12, C51WH13, C51WH14, C51DEL},
            {C51STR1, C51STR2, C51STR3, C51STR4, C51STR5, C51STR6, C51STR7, C51WH21, C51WH22, C51WH23, C51WH24, C51DEL},
            {C51STR1, C51STR2, C51STR3, C51STR4, C51STR5, C51STR6, C51STR7, C51WH31, C51WH32, C51WH33, C51WH34, C51DEL},
            {C51STR1, C51STR2, C51STR3, C51STR4, C51STR5, C51STR6, C51STR7, C51WH41, C51WH42, C51WH43, C51WH44, C51DEL},
            {C51STR1, C51STR2, C51STR3, C51STR4, C51STR5, C51STR6, C51STR7, C51WH51, C51WH52, C51WH53, C51WH54, C51DEL},
            {C51STR1, C51STR2, C51STR3, C51STR4, C51STR5, C51STR6, C51STR7, C51WH61, C51WH62, C51WH63, C51WH64, C51DEL}
    };
    static final String[] C51_COAL = {
            COALDEL, COAL01, COAL02, COAL03, COAL04, COAL05, COAL06, COAL07, COAL08, COAL09, COAL10, COALDEL
    };
    static final String[][] MAN = {
            {"", "(O)"}, {"Help!", "\\O/"}
    };
    static final String[][] SMOKE = {
            {"(   )", "(    )", "(    )", "(   )", "(  )", "(  )", "( )", "( )", "()", "()", "O", "O", "O", "O", "O", " "},
            {"(@@@)", "(@@@@)", "(@@@@)", "(@@@)", "(@@)", "(@@)", "(@)", "(@)", "@@", "@@", "@", "@", "@", "@", "@", " "}
    };
    static final int SMOKE_PATTERNS = SMOKE[0].length;
    static final String[] SMOKE_ERASER = {
            "     ", "      ", "      ", "     ", "    ", "    ", "   ", "   ", "  ", "  ", " ", " ", " ", " ", " ", " "
    };
    static final int[] SMOKE_DY = {2, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    static final int[] SMOKE_DX = {-2, -1, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3};

    public void initialize() throws IOException {
        if (playSound) {
            initializeSound();
        }

        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        TerminalSize size = screen.getTerminalSize();
        COLUMNS = size.getColumns();
        ROWS = size.getRows();

        textGraphics = screen.newTextGraphics();

        screen.startScreen();
        screen.clear();
    }

    private void initializeSound() {
        if (soundFile != null) {
            if (Files.exists(Paths.get(soundFile))) {
                if (soundFile.endsWith(".mp3")) {
                    player = new SoundPlayer(soundFile, SoundPlayer.MEDIA_TYPE.MP3, SoundPlayer.SOURCE_TYPE.FILE);
                } else if (soundFile.endsWith(".wav")) {
                    player = new SoundPlayer(soundFile, SoundPlayer.MEDIA_TYPE.WAV, SoundPlayer.SOURCE_TYPE.FILE);
                } else {
                    System.err.printf("%s is unsupported media type...", soundFile);
                }
            } else {
                System.err.printf("%s not exits, ignore...", soundFile);
            }
        } else {
            player = new SoundPlayer("/sl_sound.mp3", SoundPlayer.MEDIA_TYPE.MP3, SoundPlayer.SOURCE_TYPE.RESOURCE);
        }
    }

    public void terminate() throws IOException {
        screen.readInput();
        screen.stopScreen();
    }

    void putString(int x, int y, String str) {
        textGraphics.putString(x, y, str);
    }

    boolean printSL(int x) {
        int y, py1 = 0, py2 = 0, py3 = 0;

        if (x < -LOGOLENGTH) return false;
        y = ROWS / 2 - 3;

        if (flyMode) {
            y = (x / 6) + ROWS - (COLUMNS / 6) - LOGOHEIGHT;
            py1 = 2;
            py2 = 4;
            py3 = 6;
        }

        for (int i = 0; i <= LOGOHEIGHT; i++) {
            putString(x, y + i, SL[(LOGOLENGTH + x) / 3 % LOGOPATTERNS][i]);
            putString(x + 21, y + i + py1, SL_COAL[i]);
            putString(x + 42, y + i + py2, SL_CAR[i]);
            putString(x + 63, y + i + py3, SL_CAR[i]);
        }

        if (accidentMode) {
            addMan(y + 1, x + 14);
            addMan(y + 1 + py2, x + 45);
            addMan(y + 1 + py2, x + 53);
            addMan(y + 1 + py3, x + 66);
            addMan(y + 1 + py3, x + 74);
        }

        addSmoke(y - 1, x + LOGOFUNNEL);

        return true;
    }

    boolean printD51(int x) {
        int y, dy = 0;

        if (x < -D51LENGTH) return false;
        y = ROWS / 2 - 5;

        if (flyMode) {
            y = (x / 7) + ROWS - (COLUMNS / 7) - D51HEIGHT;
            dy = 1;
        }

        for (int i = 0; i <= D51HEIGHT; i++) {
            putString(x, y + i, D51[(D51LENGTH + x) % D51PATTERNS][i]);
            putString(x + 53, y + i + dy, D51_COAL[i]);
        }

        if (accidentMode) {
            addMan(y + 2, x + 43);
            addMan(y + 2, x + 47);
        }
        addSmoke(y - 1, x + D51FUNNEL);
        return true;
    }

    boolean printC51(int x) {
        int y, dy = 0;

        if (x < -C51LENGTH) return false;
        y = ROWS / 2 - 5;

        if (flyMode) {
            y = (x / 7) + ROWS - (COLUMNS / 7) - C51HEIGHT;
            dy = 1;
        }

        for (int i = 0; i <= C51HEIGHT; i++) {
            putString(x, y + i, C51[(C51LENGTH + x) % C51PATTERNS][i]);
            putString(x + 55, y + i + dy, C51_COAL[i]);
        }

        if (accidentMode) {
            addMan(y + 3, x + 45);
            addMan(y + 3, x + 49);
        }

        addSmoke(y - 1, x + C51FUNNEL);

        return true;
    }

    void addMan(int y, int x) {
        for (int i = 0; i < 2; i++) {
            putString(x, y + i, MAN[(LOGOLENGTH + x) / 12 % 2][i]);
        }
    }

    List<Smoke> smokes = new ArrayList<>();

    static class Smoke {
        int y, x;
        int pattern, kind;

        public Smoke(int y, int x, int pattern, int kind) {
            this.y = y;
            this.x = x;
            this.pattern = pattern;
            this.kind = kind;
        }
    }

    void addSmoke(int y, int x) {
        if (x % 4 == 0) {
            for (Smoke s : smokes
            ) {
                putString(s.x, s.y, SMOKE_ERASER[s.pattern]);
                s.y -= SMOKE_DY[s.pattern];
                s.x += SMOKE_DX[s.pattern];
                s.pattern += (s.pattern < SMOKE_PATTERNS - 1) ? 1 : 0;
                putString(s.x, s.y, SMOKE[s.kind][s.pattern]);
            }
            putString(x, y, SMOKE[smokes.size() % 2][0]);
            Smoke s = new Smoke(y, x, 0, smokes.size() % 2);
            smokes.add(s);
        }
    }

    void sleep() {
        try {
            Thread.sleep(REFRESH_RATE_MILLS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void run() throws IOException {
        if (playSound) {
            player.start();
        }

        for (int x = COLUMNS - 1; ; x--) {
            boolean isContinue;
            switch (runMode) {
                case LOGO:
                    isContinue = printSL(x);
                    break;
                case D51:
                    isContinue = printD51(x);
                    break;
                case C51:
                    isContinue = printC51(x);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + runMode);
            }
            if (!isContinue) break;
            screen.refresh();
            sleep();
        }
        putString((COLUMNS - GOOD_BYE_MESSAGE.length())/ 2 + 1, ROWS / 2 - 1, GOOD_BYE_MESSAGE);
        putString((COLUMNS - GOOD_BYE_MESSAGE.length())/ 2 + 3, ROWS / 2 + 2, "Hit any key");
        screen.refresh();

        if (playSound) {
            player.stop();
        }
    }

    void parseOptions(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String option = args[i];
            switch (option) {
                case "-a":
                    accidentMode = true;
                    break;
                case "-F":
                    flyMode = true;
                    break;
                case "-l":
                    runMode = MODE.LOGO;
                    break;
                case "-c":
                    runMode = MODE.C51;
                    break;
                case "-n":
                    playSound = false;
                    break;
                case "-s":
                    i++;
                    if (i < args.length) {
                        soundFile = args[i];
                    } else {
                        System.err.println("No audio file specified.");
                        usage();
                    }
                    break;
                case "-h":
                    usage();
                default:
                    break;
            }
        }
    }

    private void usage() {
        System.out.println("Usage: sl [options]");
        System.out.println("sl is a highly advanced animation program for curing your bad habit of mistyping.");
        System.out.println();
        System.out.println("-a              An accident is occurring. People cry for help.");
        System.out.println("-l              Little version.");
        System.out.println("-F              It flies like the galaxy express 999.");
        System.out.println("-c              C51 appears instead of D51.");
        System.out.println("-n              no sound.");
        System.out.println("-s audio_file   play specified audio file.");
        System.out.println();
        System.out.println("-h              display this message.");
        System.out.println();
        System.out.println("Original Copyright 1993,1998,2014 Toyoda Masashi (mtoyoda@acm.org)");
        System.out.println("Copyright 2021 Yoshiyuki Karezaki (y.karezaki@gmail.com)");
        System.exit(1);
    }

    public static void main(String[] args) throws IOException {
        SL sl = new SL();
        sl.parseOptions(args);

        sl.initialize();

        sl.run();

        sl.terminate();
    }
}
