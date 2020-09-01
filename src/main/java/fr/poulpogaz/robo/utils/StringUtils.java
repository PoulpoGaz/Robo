package fr.poulpogaz.robo.utils;

import java.awt.*;

public class StringUtils {

    public static Dimension computeDimension(FontMetrics fm, String text) {
        Dimension dim = new Dimension();

        String[] lines = text.split("\n");

        for (String line : lines) {
            dim.width = Math.max(dim.width, fm.stringWidth(line));

            dim.height += fm.getHeight();
        }

        return dim;
    }
}