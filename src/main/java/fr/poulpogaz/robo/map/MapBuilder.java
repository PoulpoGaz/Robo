package fr.poulpogaz.robo.map;

import java.util.Arrays;

public class MapBuilder {

    private Tile[][] tiles;
    private int width;
    private int height;

    public MapBuilder() {

    }

    public Map build() {
        Tile[][] copy = new Tile[height][width];

        for (int y = 0; y < height; y++) {
            copy[y] = Arrays.copyOf(tiles[y], width);
        }

        return new Map(copy, width, height);
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}