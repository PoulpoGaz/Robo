package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.map.Map;
import fr.poulpogaz.robo.robot.Robot;

public abstract class Level {

    private final int levelIndex;
    private LevelData data;
    private String script;

    protected Map map;
    protected Robot robot;

    public Level(int index) {
        this.levelIndex = index;

        data = new LevelData(index);
    }

    public void init() {
        map = data.buildMap();
        robot = data.buildRobot();
    }

    public void clear() {
        map = null;
        robot = null;
    }

    public abstract boolean check();

    public int getWidth() {
        return map.getWidth();
    }

    public int getHeight() {
        return map.getHeight();
    }

    public Map getMap() {
        return map;
    }

    public Robot getRobot() {
        return robot;
    }

    public String getDescription() {
        return data.getDescription();
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public int getIndex() {
        return levelIndex;
    }
}