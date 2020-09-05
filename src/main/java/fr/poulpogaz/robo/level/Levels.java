package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.robot.GoTo;
import fr.poulpogaz.robo.robot.Label;
import fr.poulpogaz.robo.robot.Move;

import java.util.ArrayList;
import java.util.List;

public class Levels {

    private static ArrayList<Level> levels;

    public static ArrayList<Level> getLevels() {
        if (levels == null) {
            levels = loadLevels();
        }

        return levels;
    }

    private static ArrayList<Level> loadLevels() {
        ArrayList<Level> levels = new ArrayList<>();

        levels.add(new DestinationLevel(1, List.of(Move.class)));
        levels.add(new DestinationLevel(2, List.of(Move.class)));
        levels.add(new DestinationLevel(3, List.of(Move.class, GoTo.class, Label.class)));

        return levels;
    }
}