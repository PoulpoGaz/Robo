package fr.poulpogaz.robo.map;

import fr.poulpogaz.robo.level.DataCube;

import java.util.Arrays;

public class MapBuilder {

    private Tile[][] tiles;
    private DataCube[][] dataCubes;
    private int width;
    private int height;

    public MapBuilder() {

    }

    public Map build() {
        Tile[][] copy = new Tile[height][width];

        for (int y = 0; y < height; y++) {
            copy[y] = Arrays.copyOf(tiles[y], width);
        }

        DataCube[][] dataCubesCopy = new DataCube[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dataCubes[y][x] != null) {
                    dataCubesCopy[y][x] = new DataCube(dataCubes[y][x]);
                }
            }
        }

        return new Map(copy, dataCubesCopy, width, height);
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

    public void setDataCubes(DataCube[][] dataCubes) {
        this.dataCubes = dataCubes;
    }
}