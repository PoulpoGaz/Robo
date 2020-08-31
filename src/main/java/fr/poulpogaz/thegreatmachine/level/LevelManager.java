package fr.poulpogaz.thegreatmachine.level;

import java.util.ArrayList;

public final class LevelManager {

    private static final LevelManager INSTANCE = new LevelManager();

    private ArrayList<Level> levels;

    private Level currentLevel;
    private int index;

    private LevelManager() {
        levels = new ArrayList<>();
    }

    public void loadLevels() {
        levels.add(new FirstLevel());

        index = 0;
        currentLevel = levels.get(index);
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public static LevelManager getInstance() {
        return INSTANCE;
    }
}