package fr.poulpogaz.thegreatmachine.main;

import fr.poulpogaz.thegreatmachine.states.StateManager;
import fr.poulpogaz.thegreatmachine.utils.TextureManager;
import fr.poulpogaz.thegreatmachine.window.KeyHandler;
import fr.poulpogaz.thegreatmachine.window.MouseHandler;
import fr.poulpogaz.thegreatmachine.window.View;
import fr.poulpogaz.thegreatmachine.window.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;


public class TheGreatMachine extends Canvas implements View {

    private static final TheGreatMachine INSTANCE = new TheGreatMachine();

    private static final Logger LOGGER = LogManager.getLogger(TheGreatMachine.class);

    public static final int SCALE_FACTOR = 2;

    public static final int TILE_SIZE = 16;

    public static final int WIDTH = 512;
    public static final int HEIGHT = WIDTH * 3 / 5;

    public static final int WINDOW_WIDTH = WIDTH * SCALE_FACTOR;
    public static final int WINDOW_HEIGHT = HEIGHT * SCALE_FACTOR;

    public static final String NAME = "The great machine";

    public static final int TPS = 30;

    private Window window;

    private MouseHandler mouse;
    private KeyHandler key;

    private TextureManager textureManager;
    private StateManager stateManager;

    private Font font;

    private TheGreatMachine() {

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

        textureManager = new TextureManager();
        stateManager = new StateManager();
        stateManager.loadStates();
        stateManager.switchGUI("GameState");
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
            font = new Font("monospaced", Font.PLAIN, 8);
        }

        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        stateManager.render(g2d);
    }

    @Override
    public void terminate() {

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

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public int getTicks() {
        return window.getTicks();
    }

    public static TheGreatMachine getInstance() {
        return INSTANCE;
    }

    public StateManager getStateManager() {
        return stateManager;
    }
}