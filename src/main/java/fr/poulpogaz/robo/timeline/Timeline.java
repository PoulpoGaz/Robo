package fr.poulpogaz.robo.timeline;

import fr.poulpogaz.json.*;
import fr.poulpogaz.robo.level.Level;
import fr.poulpogaz.robo.level.Levels;
import fr.poulpogaz.robo.states.GameState;
import fr.poulpogaz.robo.states.StateManager;
import fr.poulpogaz.robo.states.StoryState;
import fr.poulpogaz.robo.states.TimelineState;
import fr.poulpogaz.robo.utils.Cache;
import fr.poulpogaz.robo.utils.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;

public class Timeline {

    private static final Logger LOGGER = LogManager.getLogger(Timeline.class);
    private static final StateManager manager = StateManager.getInstance();

    private static final Timeline INSTANCE = new Timeline();
    private static final ArrayList<Node> nodes = new ArrayList<>();

    private Node currentNode;
    private int index = -1;
    private int unlockedNodes = 0;

    private Timeline() {

    }

    /**
     * INITIALISATION
     */
    public void load() {
        ArrayList<Level> levels = Levels.getLevels();
        ArrayList<StoryLog> storyLogs = loadStory();

        nodes.addAll(levels);

        int offset = 0;
        for (StoryLog log : storyLogs) {
            if (log.getBefore() == StoryLog.END_LOG) {
                nodes.add(log);
            } else {
                int i = offset + log.getBefore() - 1;

                if (i >= nodes.size()) {
                    nodes.add(log);
                } else {
                    nodes.add(i , log);
                }

                offset++;
            }
        }
    }

    private ArrayList<StoryLog> loadStory() {
        ArrayList<StoryLog> storyLogs = new ArrayList<>();

        try {
            InputStream stream = ResourceLocation.createInputStreamStatic("story", ResourceLocation.STORY);

            IJsonReader reader = new JsonReader(stream);

            reader.beginArray();

            while (!reader.isArrayEnd()) {
                reader.beginObject();
                storyLogs.add(StoryLog.deserialize(reader));
                reader.endObject();
            }

            reader.endArray();
            reader.close();
        } catch (IOException | JsonException e) {
            LOGGER.warn("Failed to load the story", e);
        }

        return storyLogs;
    }

    public void newGame() {
        LOGGER.info("Starting new game");
        deleteSave();

        index = 0;
        currentNode = nodes.get(index);
        unlockedNodes = 0;

        for (Node node : nodes) {
            if (node.getType() == Node.LEVEL_NODE) {
                Level level = (Level) node;
                level.setScript(null);
            }
        }
    }

    public void loadGame() {
        Path cache = Cache.of("save.json");

        if (Cache.exists(cache)) {
            LOGGER.info("Loading game");

            try {
                JsonReader reader = new JsonReader(Cache.newBufferedReader(cache));

                reader.beginObject();

                index = reader.skipKey().nextInt();
                unlockedNodes = reader.skipKey().nextInt();
                currentNode = nodes.get(index);

                reader.skipKey().beginArray(); // scripts

                while (!reader.isArrayEnd()) {
                    reader.beginObject();

                    int index = reader.skipKey().nextInt();

                    Level level = (Level) nodes.get(index);

                    reader.skipKey();
                    if (reader.hasNextNull()) {
                        reader.nextNull();
                        level.setScript(null);
                    } else {
                        level.setScript(reader.nextString());
                    }

                    reader.endObject();
                }

                reader.endArray();

                reader.endObject();
                reader.close();
            } catch (IOException | JsonException e) {
                LOGGER.warn("Failed to load save", e);
                newGame();
            }

        } else {
            newGame();
        }
    }

    public void save() {
        if (index >= 0) {
            Path save = Cache.of("save.json");

            LOGGER.info("Saving");
            try {
                IJsonWriter writer = new JsonPrettyWriter(Cache.newBufferedWriter(save));
                writer.beginObject();

                writer.field("index", index);
                writer.field("unlockedNodes", unlockedNodes);
                writer.key("script").beginArray();

                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);

                    if (node.getType() == Node.LEVEL_NODE) {
                        Level level = (Level) node;

                        writer.beginObject();

                        writer.field("index", i);

                        if (level.getScript() != null) {
                            writer.field("script", level.getScript());
                        } else {
                            writer.nullField("script");
                        }

                        writer.endObject();
                    }
                }

                writer.endArray();
                writer.endObject();
                writer.close();
            } catch (IOException | JsonException e) {
                LOGGER.warn("Failed to save", e);
            }
        }
    }

    private void deleteSave() {
        try {
            Cache.deleteIfExists(Cache.of("save.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasSave() {
        return Cache.exists(Cache.of("save.json"));
    }

    public void swap() {
        if (manager.getCurrentState() instanceof TimelineState) {
            Node node = currentNode;

            if (node.getType() == Node.LEVEL_NODE) {
                manager.switchState(GameState.class);
            } else {
                manager.switchState(StoryState.class);
            }
        } else {
            manager.switchState(TimelineState.class);
        }
    }

    public void nextNode() {
        index++;

        if (index >= nodes.size()) {
            index = nodes.size() - 1;
        }

        if (unlockedNodes < index) {
            unlockedNodes = index;
        }

        currentNode = nodes.get(index);
    }

    public void setCurrentNode(int index) {
        this.index = index;
        currentNode = nodes.get(index);
    }

    public int getUnlockedNodes() {
        return unlockedNodes;
    }

    public int getLength() {
        return nodes.size();
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public static Timeline getInstance() {
        return INSTANCE;
    }
}