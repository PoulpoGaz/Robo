package fr.poulpogaz.thegreatmachine.map.tiles;

import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;
import fr.poulpogaz.thegreatmachine.map.Tile;

public class DestinationTile extends Tile {

    public DestinationTile() {
        super(new ResourceLocation("destination", ResourceLocation.TILE));
    }

    @Override
    public boolean isDestination() {
        return true;
    }
}