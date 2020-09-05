package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.map.MapBuilder;

public class MapBuilderWithDataCubeGenerator extends MapBuilder {

    private DataCubeGenerator generator;

    public MapBuilderWithDataCubeGenerator() {

    }

    @Override
    protected DataCube[][] copyDataCubes() {
        generator.setWidth(getWidth());
        generator.setHeight(getHeight());
        generator.setBuilder(this);

        generator.init();

        DataCube[][] cubes = new DataCube[getHeight()][getWidth()];

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                cubes[y][x] = generator.generate(x, y);
            }
        }

        return cubes;
    }

    @Override
    public void setDataCubes(DataCube[][] dataCubes) {

    }

    public DataCubeGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(DataCubeGenerator generator) {
        this.generator = generator;
    }
}