package fr.poulpogaz.thegreatmachine.robot;

import fr.poulpogaz.thegreatmachine.map.Map;

public abstract class Operation {

    private final String name;
    protected final int lineNumber;

    public Operation(String name, int lineNumber) {
        this.name = name;
        this.lineNumber = lineNumber;
    }

    public abstract Report parse(Operation[] operations, String[] script, String[] self);

    public abstract int execute(Map map, Robot robot);

    public String getName() {
        return name;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}