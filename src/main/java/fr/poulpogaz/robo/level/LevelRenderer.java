package fr.poulpogaz.robo.level;

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

                renderSprite(g2d, tile.getResourceLocation(), offsetX + x * TILE_SIZE, offsetY + y * TILE_SIZE);
            }
        }

        Robot robot = level.getRobot();
        Pos pos = robot.getPos();

        renderSprite(g2d, robot.getResourceLocation(levelFailed, levelFinished, robotRunning), offsetX + pos.getX() * TILE_SIZE, offsetY + pos.getY() * TILE_SIZE);
    }

    public void renderSprite(Graphics2D g2d, ResourceLocation resourceLocation, int x, int y) {
        BufferedImage image = textureManager.getTexture(resourceLocation);

        g2d.drawImage(image, x, y, null);
    }

    public static LevelRenderer getInstance() {
        return INSTANCE;
    }
}