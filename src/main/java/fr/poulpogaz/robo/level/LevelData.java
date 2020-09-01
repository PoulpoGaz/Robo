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

import java.io.IOException;
import java.io.InputStream;

public class LevelData {

    protected MapBuilder mapBuilder;
    protected RobotBuilder robotBuilder;

    protected String description;

    public LevelData(int levelIndex) {
        mapBuilder = new MapBuilder();
        robotBuilder = new RobotBuilder();

        readData(ResourceLocation.createInputStreamStatic(Integer.toString(levelIndex), ResourceLocation.LEVEL));
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

            reader.endObject();
            reader.close();
        } catch (IOException | JsonException e) {
            e.printStackTrace();
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
}