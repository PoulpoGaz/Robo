package fr.poulpogaz.robo.window;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeyHandler extends KeyAdapter {

    private static final Logger LOGGER = LogManager.getLogger(KeyHandler.class);

    private final HashMap<Integer, Key> keys;

    public KeyHandler() {
        keys = new HashMap<>();
    }

    public void addAlphabeticalKeys() {
        for (int key = KeyEvent.VK_A; key <= KeyEvent.VK_Z; key++) {
            addKey(key);
        }
    }

    public void addNumberKeys() {
        for (int key = KeyEvent.VK_0; key <= KeyEvent.VK_9; key++) {
            addKey(key);
        }
    }

    public void addEditorKeys() {
        addKey(KeyEvent.VK_SHIFT);
        addKey(KeyEvent.VK_ENTER);
        addKey(KeyEvent.VK_SPACE);
        addKey(KeyEvent.VK_BACK_SPACE);
    }

    public void addDirectionKeys() {
        for (int key = KeyEvent.VK_LEFT; key <= KeyEvent.VK_DOWN; key++) {
            addKey(key);
        }
    }

    public void keyPressed(KeyEvent e) {
        if(keys.containsKey(e.getKeyCode())) {
            keys.get(e.getKeyCode()).update(true);
        }
    }

    public void keyReleased(KeyEvent e) {
        if(keys.containsKey(e.getKeyCode())) {
            keys.get(e.getKeyCode()).update(false);
        }
    }

    public void reset() {
        keys.forEach((k, v) -> {
            if(!v.press) {
                v.release = false;
            }
        });
    }

    public boolean isKeyPressed(int keyCode) {
        Key key = keys.get(keyCode);

        if(key != null) {
            return key.press;
        }

        LOGGER.warn("Key {} doesn't exist", keyCode);
        return false;
    }

    public boolean isKeyPressed(int keyCode, int tickPeriod) {
        Key key = keys.get(keyCode);

        if(key != null) {
            return key.press && key.pressedNTick % tickPeriod == 0;
        }

        LOGGER.warn("Key {} doesn't exist", keyCode);
        return false;
    }

    public boolean isKeyReleased(int keyCode) {
        Key key = keys.get(keyCode);

        if(key != null) {
            return key.release;
        }

        LOGGER.warn("Key {} doesn't exist", keyCode);
        return false;
    }

    public void addKey(int keyCode) {
        keys.put(keyCode, new Key(keyCode));
    }

    public void removeKey(int keyCode) {
        keys.remove(keyCode);
    }
}