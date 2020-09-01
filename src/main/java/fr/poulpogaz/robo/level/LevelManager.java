package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.utils.Cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LevelManager {

    private static final Logger LOGGER = LogManager.getLogger(LevelManager.class);

    private static final LevelManager INSTANCE = new LevelManager();

    private ArrayList<Level> levels;

    private Level currentLevel;
    private int index;

    private LevelManager() {
        levels = new ArrayList<>();
        loadLevels();
    }

    public void loadLevels() {
        levels.add(new FirstLevel());

        read();

        currentLevel = levels.get(index);
    }

    public void initLevel() {
        currentLevel.init();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    private void read() {
        Path save = Cache.of("save");

        if (Files.exists(save)) {

            readScripts();

            try {
                BufferedReader br = Cache.newBufferedReader(Cache.of("save"));
                index = Integer.parseInt(br.readLine());

                br.close();
            } catch (Exception e) {
                LOGGER.warn("Corrupted save", e);

                index = 0;
            }
        } else {
            index = 0;
        }
    }

    private void readScripts() {
        Path directory = Cache.of("scripts/");

        if (Cache.exists(directory)) {
            LOGGER.info("Reading scripts");

            try (Stream<Path> scripts = Cache.walk(directory)) {

                scripts.forEach((scriptPath) -> {
                    if (scriptPath.equals(directory)) {
                        return;
                    }

                    int index = Integer.parseInt(scriptPath.getFileName().toString()) - 1;
                    try {
                        BufferedReader br = Cache.newBufferedReader(scriptPath);

                        String script = br.lines().collect(Collectors.joining("\n"));

                        levels.get(index).setScript(script);

                        br.close();
                    } catch (IOException e) {
                        LOGGER.warn(e);
                    }
                });

            } catch (IOException e) {
                LOGGER.warn("Failed to read scripts", e);
            }
        }
    }

    public void save() {
        LOGGER.info("Saving");
        saveScripts();

        try {
            BufferedWriter bw = Cache.newBufferedWriter(Cache.of("save"));
            bw.write(Integer.toString(index));
            bw.close();
        } catch (IOException e) {
            LOGGER.warn(e);
        }
    }

    private void saveScripts() {
        for (Level level : levels) {
            if (level.getScript() != null) {

                try {
                    Path path = Cache.of("scripts/" + level.getIndex());

                    if (!Cache.exists(path.getParent())) {
                        Cache.createDirectories(path.getParent());
                    }

                    BufferedWriter bw = Cache.newBufferedWriter(path);
                    bw.write(level.getScript());
                    bw.close();
                } catch (IOException e) {
                    LOGGER.warn("Failed to save a script: " + level.getIndex(), e);
                }
            }
        }
    }

    public static LevelManager getInstance() {
        return INSTANCE;
    }
}