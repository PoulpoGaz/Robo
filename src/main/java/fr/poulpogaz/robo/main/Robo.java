package fr.poulpogaz.robo.main;

import fr.poulpogaz.robo.states.MainMenu;
import fr.poulpogaz.robo.states.StateManager;
import fr.poulpogaz.robo.timeline.Timeline;
import fr.poulpogaz.robo.window.KeyHandler;
import fr.poulpogaz.robo.window.MouseHandler;
import fr.poulpogaz.robo.window.View;
import fr.poulpogaz.robo.window.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class Robo extends Canvas implements View {

    private static final Robo INSTANCE = new Robo();

    private static final Logger LOGGER = LogManager.getLogger(Robo.class);

    public static final int SCALE_FACTOR = 2;

    public static final int TILE_SIZE = 16;

    public static final int WIDTH = 512;
    public static final int HEIGHT = WIDTH * 3 / 5;

    public static final int WINDOW_WIDTH = WIDTH * SCALE_FACTOR;
    public static final int WINDOW_HEIGHT = HEIGHT * SCALE_FACTOR;

    public static final String NAME = "Robo";

    public static final int TPS = 30;

    private Window window;

    private MouseHandler mouse;
    private KeyHandler key;

    private final StateManager stateManager = StateManager.getInstance();

    private Font font;

    private Robo() {

    }

    @Override
    public void start(Window window) {
        this.window = window;

        mouse = window.getMouseHandler();
        key = window.getKeyHandler();
        key.addAlphabeticalKeys();
        key.addNumberKeys();
        key.addEditorKeys();
        key.addDirectionKeys();

        Timeline.getInstance().load();
        stateManager.loadStates();
        stateManager.switchState(MainMenu.class);
    }

    @Override
    public void update(float delta) {
        stateManager.update(delta);

        if (stateManager.requireExit()) {
            window.stop();
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (font == null) {
            font = new Font("Monospace", Font.PLAIN, 8);
        }

        g2d.setFont(font);
        stateManager.render(g2d);
    }

    @Override
    public void terminate() {
        Timeline.getInstance().save();
    }

    @Override
    public String getViewName() {
        return NAME;
    }

    @Override
    public int getViewWidth() {
        return WIDTH;
    }

    @Override
    public int getViewHeight() {
        return HEIGHT;
    }

    @Override
    public int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    @Override
    public int getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    @Override
    public Canvas geCanvas() {
        return this;
    }

    @Override
    public int getTPS() {
        return TPS;
    }

    public MouseHandler getMouseHandler() {
        return mouse;
    }

    public KeyHandler getKeyHandler() {
        return key;
    }

    public int getTicks() {
        return window.getTicks();
    }

    public static Robo getInstance() {
        return INSTANCE;
    }
}