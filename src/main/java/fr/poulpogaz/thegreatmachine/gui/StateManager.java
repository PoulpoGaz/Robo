package fr.poulpogaz.thegreatmachine.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.HashMap;

public class StateManager {

    private static final Logger LOGGER = LogManager.getLogger(StateManager.class);

    private final HashMap<String, State> GUI_MAP;

    private State currentState;

    private boolean exit = false;

    public StateManager() {
        GUI_MAP = new HashMap<>();
    }

    public void loadStates() {
        add(new GameState());
    }

    public void update(float delta) {
        currentState.update(delta);
    }

    public void render(Graphics2D g) {
        currentState.render(g);
    }

    private void add(State state) {
        GUI_MAP.put(state.getName(), state);
    }

    public State getGUI(String gui) {
        return GUI_MAP.get(gui);
    }

    public void switchGUI(String gui) {
        State g = GUI_MAP.get(gui);

        if (g != null) {
            if (currentState != null) {
                currentState.hide();
            }
            currentState = g;
            currentState.show();

            LOGGER.info("Switching state to {}", gui);
        } else {
            LOGGER.info("Unknown state: {}", gui);
        }
    }

    public void exit() {
        exit = true;
    }

    public State getCurrentState() {
        return currentState;
    }

    public boolean requireExit() {
        return exit;
    }
}