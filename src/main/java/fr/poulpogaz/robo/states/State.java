package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.gui.GUIBox;

import java.awt.*;
import java.util.ArrayList;

public abstract class State {

    protected final StateManager manager = StateManager.getInstance();

    protected ArrayList<GUIBox> boxes;

    public State() {
        boxes = new ArrayList<>();
    }

    public void show() {

    }

    public void render(Graphics2D g2d) {
        renderBackground(g2d);
        renderForeground(g2d);
    }

    protected void renderForeground(Graphics2D g2d) {
        boxes.forEach((b) -> b.render(g2d));
    }

    protected void renderBackground(Graphics2D g2d) {

    }

    public void update(float delta) {

    }

    protected boolean updateGUIBoxes() {
        for (GUIBox guiBox : boxes) {
            if (guiBox.isVisible()) {
                guiBox.update();

                return true;
            }
        }

        return false;
    }

    public void hide() {

    }
}