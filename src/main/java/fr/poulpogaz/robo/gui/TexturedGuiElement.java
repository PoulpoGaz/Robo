package fr.poulpogaz.robo.gui;

import fr.poulpogaz.json.IJsonReader;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.JsonReader;
import fr.poulpogaz.robo.main.TheGreatMachine;
import fr.poulpogaz.robo.utils.ResourceLocation;
import fr.poulpogaz.robo.utils.TextureManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class TexturedGuiElement extends GuiElement {

    private static final Logger LOGGER = LogManager.getLogger(GuiElement.class);
    private static final TextureManager textureManager = TheGreatMachine.getInstance().getTextureManager();

    private ResourceLocation resourceLocation;
    protected ResourceLocation noneLocation;
    protected ResourceLocation hoveredLocation;
    protected ResourceLocation pressedLocation;

    public TexturedGuiElement(ResourceLocation resourceLocation) {
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

    protected void renderImpl(Graphics2D g2d) {
        BufferedImage image = getTexture();

        g2d.drawImage(image, 0, 0, null);
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

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }
}