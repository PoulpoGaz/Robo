package fr.poulpogaz.thegreatmachine.map;

import fr.poulpogaz.thegreatmachine.map.tiles.CrateTile;
import fr.poulpogaz.thegreatmachine.map.tiles.DestinationTile;
import fr.poulpogaz.thegreatmachine.map.tiles.VoidTile;
import fr.poulpogaz.thegreatmachine.map.tiles.WallTile;
import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;

import java.util.HashMap;

import static fr.poulpogaz.thegreatmachine.utils.ResourceLocation.TILE;

public class Tiles {

    private static final HashMap<ResourceLocation, Tile> REGISTRY = new HashMap<>();

    public static final Tile FLOOR = register(new Tile(new ResourceLocation("floor", TILE)));
    public static final VoidTile VOID = register(new VoidTile());
    public static final CrateTile CRATE = register(new CrateTile());
    public static final WallTile WALL = register(new WallTile());
    public static final DestinationTile DESTINATION = register(new DestinationTile());

    private static <T extends Tile> T register(T tile) {
        REGISTRY.put(tile.getResourceLocation(), tile);

        return tile;
    }

    public static Tile of(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(name);

        return REGISTRY.get(resourceLocation);
    }
}