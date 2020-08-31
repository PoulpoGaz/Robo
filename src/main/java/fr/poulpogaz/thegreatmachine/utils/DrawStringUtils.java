package fr.poulpogaz.thegreatmachine.utils;

import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;

import java.awt.*;

public class DrawStringUtils {

    public static void drawStringCenteredWithLeftAlign(Graphics2D g2d, String str, int minX, int y) {
        String[] str_ = str.split("\n");

        FontMetrics fm = g2d.getFontMetrics();

        int h = fm.getHeight();
        for (String s: str_) {
            g2d.drawString(s, minX, y);

            y += h;
        }
    }

    public static void drawStringCenteredWithCenterAlign(Graphics2D g2d, String str, int y) {
        String[] str_ = str.split("\n");

        FontMetrics fm = g2d.getFontMetrics();

        int h = fm.getHeight();
        for (String s: str_) {
            drawStringCentered(g2d, s, y);

            y += h;
        }
    }

    public static void drawStringCentered(Graphics2D g2d, String str, int y) {
        drawStringCenteredBaseOn(g2d, str, y, 0, TheGreatMachine.WIDTH);
    }

    public static void drawStringCenteredBaseOn(Graphics2D g2d, String str, int y, int leftX, int rightX) {
        FontMetrics fm = g2d.getFontMetrics();

        int x = (rightX - leftX - fm.stringWidth(str)) / 2 + leftX;

        g2d.drawString(str, x, y);
    }
}