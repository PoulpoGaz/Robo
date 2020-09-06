package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.map.Map;
import fr.poulpogaz.robo.map.Tile;

import java.util.Arrays;

public class Drop extends DirectionalOperation {

    public Drop(int lineNumber) {
        super("drop", lineNumber);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        Pos dropPos = getPos(robot);

        DataCube cube = robot.getDataCube();

        if (cube == null) {
            return new OperationReport("Robo doesn't hold a cube");
        } else {
            Tile tile = map.get(dropPos);

            if (tile.isVoid()) {
                robot.setDataCube(null);
            } else if (tile.isSolid()) {
                return new OperationReport("Cannot drop a cube at (" + dropPos.x + "; " + dropPos.y + ")");
            } else {
                robot.setDataCube(null);

                map.setDataCube(dropPos, cube);

            }

            return new OperationReport(lineNumber + 1);
        }
    }
}