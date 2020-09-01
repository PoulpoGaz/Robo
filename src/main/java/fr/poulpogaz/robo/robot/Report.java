package fr.poulpogaz.robo.robot;

public class Report {

    public static final Report TRUE = new Report();

    private final boolean success;
    private final String error;
    private final int line;
    private final Class<?> from;
    private final long time;

    private Report() {
        this.success = true;
        this.error = null;
        this.line = -1;
        this.from = Report.class;
        this.time = -1;
    }

    public Report(long time) {
        this.success = true;
        this.error = null;
        this.line = -1;
        this.from = Report.class;
        this.time = time;
    }

    public Report(String error, int line, Class<?> from) {
        this.success = false;
        this.error = error;
        this.line = line + 1;
        this.from = from;
        this.time = -1;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public int getLine() {
        return line;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("Report[success = %s, error = %s, line = %d, from = %s]", success, error, line, from.getSimpleName());
    }
}