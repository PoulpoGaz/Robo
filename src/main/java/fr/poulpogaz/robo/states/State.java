package fr.poulpogaz.robo.states;

import java.awt.*;

public abstract class State {

    protected final StateManager manager = StateManager.getInstance();

    public State() {

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
}