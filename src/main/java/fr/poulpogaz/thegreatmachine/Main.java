package fr.poulpogaz.thegreatmachine;

import fr.poulpogaz.thegreatmachine.window.Window;

public class Main {

    public static void main(String[] args) {
        View view = new TheGreatMachine();

        Window window = new Window(view);
        window.start();
    }
}