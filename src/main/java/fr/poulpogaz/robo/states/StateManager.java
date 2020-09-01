package fr.poulpogaz.robo.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.HashMap;

public class StateManager {

    private static final Logger LOGGER = LogManager.getLogger(StateManager.class);

    private final HashMap<String, State> STATE_MAP;

    private State currentState;

    private boolean exit = false;

    public StateManager() {
        STATE_MAP = new HashMap<>();
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
        STATE_MAP.put(state.getName(), state);
    }

    public State getState(String state) {
        return STATE_MAP.get(state);
    }

    public void switchState(String state) {
        State g = STATE_MAP.get(state);

        if (g != null) {
            if (currentState != null) {
                currentState.hide();
            }
            currentState = g;
            currentState.show();

            LOGGER.info("Switching state to {}", state);
        } else {
            LOGGER.info("Unknown state: {}", state);
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