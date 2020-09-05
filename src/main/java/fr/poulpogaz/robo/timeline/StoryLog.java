package fr.poulpogaz.robo.timeline;

import fr.poulpogaz.json.IJsonReader;
import fr.poulpogaz.json.JsonException;

import java.io.IOException;
import java.util.ArrayList;

public class StoryLog implements Node {

    public static final int END_LOG = -1;

    private final int before;
    private final String[] script;
    private final int[] sleeps;

    private String[] visibleScript;
    private int nLineVisible = 1;
    private int tickCount;

    private StoryLog(int before, String[] script, int[] sleeps) {
        this.before = before;
        this.script = script;
        this.sleeps = sleeps;
    }

    public void reset() {
        nLineVisible = 1;
        tickCount = 0;

        visibleScript = new String[nLineVisible];
        visibleScript[0] = script[0];
    }

    public boolean update() {
        tickCount++;

        if (tickCount == sleeps[nLineVisible - 1]) {
            tickCount = 0;
            nLineVisible++;

            visibleScript = new String[nLineVisible - 1];
            System.arraycopy(script, 0, visibleScript, 0, visibleScript.length);
        }

        return nLineVisible == script.length;
    }

    public int getBefore() {
        return before;
    }

    public String[] getVisibleScript() {
        return visibleScript;
    }

    public static StoryLog deserialize(IJsonReader reader) throws IOException, JsonException {
        int before = reader.skipKey().nextInt();

        reader.skipKey().beginArray();

        ArrayList<String> script = new ArrayList<>();
        ArrayList<Integer> sleep = new ArrayList<>();
        while (!reader.isArrayEnd()) {
            script.add(reader.nextString());
            sleep.add(reader.nextInt());
        }

        reader.endArray();

        int[] sleeps = new int[sleep.size()];
        for (int i = 0; i < sleeps.length; i++) {
            sleeps[i] = sleep.get(i);
        }

        return new StoryLog(before, script.toArray(new String[0]), sleeps);
    }

    @Override
    public int getType() {
        return STORY_NODE;
    }
}