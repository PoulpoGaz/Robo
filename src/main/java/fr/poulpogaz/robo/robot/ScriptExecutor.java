package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.map.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class ScriptExecutor {

    private static final ScriptExecutor INSTANCE = new ScriptExecutor();
    private static final Logger LOGGER = LogManager.getLogger(ScriptExecutor.class);

    private String[] lines;
    private int currentLine;

    private Operation[] operations;

    private ScriptExecutor() {

    }

    public Report parse(String script, List<Class<? extends Operation>> availableOperations) {
        LOGGER.info("Parsing script");

        currentLine = 0;

        long time = System.currentTimeMillis();

        this.lines = script.split("(?<=\n)");

        operations = new Operation[lines.length];

        createEmptyOperationsAndRemoveLineFeed();

        // first we parse labels
        Report report = parseLabels(availableOperations.contains(Label.class));
        if (report != Report.TRUE) {
            return report;
        }

        // then we parse other operations
        for (int i = 0, nLines = lines.length; i < nLines; i++) {
            String line = lines[i];

            String[] parts = line.split(" ");

            Operation operation;

            switch (parts[0]) {
                case "label", "" -> {
                    continue;
                }
                case "goto" -> operation = new GoTo(i);
                case "move" -> operation = new Move(i);
                case "pick" -> operation = new Pick(i);
                case "drop" -> operation = new Drop(i);
                default -> {
                    return unknownOperation(parts[0], i);
                }
            }

            if (!availableOperations.contains(operation.getClass())) {
                return unknownOperation(parts[0], i);
            }

            report = operation.parse(operations, lines, parts);

            if (report != Report.TRUE) {
                return report;
            }

            operations[i] = operation;
        }

        long time2 = System.currentTimeMillis();

        LOGGER.info("Script parsed in {}", time2 - time);

        return new Report(time2 - time);
    }

    private void createEmptyOperationsAndRemoveLineFeed() {
        for (int i = 0, nLines = lines.length; i < nLines; i++) {
            String line = lines[i];

            if (line.isEmpty() || line.equals("\n")) {
                operations[i] = new EmptyOperation(i);
            }

            lines[i] = line.replace("\n", "");
        }
    }

    private Report parseLabels(boolean allowLabel) {
        for (int i = 0, nLines = lines.length; i < nLines; i++) {
            String line = lines[i];

            String[] parts = line.split(" ");

            if (parts[0].equals("label")) {
                if (!allowLabel) {
                    return unknownOperation(parts[0], i);
                }

                Label label = new Label(i);
                Report report = label.parse(operations, lines, parts);

                if (report != Report.TRUE) {
                    return report;
                }

                operations[i] = label;
            }
        }

        return Report.TRUE;
    }

    private Report unknownOperation(String operation, int line) {
        return new Report("Unknown operation: \"" + operation + "\"", line, ScriptExecutor.class);
    }

    /**
     * @return true if the end is reached
     */
    public ExecuteReport executeOneLine(Map map, Robot robot) {
        if (currentLine == operations.length) {
            return new ExecuteReport(-1, null, true);
        }

        LOGGER.info("Executing line {}", currentLine);

        Operation operation;
        OperationReport report;

         while ((operation = operations[currentLine]) instanceof EmptyOperation) {
             currentLine = operation.execute(map, robot).getNextLine();
         }

         operation = operations[currentLine];
         report = operation.execute(map, robot);

        if (report.getError() != null) {
            return new ExecuteReport(report.getError(), operation);
        } else {
            int oldLine = currentLine;
            currentLine = report.getNextLine();

            return new ExecuteReport(oldLine, operation, false);
        }
    }

    public int getCurrentLine() {
        return currentLine;
    }

    static ScriptExecutor getInstance() {
        return INSTANCE;
    }
}