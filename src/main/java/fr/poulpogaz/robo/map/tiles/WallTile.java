package fr.poulpogaz.robo.map.tiles;

import fr.poulpogaz.robo.utils.ResourceLocation;
import fr.poulpogaz.robo.map.Tile;

public class WallTile extends Tile {

    public WallTile() {
        super(new ResourceLocation("wall", ResourceLocation.TILE));
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}