package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.map.Map;

import java.util.Arrays;

public class Move extends Operation {

    private static final String[] DIRECTIONS = new String[] {"up", "down", "left", "right"};

    private String direction;

    public Move(int lineNumber) {
        super("move", lineNumber);
    }

    @Override
    public Report parse(Operation[] operations, String[] script, String[] self) {
        if (self.length != 2) {
            return new Report("The move operation needs one argument (one of " + Arrays.toString(DIRECTIONS) + ")", lineNumber, Move.class);
        }

        for (String direction : DIRECTIONS) {
            if (direction.equals(self[1])) {
                this.direction = self[1];

                return Report.TRUE;
            }
        }

        return new Report("Unknown arguments: " + self[1], lineNumber, Move.class);
    }

    @Override
    public int execute(Map map, Robot robot) {
        Pos pos = new Pos(robot.getPos());
        switch (direction) {
            case "up" -> pos.y--;
            case "down" -> pos.y++;
            case "left" -> pos.x--;
            case "right" -> pos.x++;
        }

        if (map.canMoveHere(pos)) {
            robot.setPos(pos);
        }

        return lineNumber + 1;
    }
}