package fr.poulpogaz.robo.gui;

import fr.poulpogaz.robo.main.Robo;

import java.awt.*;

public class InfoGUI extends GuiElement {

    private static final String INFO_STRING = "** INFO **";

    private String text;
    private boolean isValid = false;

    public InfoGUI() {
        isVisible = false;
    }

    @Override
    protected void renderImpl(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();

        if (!isValid) {
            computeBounds(fm);

            isValid = true;
        }

        g2d.setColor(new Color(25, 25, 25));
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(new Color(44, 44, 44));
        g2d.fillRect(5, 5, width - 10, height - 10);

        g2d.setColor(FontColor.FOREGROUND);

        int yText = 20 - fm.getAscent();
        int x = (width - fm.stringWidth(INFO_STRING)) / 2;
        g2d.drawString(INFO_STRING, x, yText);

        String[] lines = text.split("\n");

        int fontHeight = fm.getHeight();
        for (String line : lines) {
            yText += fontHeight;

            g2d.drawString(line, 8, yText);
        }
    }

    @Override
    protected void updateImpl() {
        if (isValid) {
            if (released) {
                isVisible = false;
            }
        }
    }

    private void computeBounds(FontMetrics fm) {
        height = 20 + fm.getHeight(); // do not forget the info string
        width = 20 + fm.stringWidth(INFO_STRING); // do not forget the info string

        String[] lines = text.split("\n");

        for (String line : lines) {
            width = Math.max(width, fm.stringWidth(line) + 20);

            height += fm.getHeight();
        }

        x = (Robo.WIDTH - width) / 2;
        y = (Robo.HEIGHT - height) / 2;
    }

    public void setText(String text) {
        this.text = text;

        isValid = false;
    }
}