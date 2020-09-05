package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.gui.FontColor;
import fr.poulpogaz.robo.gui.TexturedButton;
import fr.poulpogaz.robo.level.Level;
import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.timeline.Node;
import fr.poulpogaz.robo.timeline.Timeline;
import fr.poulpogaz.robo.utils.ImageUtils;
import fr.poulpogaz.robo.utils.ResourceLocation;
import fr.poulpogaz.robo.utils.TextureManager;
import fr.poulpogaz.robo.window.MouseHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static fr.poulpogaz.robo.main.Robo.*;

public class TimelineState extends State {

    private static final MouseHandler mouse = Robo.getInstance().getMouseHandler();
    private static final TextureManager textureManager = TextureManager.getInstance();
    private static final Timeline timeline = Timeline.getInstance();

    private static final ResourceLocation STORY_ICON = new ResourceLocation("timeline_story", ResourceLocation.SPRITE);
    private static final ResourceLocation STORY_ICON_NOT_UNLOCKED = new ResourceLocation("timeline_story_not_unlocked", ResourceLocation.SPRITE);
    private static final ResourceLocation LEVEL_ICON = new ResourceLocation("timeline_level", ResourceLocation.SPRITE);
    private static final ResourceLocation LEVEL_ICON_NOT_UNLOCKED = new ResourceLocation("timeline_level_not_unlocked", ResourceLocation.SPRITE);

    private static final int LINE_LENGTH = 96;
    private static final int LINE_HEIGHT = 8;
    private static final int NODE_SIZE = 64;
    private static final int NODE_SIZE_HALF = NODE_SIZE / 2;

    private static final int MAX_OFFSET = -(NODE_SIZE + LINE_LENGTH - 2 * NODE_SIZE_HALF) * (timeline.getLength() - 1);

    private static final int X_START = (WIDTH - NODE_SIZE) / 2;
    private static final int Y_START = (HEIGHT - NODE_SIZE) / 2;

    private Font levelFont;
    private final TexturedButton backButton;

    private int xOffset = 0;

    public TimelineState() {
        BufferedImage storyIcon = textureManager.getTexture(STORY_ICON);
        BufferedImage levelIcon = textureManager.getTexture(LEVEL_ICON);

        BufferedImage dyedStoryIcon = ImageUtils.dye(storyIcon, new Color(45, 45, 45, 200));
        BufferedImage dyedLevelIcon = ImageUtils.dye(levelIcon, new Color(45, 45, 45, 200));

        textureManager.put(dyedStoryIcon, STORY_ICON_NOT_UNLOCKED);
        textureManager.put(dyedLevelIcon, LEVEL_ICON_NOT_UNLOCKED);


        backButton = new TexturedButton(new ResourceLocation("back", ResourceLocation.GUI_ELEMENT));
        backButton.setBounds(5, HEIGHT - TILE_SIZE - 5, TILE_SIZE, TILE_SIZE);
        backButton.setReleaseListener(this::back);
    }

    @Override
    public void show() {
        xOffset = 0;
    }

    @Override
    protected void renderBackground(Graphics2D g2d) {
        g2d.setColor(new Color(37, 49, 33));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
    }

    @Override
    protected void renderForeground(Graphics2D g2d) {
        int x = X_START + xOffset;
        int y = Y_START;
        int yLine = (HEIGHT - LINE_HEIGHT) / 2;

        ArrayList<Node> nodes = timeline.getNodes();
        for (int i = 0, nodesSize = nodes.size(); i < nodesSize; i++) {
            Node node = nodes.get(i);

            boolean unlocked = i <= timeline.getUnlockedNodes();

            if (i < nodesSize - 1) {
                if (i + 1 <= timeline.getUnlockedNodes()) {
                    g2d.setColor(new Color(46, 94, 50));
                } else {
                    g2d.setColor(new Color(38, 69, 41));
                }

                int x2 = x + NODE_SIZE_HALF;

                g2d.fillRect(x2, yLine, LINE_LENGTH, LINE_HEIGHT);
            }

            BufferedImage image = getTextureFor(node, unlocked);

            g2d.drawImage(image, x, y, null);

            if (node.getType() == Node.LEVEL_NODE) {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);

                if (unlocked) {
                    g2d.setColor(FontColor.FOREGROUND_DARK);
                } else {
                    g2d.setColor(FontColor.FOREGROUND_VERY_DARK);
                }

                if (levelFont == null) {
                    levelFont = g2d.getFont().deriveFont(26f);
                }

                Font old = g2d.getFont();
                g2d.setFont(levelFont);
                FontMetrics fm = g2d.getFontMetrics();

                Level level = (Level) node;
                String str = String.valueOf(level.getIndex());

                int x2 = x + NODE_SIZE_HALF - fm.stringWidth(str) / 2;
                int y2 = y + NODE_SIZE_HALF - fm.getHeight() / 2 + fm.getAscent();

                g2d.drawString(str, x2, y2);

                g2d.setFont(old);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            }

            x += LINE_LENGTH;

            if (x > WIDTH) {
                break;
            }
        }

        backButton.render(g2d);
    }

    private BufferedImage getTextureFor(Node node, boolean unlocked) {
        ResourceLocation location;

        if (node.getType() == Node.LEVEL_NODE) {
            location = unlocked ? LEVEL_ICON : LEVEL_ICON_NOT_UNLOCKED;
        } else {
            location = unlocked ? STORY_ICON : STORY_ICON_NOT_UNLOCKED;
        }

        return textureManager.getTexture(location);
    }

    @Override
    public void update(float delta) {
        xOffset = xOffset + 15 * mouse.wheel();

        if (xOffset > 0) {
            xOffset = 0;
        } else if (xOffset < MAX_OFFSET) {
            xOffset = MAX_OFFSET;
        }

        if (mouse.isMousePressed(MouseHandler.LEFT_BUTTON)) {
            int mX = mouse.getMouseX();
            int mY = mouse.getMouseY();

            if (mY > Y_START && mY < Y_START + NODE_SIZE) {

                int x = X_START + xOffset;
                ArrayList<Node> nodes = timeline.getNodes();
                for (int i = 0, nodesSize = nodes.size(); i < nodesSize; i++) {
                    if (x < mX && mX < x + NODE_SIZE) {
                        // touch
                        if (i <= timeline.getUnlockedNodes()) {
                            timeline.setCurrentNode(i);
                            timeline.swap();
                        }
                    }

                    x += LINE_LENGTH;
                }
            }
        }

        backButton.update();
    }

    private void back() {
        timeline.save();
        manager.switchState(MainMenu.class);
    }
}