package fr.poulpogaz.robo.level.levels;

import fr.poulpogaz.robo.level.*;
import fr.poulpogaz.robo.map.MapBuilder;
import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.robot.*;

import java.util.ArrayList;
import java.util.List;

public class Level7 extends Level {

    private ArrayList<Integer> values = new ArrayList<>();

    public Level7() {
        super(7, List.of(Move.class, GoTo.class, Label.class, Pick.class, Drop.class, IfZero.class, CopyFrom.class, CopyTo.class));
    }

    @Override
    protected LevelData createLevelData(int index) {
        return new LevelData(index) {
            @Override
            protected MapBuilder createMapBuilder() {
                MapBuilderWithDataCubeGenerator builder = new MapBuilderWithDataCubeGenerator();
                builder.setGenerator(new Generator());
                return builder;
            }
        };
    }

    @Override
    public void init() {
        super.init();

        values.clear();

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                DataCube cube = map.getDataCube(x, y);

                if (cube != null) {
                    values.add(cube.getValue());
                }
            }
        }
    }

    @Override
    public CheckReport check() {
        Pos pos = robot.getPos();

        Tile playerTile = map.get(pos);

        if (playerTile.isVoid()) {
            return CheckReport.LEVEL_FAILED;
        }

        for (int y = 1; y < getHeight() - 1; y++) {
            DataCube a = map.getDataCube(1, y);
            DataCube b = map.getDataCube(2, y);

            if (a == null && b == null) {
                return CheckReport.LEVEL_FAILED;
            }

            if (a == null || b == null) {
                return CheckReport.OK;
            }

            if (a.getValue() != b.getValue()) {
                return CheckReport.OK;
            }

            if (a.getValue() != values.get(y - 1)) {
                return CheckReport.OK;
            }
        }

        return CheckReport.LEVEL_FINISHED;
    }

    private static class Generator extends DataCubeGenerator {

        @Override
        public void init() {

        }

        @Override
        public DataCube generate(int x, int y) {
            if (x == 1 && y > 0 && y < getHeight() - 1) {
                return DataCube.createRandomCube();
            }

            return null;
        }
    }
}