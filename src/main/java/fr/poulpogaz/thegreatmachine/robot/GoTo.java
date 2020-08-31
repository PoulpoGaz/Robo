package fr.poulpogaz.thegreatmachine.robot;

import fr.poulpogaz.thegreatmachine.map.Map;

import javax.swing.*;

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
    public int execute(Map map, Robot robot) {
        return label.getLineNumber();
    }
}