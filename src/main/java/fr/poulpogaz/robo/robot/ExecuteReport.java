package fr.poulpogaz.robo.robot;

public class ExecuteReport {

    private final int line;
    private final Operation operation;
    private final boolean end;
    private final String error;

    public ExecuteReport(int line, Operation operation, boolean end) {
        this.line = line;
        this.operation = operation;
        this.end = end;
        this.error = null;
    }

    public ExecuteReport(String error, Operation operation) {
        this.error = error;
        this.line = -1;
        this.operation = operation;
        this.end = false;
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

    public String getError() {
        return error;
    }
}