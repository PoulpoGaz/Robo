package fr.poulpogaz.thegreatmachine.utils;

import fr.poulpogaz.json.IJsonReader;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.JsonReader;
import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public final class TextureManager {

    private static final Logger LOGGER = LogManager.getLogger(TextureManager.class);

    private final HashMap<ResourceLocation, BufferedImage> IMAGES;
    private final HashMap<ResourceLocation, BufferedImage> SPRITES;

    public TextureManager() {
        IMAGES = new HashMap<>();
        SPRITES = new HashMap<>();
    }

    public void put(BufferedImage image, ResourceLocation location) {
        if (location.getType() == ResourceLocation.TEXTURE) {
            IMAGES.put(location, image);
        } else {
            SPRITES.put(location, image);
        }
    }

    public BufferedImage getTexture(ResourceLocation resource) {
        if (resource.getType() == ResourceLocation.TEXTURE) {
            return readImage(resource);
        } else {
            BufferedImage texture = SPRITES.get(resource);

            if (texture == null) {
                texture = loadTileTexture(resource);
            }

            return texture;
        }
    }

    private BufferedImage loadTileTexture(ResourceLocation resource) {
        try {
            LOGGER.info("Reading resource at {}", resource);
            IJsonReader reader = new JsonReader(resource.createInputStream());

            reader.beginObject();

            String texture = reader.skipKey().nextString();

            BufferedImage image = readImage(new ResourceLocation(texture));

            if (image == null) {
                return null;
            }

            if (reader.hasNextKey()) {
                reader.skipKey().beginArray();

                int x = reader.nextInt();
                int y = reader.nextInt();

                BufferedImage subImage = image.getSubimage(x * TheGreatMachine.TILE_SIZE, y * TheGreatMachine.TILE_SIZE, TheGreatMachine.TILE_SIZE, TheGreatMachine.TILE_SIZE);

                SPRITES.put(resource, subImage);

                reader.endArray();
                reader.endObject();
                reader.close();

                return subImage;
            } else {
                reader.endObject();
                reader.close();

                SPRITES.put(resource, image);

                return image;
            }
        } catch (IOException | JsonException e) {
            LOGGER.warn("Failed to load texture at {}", resource);
            LOGGER.warn(e);

            return null;
        }
    }

    private BufferedImage readImage(ResourceLocation resource) {
        try {
            LOGGER.info("Reading image at {}", resource);

            BufferedImage image = IMAGES.get(resource);

            if (image == null) {
                image = ImageIO.read(resource.createInputStream());

                IMAGES.put(resource, image);
            }

            return image;
        } catch (IOException e) {
            LOGGER.warn("Failed to load texture at {}", resource);
            LOGGER.warn(e);

            return null;
        }
    }
}