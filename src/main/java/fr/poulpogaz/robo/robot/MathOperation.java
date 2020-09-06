package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.map.Map;

import java.util.Arrays;
import java.util.function.BiFunction;

public abstract class MathOperation extends DirectionalOperation {

    public MathOperation(String name, int lineNumber) {
        super(name, lineNumber);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        Pos pos = getPos(robot);

        DataCube cube = robot.getDataCube();

        if (cube == null) {
            return new OperationReport("Robo doesn't hold a cube");
        } else {
            DataCube cube2 = map.getDataCube(pos);

            if (cube2 == null) {
                return new OperationReport("There is not cube at (" + pos.x + "; " + pos.y + ")");
            } else {
                int value = apply(cube.getValue(), cube2.getValue());

                cube.setValue(value);

                return new OperationReport(lineNumber + 1);
            }
        }
    }

    protected abstract int apply(int a, int b);

    public static class Add extends MathOperation {

        public Add(int lineNumber) {
            super("add", lineNumber);
        }

        protected int apply(int a, int b) {
            return a + b;
        }
    }

    public static class Sub extends MathOperation {

        public Sub(int lineNumber) {
            super("sub", lineNumber);
        }

        protected int apply(int a, int b) {
            return a - b;
        }
    }
}