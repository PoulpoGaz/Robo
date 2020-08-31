package fr.poulpogaz.thegreatmachine.gui;

import fr.poulpogaz.json.IJsonReader;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.JsonReader;
import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;
import fr.poulpogaz.thegreatmachine.utils.TextureManager;
import fr.poulpogaz.thegreatmachine.window.MouseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GuiElement {

    private static final Logger LOGGER = LogManager.getLogger(GuiElement.class);
    private static final TextureManager textureManager = TheGreatMachine.getInstance().getTextureManager();
    private static final MouseHandler mouse = TheGreatMachine.getInstance().getMouseHandler();

    private ResourceLocation resourceLocation;
    protected ResourceLocation noneLocation;
    protected ResourceLocation hoveredLocation;
    protected ResourceLocation pressedLocation;

    protected boolean pressed;
    protected boolean hovered;
    protected boolean released;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GuiElement(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;

        load();
    }

    protected void load() {
        try {
            String name = resourceLocation.getName();

            IJsonReader reader = new JsonReader(resourceLocation.createInputStream());

            reader.beginObject();

            for (int i = 0; i < 3; i++) {
                String key = reader.nextKey();

                reader.beginObject();
                BufferedImage image = textureManager.loadSprite(reader);
                reader.endObject();

                switch (key) {
                    case "pressed" -> {
                        pressedLocation = new ResourceLocation(name + "_pressed", ResourceLocation.GUI_ELEMENT);
                        textureManager.put(image, pressedLocation);
                    }
                    case "hovered" -> {
                        hoveredLocation = new ResourceLocation(name + "_hovered", ResourceLocation.GUI_ELEMENT);
                        textureManager.put(image, hoveredLocation);
                    }
                    case "none" -> {
                        noneLocation = new ResourceLocation(name + "_none", ResourceLocation.GUI_ELEMENT);
                        textureManager.put(image, noneLocation);
                    }
                    default -> throw new IllegalStateException("Unknown gui type: " + key);
                }
            }

            reader.endObject();
            reader.close();
        } catch (IOException | JsonException e) {
            LOGGER.warn("Failed to load gui element at " + resourceLocation, e);
        }
    }

    public void render(Graphics2D g2d) {
        BufferedImage image = getTexture();

        g2d.drawImage(image, x, y, null);
    }

    public void update() {
        int mX = mouse.getMouseX();
        int mY = mouse.getMouseY();

        if (x <= mX && mX <= x + width && y <= mY && mY <= y + height) {
            hovered = true;

            if (mouse.isMousePressed(MouseHandler.LEFT_BUTTON)) {
                pressed = true;
                released = false;
            } else if (mouse.isMouseReleased(MouseHandler.LEFT_BUTTON)) {
                pressed = false;
                released = true;
            } else {
                pressed = false;
                released = false;
            }
        } else {
            hovered = false;
            pressed = false;
            released = false;
        }
    }

    public BufferedImage getTexture() {
        if (pressed) {
            return textureManager.getTexture(pressedLocation);
        } else if (hovered) {
            return textureManager.getTexture(hoveredLocation);
        } else {
            return textureManager.getTexture(noneLocation);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBounds(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }
}