package fr.poulpogaz.robo.level.levels;

import fr.poulpogaz.json.utils.Pair;
import fr.poulpogaz.robo.level.*;
import fr.poulpogaz.robo.map.MapBuilder;
import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.robot.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Level8 extends Level {

    private ArrayList<Integer> values = new ArrayList<>();

    public Level8() {
        super(8, List.of(Move.class, GoTo.class, Label.class, Pick.class, Drop.class, IfZero.class, MathOperation.Sub.class, MathOperation.Add.class, CopyTo.class, CopyFrom.class));
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

        for (int y = 1; y < getHeight() - 1; y++) {
            DataCube a = map.getDataCube(1, y);
            DataCube b = map.getDataCube(2, y);

            if (a.getValue() != b.getValue()) {
                values.add(a.getValue());
                values.add(b.getValue());
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

        int nOk = 0;
        boolean hasEqualCouple = false;
        for (int y = 1; y < getHeight() - 1; y++) {
            DataCube a = map.getDataCube(1, y);
            DataCube b = map.getDataCube(2, y);

            if (a != null && b != null) {
                if (a.getValue() == b.getValue()) {
                    hasEqualCouple = true;
                } else {
                    int expectedValue = values.get(nOk * 2);
                    int expectedValue2 = values.get(nOk * 2 + 1);

                    if (expectedValue == a.getValue() && expectedValue2 == b.getValue()) {
                        nOk++;
                    } else {
                        return CheckReport.LEVEL_FAILED;
                    }
                }
            }
        }

        if (nOk * 2 != values.size()) {
            return CheckReport.LEVEL_FAILED;
        } else if (!hasEqualCouple && pos.y == getHeight() - 2) {
            return CheckReport.LEVEL_FINISHED;
        } else {
            return CheckReport.OK;
        }
    }

    private static class Generator extends DataCubeGenerator {

        private int nEqualCouple = 0;
        private int nValidTiles = 0;
        private int maxEqualCouple = 0;

        private ArrayList<Pair<DataCube, DataCube>> couples;

        @Override
        public void init() {
            nValidTiles = 0;
            nEqualCouple = 0;

            Tile[][] tiles = builder.getTiles();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (isValid(tiles[y][x])) {
                        nValidTiles++;
                    }
                }
            }

            nValidTiles /= 2;

            maxEqualCouple = nValidTiles / 2;

            generateCouples();
        }

        private void generateCouples() {
            couples = new ArrayList<>();

            for (int i = 0; i < nValidTiles; i++) {
                if (nValidTiles == 1 && nEqualCouple == 0) {
                    couples.add(generateEqualCouple());
                } else if (nEqualCouple == maxEqualCouple) {
                    couples.add(generateRandomCouple());
                } else {
                    boolean genZero = ThreadLocalRandom.current().nextBoolean();

                    if (genZero) {
                        nEqualCouple++;
                        couples.add(generateEqualCouple());
                    } else {
                        couples.add(generateRandomCouple());
                    }
                }
            }
        }

        private Pair<DataCube, DataCube> generateEqualCouple() {
            int v = ThreadLocalRandom.current().nextInt(-99, 100);

            return new Pair<>(DataCube.createDataCube(v), DataCube.createDataCube(v));
        }

        private Pair<DataCube, DataCube> generateRandomCouple() {
            DataCube a = DataCube.createRandomCube();
            DataCube b = DataCube.createRandomCube();

            return new Pair<>(a, b);
        }

        @Override
        public DataCube generate(int x, int y) {
            if (y > 0 && y < getHeight() - 1 && (x == 1 || x == 2)) {
                if (x == 1) {
                    return couples.get(y - 1).getLeft();
                } else {
                    return couples.get(y - 1).getRight();
                }
            }

            return null;
        }

        private boolean isValid(Tile tile) {
            return !tile.isSolid() && !tile.isVoid();
        }
    }
}