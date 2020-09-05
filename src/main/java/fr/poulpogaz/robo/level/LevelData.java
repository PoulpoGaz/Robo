package fr.poulpogaz.robo.level;

import fr.poulpogaz.json.IJsonReader;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.JsonReader;
import fr.poulpogaz.robo.map.Map;
import fr.poulpogaz.robo.map.MapBuilder;
import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.map.Tiles;
import fr.poulpogaz.robo.robot.Robot;
import fr.poulpogaz.robo.robot.RobotBuilder;
import fr.poulpogaz.robo.utils.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelData {

    private static final Logger LOGGER = LogManager.getLogger(LevelData.class);

    private final int index;
    private MapBuilder mapBuilder;
    private RobotBuilder robotBuilder;

    private String description;

    public LevelData(int levelIndex) {
        this.index = levelIndex;
        mapBuilder = createMapBuilder();
        robotBuilder = createRobotBuilder();

        readData(ResourceLocation.createInputStreamStatic(Integer.toString(levelIndex), ResourceLocation.LEVEL));
    }

    protected MapBuilder createMapBuilder() {
        return new MapBuilder();
    }

    protected RobotBuilder createRobotBuilder() {
        return new RobotBuilder();
    }

    public Map buildMap() {
        return mapBuilder.build();
    }

    public Robot buildRobot() {
        return robotBuilder.build();
    }

    public int getWidth() {
        return mapBuilder.getWidth();
    }

    public int getHeight() {
        return mapBuilder.getHeight();
    }

    public String getDescription() {
        return description;
    }

    private void readData(InputStream data) {
        try {
            IJsonReader reader = new JsonReader(data);

            reader.beginObject();

            mapBuilder.setWidth(reader.skipKey().nextInt());
            mapBuilder.setHeight(reader.skipKey().nextInt());

            reader.skipKey().beginArray();
            description = readDescription(reader);
            reader.endArray();

            reader.skipKey().beginArray();
            readRobot(reader);
            reader.endArray();

            reader.skipKey().beginArray();
            mapBuilder.setTiles(readTiles(reader));
            reader.endArray();

            reader.skipKey().beginArray();
            mapBuilder.setDataCubes(readDataCubes(reader));
            reader.endArray();

            reader.endObject();
            reader.close();
        } catch (IOException | JsonException e) {
            LOGGER.warn("Failed to read level {}", index, e);
        }
    }

    private String readDescription(IJsonReader reader) throws IOException, JsonException {
        StringBuilder builder = new StringBuilder();

        while (!reader.isArrayEnd()) {
            builder.append(reader.nextString()).append("\n");
        }

        return builder.toString();
    }

    private void readRobot(IJsonReader reader) throws IOException, JsonException {
        robotBuilder.setX(reader.nextInt());
        robotBuilder.setY(reader.nextInt());
    }

    private Tile[][] readTiles(IJsonReader reader) throws IOException, JsonException {
        Tile[][] tiles = new Tile[getHeight()][getWidth()];

        int x = 0;
        int y = 0;
        while (!reader.isArrayEnd()) {
            String tileResource = reader.nextString();

            tiles[y][x] = Tiles.of(tileResource);

            x++;

            if (x >= getWidth()) {
                x = 0;
                y++;
            }
        }

        return tiles;
    }

    private DataCube[][] readDataCubes(IJsonReader reader) throws IOException, JsonException {
        DataCube[][] dataCubes = new DataCube[getHeight()][getWidth()];

        while (!reader.isArrayEnd()) {

            reader.beginObject();

            int x = reader.skipKey().nextInt();
            int y = reader.skipKey().nextInt();

            reader.skipKey();

            if (reader.hasNextInt()) {
                dataCubes[y][x] = DataCube.createDataCube(reader.nextInt());
            } else {
                dataCubes[y][x] = DataCube.createRandomCube();
                reader.skipValue();
            }

            reader.endObject();
        }

        return dataCubes;
    }
}