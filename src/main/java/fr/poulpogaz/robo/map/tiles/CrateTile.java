package fr.poulpogaz.robo.map.tiles;

import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.utils.ResourceLocation;

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