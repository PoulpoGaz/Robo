package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.map.Map;

public class IfZero extends GoToOperations {

    public IfZero(int lineNumber) {
        super("ifzero", lineNumber);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        DataCube cube = robot.getDataCube();

        if (cube == null) {
            return new OperationReport("Robo doesn't hold a cube");
        }

        if (cube.getValue() == 0) {
            return new OperationReport(label.getLineNumber());
        } else {
            return new OperationReport(lineNumber + 1);
        }
    }
}