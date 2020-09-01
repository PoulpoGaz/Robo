package fr.poulpogaz.robo.utils;

import java.awt.*;

public class InterpolationHelper {

    public static int lerp(int v1, int v2, float t) {
        return (int) ((1 - t) * v1 + t * v2);
    }

    public static Color lerp(Color a, Color b, float t) {
        int red   = lerp(a.getRed(), b.getRed(), t);
        int green = lerp(a.getGreen(), b.getGreen(), t);
        int blue  = lerp(a.getBlue(), b.getBlue(), t);
        int alpha = lerp(a.getAlpha(), b.getAlpha(), t);

        return new Color(red, green, blue, alpha);
    }
}