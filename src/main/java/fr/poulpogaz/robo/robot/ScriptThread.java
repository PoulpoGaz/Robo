package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.map.Map;

import java.util.List;
import java.util.concurrent.*;

public class ScriptThread {

    private static final ExecutorService executor;
    private static final ScriptExecutor scriptExecutor = ScriptExecutor.getInstance();

    public static Future<Report> parse(String script, List<Class<? extends Operation>> availableOperations) {
        return executor.submit(() -> scriptExecutor.parse(script, availableOperations));
    }

    public static ExecuteReport executeOneLine(Map map, Robot robot) {
        return scriptExecutor.executeOneLine(map, robot);
    }

    static {
        executor = Executors.newFixedThreadPool(1, new Factory());

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executor;
        threadPool.setKeepAliveTime(2, TimeUnit.SECONDS);
        threadPool.allowCoreThreadTimeOut(true);
    }

    private static class Factory implements ThreadFactory {

        private final ThreadGroup group;

        Factory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    "Script Thread",
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }

            return t;
        }
    }
}