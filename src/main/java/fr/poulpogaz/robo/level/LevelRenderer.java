package fr.poulpogaz.robo.level;

import fr.poulpogaz.robo.gui.FontColor;
import fr.poulpogaz.robo.map.Map;
import fr.poulpogaz.robo.map.Tile;
import fr.poulpogaz.robo.map.Tiles;
import fr.poulpogaz.robo.robot.Pos;
import fr.poulpogaz.robo.robot.Robot;
import fr.poulpogaz.robo.utils.ResourceLocation;
import fr.poulpogaz.robo.utils.TextureManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import static fr.poulpogaz.robo.main.Robo.TILE_SIZE;

public class LevelRenderer {

    private static final LevelRenderer INSTANCE = new LevelRenderer();
    private static final TextureManager textureManager = TextureManager.getInstance();

    private LevelRenderer() {

    }

    public void init() {
        for (Tile tile : Tiles.getTiles()) {
            textureManager.loadTexture(tile.getResourceLocation());
        }
    }

    public void render(Graphics2D g2d, Level level, int width, int height, boolean levelFailed, boolean levelFinished, boolean robotRunning) {
        Map map = level.getMap();
        int lvlWidth = level.getWidth();
        int lvlHeight = level.getHeight();

        int offsetX = (width - lvlWidth * TILE_SIZE) / 2;
        int offsetY = (height - lvlHeight * TILE_SIZE) /2;

        for (int y = 0; y < lvlHeight; y++) {
            for (int x = 0; x < lvlWidth; x++) {
                Tile tile = map.get(x, y);

                int drawX = offsetX + x * TILE_SIZE;
                int drawY = offsetY + y * TILE_SIZE;

                renderSprite(g2d, tile.getResourceLocation(), drawX, drawY);

                DataCube cube = map.getDataCube(x, y);

                if (cube != null) {
                    renderDataCube(g2d, cube, drawX, drawY);
                }
            }
        }

        Robot robot = level.getRobot();
        Pos pos = robot.getPos();

        renderSprite(g2d, robot.getResourceLocation(levelFailed, levelFinished, robotRunning), offsetX + pos.getX() * TILE_SIZE, offsetY + pos.getY() * TILE_SIZE);

        if (robot.getDataCube() != null) {
            drawRobotDataCube(g2d, robot.getDataCube());
        }
    }

    private void drawRobotDataCube(Graphics2D g2d, DataCube dataCube) {
        int y = TILE_SIZE * 4;
        int size = TILE_SIZE + 8;

        g2d.setColor(new Color(25, 25, 25));
        g2d.fillRect(0, y, size, size);

        g2d.setColor(new Color(44, 44, 44));
        g2d.fillRect(3, y + 3, size - 6, size - 6);

        renderDataCube(g2d, dataCube, 4, y + 4);
    }

    private void renderSprite(Graphics2D g2d, ResourceLocation resourceLocation, int x, int y) {
        BufferedImage image = textureManager.getTexture(resourceLocation);

        g2d.drawImage(image, x, y, null);
    }

    private void renderDataCube(Graphics2D g2d, DataCube cube, int x, int y) {
        renderSprite(g2d, cube.getResourceLocation(), x, y);

        String text = String.valueOf(cube.getValue());
        FontMetrics fm = g2d.getFontMetrics();

        int textX = x + (TILE_SIZE - fm.stringWidth(text)) / 2;
        int textY = y + (TILE_SIZE - fm.getHeight()) / 2 + fm.getAscent();

        g2d.setColor(FontColor.FOREGROUND);
        g2d.drawString(text, textX, textY);
    }

    public static LevelRenderer getInstance() {
        return INSTANCE;
    }
}