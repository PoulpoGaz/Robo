package fr.poulpogaz.thegreatmachine.states;

import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import fr.poulpogaz.thegreatmachine.window.MouseHandler;

import java.awt.*;

public abstract class State {

    protected final MouseHandler mouse = TheGreatMachine.getInstance().getMouseHandler();
    protected final StateManager manager = TheGreatMachine.getInstance().getStateManager();

    private final String name;

    public State(String name) {
        this.name = name;
    }

    public void show() {

    }

    public void render(Graphics2D g2d) {
        renderBackground(g2d);
        renderForeground(g2d);
    }

    protected void renderForeground(Graphics2D g2d) {

    }

    protected void renderBackground(Graphics2D g2d) {

    }

    public void update(float delta) {

    }

    public void hide() {

    }

    public String getName() {
        return name;
    }
}