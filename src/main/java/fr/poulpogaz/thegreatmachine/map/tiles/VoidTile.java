package fr.poulpogaz.thegreatmachine.map.tiles;

import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;
import fr.poulpogaz.thegreatmachine.map.Tile;

public class VoidTile extends Tile {

    public VoidTile() {
        super(new ResourceLocation("void", ResourceLocation.TILE));
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}