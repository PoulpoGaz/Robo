package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.level.levels.DestinationLevel;
import fr.poulpogaz.robo.level.levels.Level6;
import fr.poulpogaz.robo.level.levels.MoveDataCubeToDestinationLevel;
import fr.poulpogaz.robo.robot.*;

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
        levels.add(new MoveDataCubeToDestinationLevel(4, List.of(Move.class, GoTo.class, Label.class, Pick.class, Drop.class)));
        levels.add(new MoveDataCubeToDestinationLevel(5, List.of(Move.class, GoTo.class, Label.class, Pick.class, Drop.class)));
        levels.add(new Level6());

        return levels;
    }
}