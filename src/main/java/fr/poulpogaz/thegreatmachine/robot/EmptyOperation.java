package fr.poulpogaz.thegreatmachine.robot;

import fr.poulpogaz.thegreatmachine.map.Map;

public class EmptyOperation extends Operation {

    public EmptyOperation(int lineNumber) {
        super(null, lineNumber);
    }

    @Override
    public Report parse(Operation[] operations, String[] script, String[] self) {
        return Report.TRUE;
    }

    @Override
    public int execute(Map map, Robot robot) {
        return lineNumber + 1;
    }
}