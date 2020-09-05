package fr.poulpogaz.robo.map.tiles;

import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.utils.ResourceLocation;

public class DestinationTile extends Tile {

    public DestinationTile() {
        super(new ResourceLocation("destination", ResourceLocation.TILE));
    }

    @Override
    public boolean isDestination() {
        return true;
    }
}