package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.robot.Operation;
import fr.poulpogaz.robo.robot.Pos;

import java.util.List;

public class DestinationLevel extends Level {

    private final List<Class<? extends Operation>> operations;

    public DestinationLevel(int index, List<Class<? extends Operation>> operations) {
        super(index);
        this.operations = operations;
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

    @Override
    public List<Class<? extends Operation>> getAvailableOperations() {
        return operations;
    }
}