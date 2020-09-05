package fr.poulpogaz.robo.main;

import fr.poulpogaz.robo.utils.Cache;
import fr.poulpogaz.robo.window.View;
import fr.poulpogaz.robo.window.Window;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        Cache.setRoot(System.getenv("APPDATA") + "/Robo");

        View view = Robo.getInstance();

        Window window = new Window(view);
        window.start();
    }
}