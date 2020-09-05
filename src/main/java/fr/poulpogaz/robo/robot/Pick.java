package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.map.Map;

import java.util.Arrays;

public class Pick extends Operation {

    private static final String[] DIRECTIONS = new String[] {"up", "down", "left", "right", "here"};

    private String direction;

    public Pick(int lineNumber) {
        super("pick", lineNumber);
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
        Pos pickPos = new Pos(robot.getPos());

        switch (direction) {
            case "up" -> pickPos.y--;
            case "down" -> pickPos.y++;
            case "left" -> pickPos.x--;
            case "right" -> pickPos.x++;
        }

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