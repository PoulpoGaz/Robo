package fr.poulpogaz.robo.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.HashMap;

public class StateManager {

    private static final Logger LOGGER = LogManager.getLogger(StateManager.class);

    private final HashMap<Class<?>, State> STATE_MAP;

    private State currentState;

    private boolean exit = false;

    public StateManager() {
        STATE_MAP = new HashMap<>();
    }

    public void loadStates() {
        add(new MainMenu(), MainMenu.class);
        add(new GameState(), GameState.class);
    }

    public void update(float delta) {
        currentState.update(delta);
    }

    public void render(Graphics2D g) {
        currentState.render(g);
    }

    private <T extends State> void add(T state, Class<T> class_) {
        STATE_MAP.put(class_, state);
    }

    public State getState(Class<? extends State> class_) {
        return STATE_MAP.get(class_);
    }

    public void switchState(Class<?> class_) {
        State g = STATE_MAP.get(class_);

        if (g != null) {
            if (currentState != null) {
                currentState.hide();
            }
            currentState = g;
            currentState.show();

            LOGGER.info("Switching state to {}", class_);
        } else {
            LOGGER.info("Unknown state: {}", class_);
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