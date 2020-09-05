package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.map.Map;

public class GoTo extends GoToOperations {

    public GoTo(int lineNumber) {
        super("goto", lineNumber);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        return new OperationReport(label.getLineNumber());
    }
}