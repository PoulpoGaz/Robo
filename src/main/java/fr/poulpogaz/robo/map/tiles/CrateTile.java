package fr.poulpogaz.robo.map.tiles;

import fr.poulpogaz.robo.utils.ResourceLocation;
import fr.poulpogaz.robo.map.Tile;

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