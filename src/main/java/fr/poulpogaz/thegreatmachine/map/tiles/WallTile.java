package fr.poulpogaz.thegreatmachine.map.tiles;

import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;
import fr.poulpogaz.thegreatmachine.map.Tile;

public class WallTile extends Tile {

    public WallTile() {
        super(new ResourceLocation("wall", ResourceLocation.TILE));
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}