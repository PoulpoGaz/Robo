package fr.poulpogaz.thegreatmachine.robot;

import fr.poulpogaz.thegreatmachine.map.Map;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ScriptThread {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final ScriptExecutor scriptExecutor = ScriptExecutor.getInstance();

    public static Future<Report> parse(String script) {
        return executor.submit(() -> scriptExecutor.parse(script));
    }

    public static boolean executeOneLine(Map map, Robot robot) {
        return scriptExecutor.executeOneLine(map, robot);
    }
}