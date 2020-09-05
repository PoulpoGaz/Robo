package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.map.Map;

public class GoTo extends Operation {

    private Label label;

    public GoTo(int lineNumber) {
        super("goto", lineNumber);
    }

    @Override
    public Report parse(Operation[] operations, String[] script, String[] self) {
        if (self.length != 2) {
            return new Report("The goto operation needs one argument", lineNumber, GoTo.class);
        }

        for (Operation operation : operations) {
            if (operation instanceof Label) {
                Label label = (Label) operation;

                String l = label.getLabel();

                if (l.equals(self[1])) {
                    this.label = label;

                    return Report.TRUE;
                }
            }
        }

        return new Report("Unknown label: " + self[1], lineNumber, GoTo.class);
    }

    @Override
    public OperationReport execute(Map map, Robot robot) {
        return new OperationReport(label.getLineNumber());
    }
}