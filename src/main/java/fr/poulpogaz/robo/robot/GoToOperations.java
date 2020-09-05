package fr.poulpogaz.robo.robot;

public abstract class GoToOperations extends Operation {

    protected Label label;

    public GoToOperations(String name, int lineNumber) {
        super(name, lineNumber);
    }

    @Override
    public Report parse(Operation[] operations, String[] script, String[] self) {
        if (self.length != 2) {
            return new Report("The goto operation needs one argument", lineNumber, GoTo.class);
        }

        label = ScriptUtils.findLabel(operations, self[1]);

        if (label == null) {
            return new Report("Unknown label: " + self[1], lineNumber, GoTo.class);
        } else {
            return Report.TRUE;
        }
    }
}