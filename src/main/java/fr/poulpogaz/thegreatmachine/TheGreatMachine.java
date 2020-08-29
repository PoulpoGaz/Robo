package fr.poulpogaz.thegreatmachine;

import fr.poulpogaz.thegreatmachine.window.KeyHandler;
import fr.poulpogaz.thegreatmachine.window.MouseHandler;
import fr.poulpogaz.thegreatmachine.window.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class TheGreatMachine extends Canvas implements View {

    private static final Logger LOGGER = LogManager.getLogger(TheGreatMachine.class);

    public static final int WIDTH = 800;
    public static final int HEIGHT = WIDTH * 3 / 4;
    public static final String NAME = "The great machine";

    public static final int TPS = 30;

    private MouseHandler mouse;
    private KeyHandler key;

    public TheGreatMachine() {

    }

    @Override
    public void start(Window window) {
        mouse = window.getMouseHandler();
        key = window.getKeyHandler();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect(15, 15, WIDTH - 30, HEIGHT - 30);
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
    public Canvas geCanvas() {
        return this;
    }

    @Override
    public int getTPS() {
        return TPS;
    }
}