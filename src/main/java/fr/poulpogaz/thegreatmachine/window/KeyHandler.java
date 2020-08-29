package fr.poulpogaz.thegreatmachine.window;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeyHandler extends KeyAdapter {

    private final HashMap<Integer, Key> keys;

    public KeyHandler() {
        keys = new HashMap<>();
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

    public boolean getKeyPressed(int keyCode) {
        if(keys.containsKey(keyCode)) {
            return keys.get(keyCode).press;
        }
        System.err.println("Key " + keyCode + " doesn't exist");
        return false;
    }

    public boolean getKeyReleased(int keyCode) {
        if(keys.containsKey(keyCode)) {
            return keys.get(keyCode).release;
        }
        System.err.println("Key " + keyCode + " doesn't exist");
        return false;
    }

    public void addKey(int keyCode) {
        keys.put(keyCode, new Key(keyCode));
    }

    public void removeKey(int keyCode) {
        keys.remove(keyCode);
    }
}