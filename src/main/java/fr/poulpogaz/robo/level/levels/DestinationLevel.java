package fr.poulpogaz.robo.level.levels;

import fr.poulpogaz.robo.level.CheckReport;
import fr.poulpogaz.robo.level.Level;
import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.robot.Operation;
import fr.poulpogaz.robo.robot.Pos;

import java.util.List;

public class DestinationLevel extends Level {

    public DestinationLevel(int index, List<Class<? extends Operation>> operations) {
        super(index, operations);
    }

    @Override
    public CheckReport check() {
        Pos pos = robot.getPos();

        Tile tile = map.get(pos);

        if (tile.isDestination()) {
            return CheckReport.LEVEL_FINISHED;
        } else if (tile.isVoid()) {
            return CheckReport.LEVEL_FAILED;
        } else {
            return CheckReport.OK;
        }
    }
}