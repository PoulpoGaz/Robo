package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.map.Map;

import java.util.Arrays;

public abstract class DirectionalOperation extends Operation {

    private static final String[] DIRECTIONS = new String[] {"up", "down", "left", "right", "here"};

    private String direction;

    public DirectionalOperation(String name, int lineNumber) {
        super(name, lineNumber);
    }

    @Override
    public Report parse(Operation[] operations, String[] script, String[] self) {
        if (self.length != 2) {
            return new Report("The pick operation needs one argument (one of " + Arrays.toString(DIRECTIONS) + ")", lineNumber, DirectionalOperation.class);
        }

        for (String direction : DIRECTIONS) {
            if (direction.equals(self[1])) {
                this.direction = self[1];

                return Report.TRUE;
            }
        }

        return new Report("Unknown arguments: " + self[1], lineNumber, DirectionalOperation.class);
    }

    protected Pos getPos(Robot robot) {
        Pos pos = new Pos(robot.getPos());

        switch (direction) {
            case "up" -> pos.y--;
            case "down" -> pos.y++;
            case "left" -> pos.x--;
            case "right" -> pos.x++;
        }

        return pos;
    }
}