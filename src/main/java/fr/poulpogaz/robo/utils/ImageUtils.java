package fr.poulpogaz.robo.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static BufferedImage dye(BufferedImage image, Color color) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage dyed = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) dyed.getGraphics();
        try {
            g2d.drawImage(image, 0, 0, null);

            g2d.setComposite(AlphaComposite.SrcAtop);
            g2d.setColor(color);
            g2d.fillRect(0, 0, width, height);
        } finally {
            g2d.dispose();
        }

        return dyed;
    }

    public static BufferedImage copy(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        Graphics2D g2d = copy.createGraphics();

        try {
            g2d.drawImage(image, 0, 0, null);
        } finally {
            g2d.dispose();
        }

        return copy;
    }

    public static BufferedImage resize(BufferedImage image, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());

        Graphics2D g2d = resizedImage.createGraphics();

        try {
            g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        } finally {
            g2d.dispose();
        }

        return resizedImage;
    }
}