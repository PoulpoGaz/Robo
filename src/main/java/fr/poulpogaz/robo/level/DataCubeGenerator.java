package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.map.MapBuilder;

public abstract class DataCubeGenerator {

    protected int width;
    protected int height;
    protected MapBuilder builder;

    public void init() {

    }

    public abstract DataCube generate(int x, int y);

    public MapBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(MapBuilder builder) {
        this.builder = builder;
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