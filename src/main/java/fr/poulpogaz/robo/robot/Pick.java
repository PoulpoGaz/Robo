package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.map.Map;

public class Pick extends DirectionalOperation {

    public Pick(int lineNumber) {
        super("pick", lineNumber);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        Pos pickPos = getPos(robot);

        DataCube cube = map.getDataCube(pickPos);

        if (cube == null) {
            return new OperationReport("There is not cube at (" + pickPos.x + "; " + pickPos.y + ")");
        } else {
            robot.setDataCube(cube);
            map.removeDataCube(pickPos);

            return new OperationReport(lineNumber + 1);
        }
    }
}