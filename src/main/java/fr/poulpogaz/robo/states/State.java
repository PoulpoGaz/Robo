package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.window.MouseHandler;

import java.awt.*;

public abstract class State {

    protected final StateManager manager = Robo.getInstance().getStateManager();

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