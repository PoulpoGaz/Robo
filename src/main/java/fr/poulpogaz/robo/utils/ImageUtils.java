package fr.poulpogaz.robo.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

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