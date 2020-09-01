package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.map.Map;

public class Label extends Operation {

    private String label;

    public Label(int lineNumber) {
        super("label", lineNumber);
    }

    @Override
    public Report parse(Operation[] operations, String[] script, String[] self) {
        if (self.length != 2) {
            return new Report("The label operation needs one argument", lineNumber, Label.class);
        }

        label = self[1];

        for (int i = 0; i < script.length; i++) {
            if (lineNumber == i) {
                continue;
            }

            String line = script[i];
            String[] parts = line.split(" ");

            if (parts.length == 2 && parts[0].equals("label") && parts[1].equals(label)) {
                return new Report("Duplicate label at lines " + (i + 1) + " and " + (lineNumber + 1), lineNumber, Label.class);
            }
        }

        return Report.TRUE;
    }

    @Override
    public int execute(Map map, Robot robot) {
        return lineNumber + 1;
    }

    public String getLabel() {
        return label;
    }
}