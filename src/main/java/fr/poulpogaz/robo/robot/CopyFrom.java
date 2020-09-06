package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.map.Map;

public class CopyFrom extends DirectionalOperation {

    public CopyFrom(int lineNumber) {
        super("copyfrom", lineNumber);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        Pos pos = getPos(robot);

        DataCube cube = map.getDataCube(pos);

        if (cube == null) {
            return new OperationReport("There is not cube at (" + pos.x + "; " + pos.y + ")");
        } else {
            robot.setDataCube(cube.copy());

            return new OperationReport(lineNumber + 1);
        }
    }
}