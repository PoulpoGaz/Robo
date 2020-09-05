package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.gui.FontColor;
import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.timeline.Node;
import fr.poulpogaz.robo.timeline.StoryLog;
import fr.poulpogaz.robo.timeline.Timeline;
import fr.poulpogaz.robo.window.KeyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

import static fr.poulpogaz.robo.main.Robo.HEIGHT;
import static fr.poulpogaz.robo.main.Robo.WIDTH;

public class StoryState extends State {

    private static final KeyHandler key = Robo.getInstance().getKeyHandler();

    private static final Logger LOGGER = LogManager.getLogger(StoryState.class);
    private static final Timeline timeline = Timeline.getInstance();

    private StoryLog storyLog;
    private boolean end;

    public StoryState() {

    }

    @Override
    public void show() {
        Node node = timeline.getCurrentNode();

        if (node.getType() != Node.STORY_NODE) {
            LOGGER.warn("Nesting problem");
        }

        storyLog = (StoryLog) node;

        end = false;
        storyLog.reset();
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        g2d.setColor(FontColor.FOREGROUND);

        FontMetrics fm = g2d.getFontMetrics();
        int fontHeight = fm.getHeight();

        int x = 10;
        int y = 10 + fm.getAscent();
        for (String line : storyLog.getVisibleScript()) {
            g2d.drawString(line, x, y);

            y += fontHeight;
        }

        if (end) {
            g2d.setColor(FontColor.FOREGROUND_VERY_DARK);
            g2d.drawString("Press <enter> to exit", x, y + fontHeight);
        }
    }

    @Override
    public void update(float delta) {
        if (!end) {
            if (storyLog.update()) {
                end = true;
            }
        } else {
            if (key.isKeyReleased(KeyEvent.VK_ENTER)) {
                timeline.nextNode();
                timeline.swap();
            }
        }
    }
}