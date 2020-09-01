package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.gui.FontColor;
import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.utils.ResourceLocation;
import fr.poulpogaz.robo.utils.TextureManager;
import fr.poulpogaz.robo.window.KeyHandler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class MainMenu extends State {

    private static final TextureManager textureManager = Robo.getInstance().getTextureManager();
    private static final ResourceLocation BACKGROUND = new ResourceLocation("main_menu", ResourceLocation.TEXTURE);

    private static final KeyHandler key = Robo.getInstance().getKeyHandler();

    private static final String[] MENUS = new String[] {"Play", "Continue", "Exit"};
    private int index;

    private Font font;

    public MainMenu() {
        super("MainMenu");
    }

    @Override
    public void show() {
        index = 0;
    }

    @Override
    protected void renderBackground(Graphics2D g2d) {
        BufferedImage image = textureManager.getTexture(BACKGROUND);

        g2d.drawImage(image, 0, 0, null);
    }

    @Override
    protected void renderForeground(Graphics2D g2d) {
        if (font == null) {
            font = g2d.getFont().deriveFont(16f);
        }

        Font old = g2d.getFont();
        g2d.setFont(font);

        int y = 150;

        FontMetrics fm = g2d.getFontMetrics();
        for (int i = 0; i < MENUS.length; i++) {
            String menu = MENUS[i];

            if (i == index) {
                g2d.setColor(FontColor.FOREGROUND);
            } else {
                g2d.setColor(FontColor.FOREGROUND_VERY_DARK);
            }

            int x = (Robo.WIDTH - fm.stringWidth(menu)) / 2;

            g2d.drawString(menu, x, y);

            y += 32;
        }

        g2d.setFont(old);
    }

    @Override
    public void update(float delta) {
        if (key.isKeyReleased(KeyEvent.VK_UP)) {
            index--;

            if (index < 0) {
                index = MENUS.length - 1;
            }
        }

        if (key.isKeyReleased(KeyEvent.VK_DOWN)) {
            index++;

            if (index >= MENUS.length) {
                index = 0;
            }
        }

        if (key.isKeyReleased(KeyEvent.VK_ENTER)) {
            switch (index) {
                case 0 -> manager.switchState(GameState.class);
                case 2 -> manager.exit();
            }
        }
    }
}