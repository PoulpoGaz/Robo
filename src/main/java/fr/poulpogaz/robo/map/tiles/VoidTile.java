package fr.poulpogaz.robo.map.tiles;

import fr.poulpogaz.robo.utils.ResourceLocation;
import fr.poulpogaz.robo.map.Tile;

public class VoidTile extends Tile {

    public VoidTile() {
        super(new ResourceLocation("void", ResourceLocation.TILE));
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}