package fr.poulpogaz.robo.level.levels;

import fr.poulpogaz.robo.level.*;
import fr.poulpogaz.robo.map.MapBuilder;
import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.robot.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Level6 extends Level {

    private ArrayList<DataCube> shouldRest = new ArrayList<>();

    public Level6() {
        super(6, List.of(Move.class, GoTo.class, Label.class, Pick.class, Drop.class, IfZero.class));
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

        shouldRest.clear();

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                DataCube cube = map.getDataCube(x, y);

                if (cube != null) {

                    if (cube.getValue() != 0) {
                        shouldRest.add(cube);
                    }
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
        if (robot.getDataCube() != null) {
            return CheckReport.OK;
        }


        int nOk = 0;
        boolean hasZero = false;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                DataCube cube = map.getDataCube(x, y);

                if (cube != null) {
                    if (cube.getValue() == 0) {
                        hasZero = true;
                    } else {
                        nOk++;
                    }
                }
            }
        }

        if (nOk != shouldRest.size()) {
            return CheckReport.LEVEL_FAILED;
        } else if (!hasZero && pos.y == getHeight() - 2) {
            return CheckReport.LEVEL_FINISHED;
        } else {
            return CheckReport.OK;
        }
    }

    private static class Generator extends DataCubeGenerator {

        private int nZero = 0;
        private int nValidTiles = 0;
        private int maxZero = 0;

        @Override
        public void init() {
            nValidTiles = 0;
            nZero = 0;

            Tile[][] tiles = builder.getTiles();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (isValid(tiles[y][x])) {
                        nValidTiles++;
                    }
                }
            }

            maxZero = nValidTiles / 2;
        }

        @Override
        public DataCube generate(int x, int y) {
            Tile[][] tiles = builder.getTiles();

            Tile tile = tiles[y][x];

            if (isValid(tile)) {
                if (nValidTiles == 1 && nZero == 0) {
                    return DataCube.createDataCube(0);
                } else if (nZero == maxZero) {
                    return DataCube.createRandomCube(); // can create a zero but anyway
                } else {
                    boolean genZero = ThreadLocalRandom.current().nextBoolean();

                    if (genZero) {
                        nZero++;
                        return DataCube.createDataCube(0);
                    } else {
                        return DataCube.createRandomCube();
                    }
                }
            }

            return null;
        }

        private boolean isValid(Tile tile) {
            return !tile.isSolid() && !tile.isVoid();
        }
    }
}