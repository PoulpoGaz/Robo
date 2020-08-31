package fr.poulpogaz.thegreatmachine.level;

import fr.poulpogaz.json.IJsonReader;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.JsonReader;
import fr.poulpogaz.thegreatmachine.map.Map;
import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;
import fr.poulpogaz.thegreatmachine.map.Tile;
import fr.poulpogaz.thegreatmachine.map.Tiles;
import fr.poulpogaz.thegreatmachine.robot.Robot;

import java.io.IOException;
import java.io.InputStream;

import static fr.poulpogaz.thegreatmachine.main.TheGreatMachine.TILE_SIZE;

public abstract class Level {

    private final int levelIndex;

    protected Map map;
    protected int width;
    protected int height;
    protected String description;
    protected Robot robot;

    public Level(int levelIndex) {
        this.levelIndex = levelIndex;

        readData(ResourceLocation.createInputStreamStatic(Integer.toString(levelIndex), ResourceLocation.LEVEL));
    }

    public abstract boolean check();

    public Robot getRobot() {
        return robot;
    }

    public Map getMap() {
        return map;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDescription() {
        return description;
    }

    private void readData(InputStream data) {
        try {
            IJsonReader reader = new JsonReader(data);

            reader.beginObject();

            width = reader.skipKey().nextInt();
            height = reader.skipKey().nextInt();

            reader.skipKey().beginArray();
            description = readDescription(reader);
            reader.endArray();

            reader.skipKey().beginArray();
            robot = readRobot(reader);
            reader.endArray();

            reader.skipKey().beginArray();
            map = readMap(reader);
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

    private Robot readRobot(IJsonReader reader) throws IOException, JsonException {
        int x = reader.nextInt() * TILE_SIZE;
        int y = reader.nextInt() * TILE_SIZE;

        return new Robot(x, y);
    }

    private Map readMap(IJsonReader reader) throws IOException, JsonException {
        Map map = new Map(width, height);

        int x = 0;
        int y = 0;
        while (!reader.isArrayEnd()) {
            String tileResource = reader.nextString();

            Tile tile = Tiles.of(tileResource);
            map.put(x, y, tile);

            x++;

            if (x >= width) {
                x = 0;
                y++;
            }
        }

        return map;
    }
}