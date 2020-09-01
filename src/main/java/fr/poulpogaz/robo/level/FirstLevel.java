package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.robot.Pos;

public class FirstLevel extends Level {

    public FirstLevel() {
        super(1);
    }

    @Override
    public boolean check() {
        Pos pos = robot.getPos();

        Tile tile = map.get(pos);

        return tile.isDestination();
    }
}