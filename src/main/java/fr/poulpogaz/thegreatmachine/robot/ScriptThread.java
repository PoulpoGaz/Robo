package fr.poulpogaz.thegreatmachine.robot;

import fr.poulpogaz.thegreatmachine.map.Map;

import java.util.concurrent.*;

public class ScriptThread {

    private static final ExecutorService executor;
    private static final ScriptExecutor scriptExecutor = ScriptExecutor.getInstance();

    public static Future<Report> parse(String script) {
        return executor.submit(() -> scriptExecutor.parse(script));
    }

    public static boolean executeOneLine(Map map, Robot robot) {
        return scriptExecutor.executeOneLine(map, robot);
    }

    static {
        executor = Executors.newFixedThreadPool(1);

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executor;
        threadPool.setKeepAliveTime(2, TimeUnit.SECONDS);
        threadPool.allowCoreThreadTimeOut(true);
    }
}