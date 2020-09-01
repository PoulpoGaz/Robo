package fr.poulpogaz.robo.level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

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

        index = 0;
        currentLevel = levels.get(index);
    }

    public void initLevel() {
        currentLevel.init();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public static LevelManager getInstance() {
        return INSTANCE;
    }
}