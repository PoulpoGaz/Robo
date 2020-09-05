package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.map.Map;
import fr.poulpogaz.robo.map.Tile;

import java.util.Arrays;

public class Drop extends Operation {

    private static final String[] DIRECTIONS = new String[] {"up", "down", "left", "right", "here"};

    private String direction;

    public Drop(int lineNumber) {
        super("drop", lineNumber);
    }

    @Override
    public Report parse(Operation[] operations, String[] script, String[] self) {
        if (self.length != 2) {
            return new Report("The pick operation needs one argument (one of " + Arrays.toString(DIRECTIONS) + ")", lineNumber, Pick.class);
        }

        for (String direction : DIRECTIONS) {
            if (direction.equals(self[1])) {
                this.direction = self[1];

                return Report.TRUE;
            }
        }

        return new Report("Unknown arguments: " + self[1], lineNumber, Pick.class);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        Pos dropPos = new Pos(robot.getPos());

        switch (direction) {
            case "up" -> dropPos.y--;
            case "down" -> dropPos.y++;
            case "left" -> dropPos.x--;
            case "right" -> dropPos.x++;
        }

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