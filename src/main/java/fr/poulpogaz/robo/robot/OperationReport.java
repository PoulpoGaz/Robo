package fr.poulpogaz.robo.robot;

public class OperationReport {

    private final int nextLine;
    private final String error;

    public OperationReport(int nextLine) {
        this.nextLine = nextLine;
        this.error = null;
    }

    public OperationReport(String error) {
        this.nextLine = -1;
        this.error = error;
    }

    public int getNextLine() {
        return nextLine;
    }

    public String getError() {
        return error;
    }
}