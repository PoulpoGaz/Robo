package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.map.Map;
import fr.poulpogaz.robo.robot.Operation;
import fr.poulpogaz.robo.robot.Robot;
import fr.poulpogaz.robo.timeline.Node;

import java.util.List;

public abstract class Level implements Node {

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

    public abstract CheckReport check();

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

    public abstract List<Class<? extends Operation>> getAvailableOperations();

    public int getIndex() {
        return levelIndex;
    }

    @Override
    public int getType() {
        return LEVEL_NODE;
    }
}