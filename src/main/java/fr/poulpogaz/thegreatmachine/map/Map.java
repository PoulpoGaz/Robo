package fr.poulpogaz.thegreatmachine.map;

import fr.poulpogaz.thegreatmachine.robot.Pos;
import fr.poulpogaz.thegreatmachine.robot.Robot;

public class Map implements Cloneable {

    private final Tile[][] tiles;
    private final int width;
    private final int height;

    public Map(int width, int height) {
        tiles = new Tile[height][width];
        this.width = width;
        this.height = height;
    }

    public boolean canMoveHere(Pos pos) {
        if (0 <= pos.x && pos.x < width && 0 <= pos.y && pos.y < height) {
            Tile tile = get(pos);

            return !tile.isVoid() && !tile.isSolid();
        }

        return false;
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

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}