package fr.poulpogaz.robo.robot;

public class ExecuteReport {

    private final int line;
    private final Operation operation;
    private final boolean end;

    public ExecuteReport(int line, Operation operation, boolean end) {
        this.line = line;
        this.operation = operation;
        this.end = end;
    }

    public int getLine() {
        return line;
    }

    public Operation getOperation() {
        return operation;
    }

    public boolean isEnd() {
        return end;
    }
}