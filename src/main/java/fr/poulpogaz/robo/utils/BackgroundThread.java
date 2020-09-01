package fr.poulpogaz.robo.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundThread {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void submit(Runnable runnable) {
        executor.submit(runnable);
    }
}