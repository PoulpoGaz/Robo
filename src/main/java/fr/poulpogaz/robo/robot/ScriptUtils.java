package fr.poulpogaz.robo.robot;

public class ScriptUtils {

    public static Label findLabel(Operation[] operations, String label) {
        for (Operation operation : operations) {
            if (operation instanceof Label) {
                Label labelOperation = (Label) operation;

                String l = labelOperation.getLabel();

                if (l.equals(label)) {
                    return labelOperation;
                }
            }
        }

        return null;
    }
}