package fr.poulpogaz.thegreatmachine.main;

import fr.poulpogaz.thegreatmachine.window.View;
import fr.poulpogaz.thegreatmachine.window.Window;

public class Main {

    public static void main(String[] args) {
        View view = TheGreatMachine.getInstance();

        Window window = new Window(view);
        window.start();
    }
}