package fr.poulpogaz.thegreatmachine.map;

import fr.poulpogaz.thegreatmachine.robot.Pos;

public class Map {

    private final Tile[][] tiles;
    private final int width;
    private final int height;

    public Map(int width, int height) {
        tiles = new Tile[height][width];
        this.width = width;
        this.height = height;
    }

    public void put(int x, int y, Tile tile) {
        tiles[y][x] = tile;
    }

    public Tile get(int x, int y) {
        return tiles[y][x];
    }

    public Tile get(Pos pos) {
        return tiles[pos.getY()][pos.getX()];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}