package fr.poulpogaz.thegreatmachine.level;

import fr.poulpogaz.thegreatmachine.utils.TextureManager;
import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import fr.poulpogaz.thegreatmachine.utils.ISprite;
import fr.poulpogaz.thegreatmachine.map.Map;
import fr.poulpogaz.thegreatmachine.map.Tile;
import fr.poulpogaz.thegreatmachine.robot.Pos;
import fr.poulpogaz.thegreatmachine.robot.Robot;

import java.awt.*;
import java.awt.image.BufferedImage;

import static fr.poulpogaz.thegreatmachine.main.TheGreatMachine.TILE_SIZE;

public class LevelRenderer {

    private static final LevelRenderer INSTANCE = new LevelRenderer();
    private static final TextureManager textureManager = TheGreatMachine.getInstance().getTextureManager();

    private LevelRenderer() {

    }

    public void render(Graphics2D g2d, Level level, int width, int height) {
        Map map = level.getMap();
        int lvlWidth = level.getWidth();
        int lvlHeight = level.getHeight();

        int offsetX = (width - lvlWidth * TILE_SIZE) / 2;
        int offsetY = (height - lvlHeight * TILE_SIZE) /2;

        for (int y = 0; y < lvlHeight; y++) {
            for (int x = 0; x < lvlWidth; x++) {
                Tile tile = map.get(x, y);

                renderSprite(g2d, tile, offsetX + x * TILE_SIZE, offsetY + y * TILE_SIZE);
            }
        }

        Robot robot = level.getRobot();
        Pos pos = robot.getPos();

        renderSprite(g2d, robot, offsetX + pos.getX() * TILE_SIZE, offsetY + pos.getY() * TILE_SIZE);
    }

    public void renderSprite(Graphics2D g2d, ISprite sprite, int x, int y) {
        BufferedImage image = textureManager.getTexture(sprite.getResourceLocation());

        g2d.drawImage(image, x, y, null);
    }

    public static LevelRenderer getInstance() {
        return INSTANCE;
    }
}