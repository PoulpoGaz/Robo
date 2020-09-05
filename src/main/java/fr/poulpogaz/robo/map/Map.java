package fr.poulpogaz.robo.map;

import fr.poulpogaz.robo.level.DataCube;
import fr.poulpogaz.robo.robot.Pos;

import java.util.Arrays;

public class Map {

    private final Tile[][] tiles;
    private final DataCube[][] dataCubes;
    private final int width;
    private final int height;

    public Map(Tile[][] tiles, DataCube[][] dataCubes, int width, int height) {
        this.tiles = tiles;
        this.dataCubes = dataCubes;
        this.width = width;
        this.height = height;
    }

    public boolean canMoveHere(Pos pos) {
        if (0 <= pos.x && pos.x < width && 0 <= pos.y && pos.y < height) {
            Tile tile = get(pos);

            return !tile.isSolid();
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

    public void removeDataCube(int x, int y) {
        dataCubes[y][x] = null;
    }

    public void removeDataCube(Pos pos) {
        dataCubes[pos.y][pos.x] = null;
    }

    public void setDataCube(Pos pos, DataCube cube) {
        dataCubes[pos.y][pos.x] = cube;
    }

    public DataCube getDataCube(int x, int y) {
        return dataCubes[y][x];
    }

    public DataCube getDataCube(Pos pos) {
        return dataCubes[pos.y][pos.x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}