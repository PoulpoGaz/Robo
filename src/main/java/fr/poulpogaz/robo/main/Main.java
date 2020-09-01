package fr.poulpogaz.robo.main;

import fr.poulpogaz.robo.utils.Cache;
import fr.poulpogaz.robo.window.View;
import fr.poulpogaz.robo.window.Window;

public class Main {

    public static void main(String[] args) {
        Cache.setRoot(System.getenv("APPDATA") + "/Robo");

        View view = TheGreatMachine.getInstance();

        Window window = new Window(view);
        window.start();
    }
}