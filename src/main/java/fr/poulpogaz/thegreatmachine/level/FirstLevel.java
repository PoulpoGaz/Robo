package fr.poulpogaz.thegreatmachine.level;

import fr.poulpogaz.thegreatmachine.map.Tile;
import fr.poulpogaz.thegreatmachine.robot.Pos;

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