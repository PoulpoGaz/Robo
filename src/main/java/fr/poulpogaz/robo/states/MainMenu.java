package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.gui.FontColor;
import fr.poulpogaz.robo.gui.GUIBox;
import fr.poulpogaz.robo.gui.StringButton;
import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.timeline.Timeline;
import fr.poulpogaz.robo.utils.ResourceLocation;
import fr.poulpogaz.robo.utils.TextureManager;
import fr.poulpogaz.robo.window.KeyHandler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class MainMenu extends State {

    private static final TextureManager textureManager = TextureManager.getInstance();
    private static final Timeline timeline = Timeline.getInstance();
    private static final ResourceLocation BACKGROUND = new ResourceLocation("main_menu", ResourceLocation.TEXTURE);

    private static final KeyHandler key = Robo.getInstance().getKeyHandler();

    private static final String[] MENUS = new String[] {"New game", "Continue", "Exit"};
    private int index;

    private Font font;

    private GUIBox warning;

    private Boolean hasSave;

    public MainMenu() {
        warning = new GUIBox();
        warning.setTitle("** WARNING **");
        warning.setText("Are you sure you want to\ndelete you old game?");
        warning.addButton(new StringButton("Yes", () -> {
            warning.setVisible(false);
            switchToGame(true);
        }));

        warning.addButton(new StringButton("No", () -> warning.setVisible(false)));

        textureManager.loadTexture(BACKGROUND);
    }

    @Override
    public void show() {
        index = 0;
        hasSave = null;
        warning.setVisible(false);
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

        if (hasSave == null) {
            hasSave = timeline.hasSave();
        }

        drawMenu(g2d);

        warning.render(g2d);
    }

    private void drawMenu(Graphics2D g2d) {
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
        if (hasSave == null) {
            hasSave = timeline.hasSave();
        }

        if (!warning.isVisible()) {
            moveCursor();

            if (key.isKeyReleased(KeyEvent.VK_ENTER)) {
                switch (index) {
                    case 0 -> {
                        if (timeline.hasSave()) {
                            warning.setVisible(true);
                        } else {
                            switchToGame(true);
                        }
                    }
                    case 1 -> switchToGame(false);
                    case 2 -> manager.exit();
                }
            }
        } else {
            warning.update();
        }
    }

    private void switchToGame(boolean newGame) {
        if (newGame) {
            timeline.newGame();
        } else {
            timeline.loadGame();
        }

        manager.switchState(TimelineState.class);
    }

    private void moveCursor() {
        if (key.isKeyReleased(KeyEvent.VK_UP)) {
            index--;

            if (index < 0) {
                index = MENUS.length - 1;
            } else if (index == 1 && !hasSave) {
                index = 0;
            }
        }

        if (key.isKeyReleased(KeyEvent.VK_DOWN)) {
            index++;

            if (index >= MENUS.length) {
                index = 0;
            } else if (index == 1 && !hasSave) {
                index = 2;
            }
        }
    }
}