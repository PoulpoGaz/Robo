package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.map.Map;

public abstract class Operation {

    private final String name;
    protected final int lineNumber;

    public Operation(String name, int lineNumber) {
        this.name = name;
        this.lineNumber = lineNumber;
    }

    public abstract Report parse(Operation[] operations, String[] script, String[] self);

    public abstract OperationReport execute(Map map, Robot robot);

    public String getName() {
        return name;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}