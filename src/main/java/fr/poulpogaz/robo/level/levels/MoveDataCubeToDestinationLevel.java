package fr.poulpogaz.robo.level.levels;

import fr.poulpogaz.robo.level.CheckReport;
import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.level.Level;
import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.robot.Operation;
import fr.poulpogaz.robo.robot.Pos;

import java.util.List;

public class MoveDataCubeToDestinationLevel extends Level {

    public MoveDataCubeToDestinationLevel(int index, List<Class<? extends Operation>> operations) {
        super(index, operations);
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

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                DataCube cube = map.getDataCube(x, y);

                if (cube != null) {
                    Tile tile = map.get(x, y);

                    if (!tile.isDestination()) {
                        return CheckReport.OK;
                    }
                }
            }
        }

        return CheckReport.LEVEL_FINISHED;
    }
}