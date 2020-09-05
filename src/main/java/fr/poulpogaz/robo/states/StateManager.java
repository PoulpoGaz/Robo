package fr.poulpogaz.robo.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.HashMap;

public class StateManager {

    private static final StateManager INSTANCE = new StateManager();

    private static final Logger LOGGER = LogManager.getLogger(StateManager.class);

    private final HashMap<Class<?>, State> STATE_MAP;

    private State currentState;

    private boolean exit = false;

    private StateManager() {
        STATE_MAP = new HashMap<>();
    }

    public void loadStates() {
        add(new MainMenu());
        add(new GameState());
        add(new StoryState());
        add(new TimelineState());
    }

    public void update(float delta) {
        currentState.update(delta);
    }

    public void render(Graphics2D g) {
        currentState.render(g);
    }

    private void add(State state) {
        STATE_MAP.put(state.getClass(), state);
    }

    public State getState(Class<? extends State> class_) {
        return STATE_MAP.get(class_);
    }

    public void switchState(Class<? extends State> class_) {
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

    public static StateManager getInstance() {
        return INSTANCE;
    }
}