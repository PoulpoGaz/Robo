package fr.poulpogaz.thegreatmachine.window;

public class Key {

    public int keyCode;
    public boolean press;
    public boolean release;

    public Key(int keyCode) {
        this.keyCode = keyCode;
        this.press = false;
        this.release = false;
    }

    public void update(boolean press) {
        if(press) {
            //Press
            this.press = true;
            release = false;
        } else {
            //Release
            this.press = false;
            release = true;
        }
    }
}