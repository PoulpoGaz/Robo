package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.map.Map;
import fr.poulpogaz.robo.map.Tile;

public class CopyTo extends DirectionalOperation {

    public CopyTo(int lineNumber) {
        super("copyto", lineNumber);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        Pos pos = getPos(robot);

        DataCube cube = robot.getDataCube();

        if (cube == null) {
            return new OperationReport("Robo doesn't hold a cube");
        } else {
            Tile tile = map.get(pos);

            if (!tile.isVoid()) {
                if (tile.isSolid()) {
                    return new OperationReport("Cannot copy a cube to (" + pos.x + "; " + pos.y + ")");
                } else {
                    map.setDataCube(pos, cube.copy());
                }
            }

            return new OperationReport(lineNumber + 1);
        }
    }
}