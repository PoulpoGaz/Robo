package fr.poulpogaz.thegreatmachine.robot;

import fr.poulpogaz.thegreatmachine.map.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ScriptExecutor {

    private static final ScriptExecutor INSTANCE = new ScriptExecutor();
    private static final Logger LOGGER = LogManager.getLogger(ScriptExecutor.class);

    private String script;
    private String[] lines;
    private int currentLine;

    private Operation[] operations;

    private ScriptExecutor() {

    }

    public Report parse(String script) {
        LOGGER.info("Parsing script");

        long time = System.currentTimeMillis();

        this.script = script;
        this.lines = script.split("(?<=\n)");

        operations = new Operation[lines.length];

        createEmptyOperationsAndRemoveLineFeed();

        // first we parse labels
        Report report = parseLabels();
        if (report != Report.TRUE) {
            return report;
        }

        // then we parse other operations
        for (int i = 0, nLines = lines.length; i < nLines; i++) {
            String line = lines[i];

            String[] parts = line.split(" ");

            Operation operation;

            switch (parts[0]) {
                case "label" -> {
                    continue;
                }
                case "goto" -> operation = new GoTo(i);
                case "move" -> operation = new Move(i);
                default -> {
                    return new Report("Unknown operation: \"" + parts[0] + "\"", i, ScriptExecutor.class);
                }
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
            } else {
                lines[i] = line.replace("\n", "");
            }
        }
    }

    private Report parseLabels() {
        for (int i = 0, nLines = lines.length; i < nLines; i++) {
            String line = lines[i];

            String[] parts = line.split(" ");

            if (parts[0].equals("label")) {
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

    /**
     * @return true if the end is reached
     */
    public ExecuteReport executeOneLine(Map map, Robot robot) {
        if (currentLine == operations.length) {
            return new ExecuteReport(-1, null, true);
        }

        LOGGER.info("Executing line {}", currentLine);

        Operation operation = operations[currentLine];

        int oldLine = currentLine;

        currentLine = operation.execute(map, robot);

        return new ExecuteReport(oldLine, operation, false);
    }

    public int getCurrentLine() {
        return currentLine;
    }

    static ScriptExecutor getInstance() {
        return INSTANCE;
    }
}