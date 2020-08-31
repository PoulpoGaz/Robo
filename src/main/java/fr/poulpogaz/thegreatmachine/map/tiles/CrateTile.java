package fr.poulpogaz.thegreatmachine.map.tiles;

import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;
import fr.poulpogaz.thegreatmachine.map.Tile;

public class CrateTile extends Tile {

    public CrateTile() {
        super(new ResourceLocation("crate", ResourceLocation.TILE));
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}