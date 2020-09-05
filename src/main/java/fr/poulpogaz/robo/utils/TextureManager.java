package fr.poulpogaz.robo.utils;

import fr.poulpogaz.json.IJsonReader;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.JsonReader;
import fr.poulpogaz.robo.main.Robo;
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

    private static final TextureManager INSTANCE = new TextureManager();

    private TextureManager() {
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

    public void loadTexture(ResourceLocation resource) {
        getTexture(resource);
    }

    private BufferedImage loadTileTexture(ResourceLocation resource) {
        try {
            LOGGER.info("Reading resource at {}", resource);
            IJsonReader reader = new JsonReader(resource.createInputStream());

            reader.beginObject();

            BufferedImage image = loadSprite(reader);
            SPRITES.put(resource, image);

            reader.endObject();
            reader.close();

            return image;
        } catch (IOException | JsonException e) {
            LOGGER.warn("Failed to load texture at {}", resource, e);

            return null;
        }
    }

    public BufferedImage loadSprite(IJsonReader reader) throws IOException, JsonException {
        String texture = reader.skipKey().nextString();

        BufferedImage image = readImage(new ResourceLocation(texture));

        if (image == null) {
            return null;
        }

        if (reader.hasNextKey()) {
            reader.skipKey().beginArray();

            int x = reader.nextInt();
            int y = reader.nextInt();

            if (reader.hasNextInt()) {
                int w = reader.nextInt();
                int h = reader.nextInt();

                image = image.getSubimage(x, y, w, h);
            } else {
                image = image.getSubimage(x * Robo.TILE_SIZE, y * Robo.TILE_SIZE, Robo.TILE_SIZE, Robo.TILE_SIZE);
            }

            reader.endArray();
        }

        return image;
    }

    private BufferedImage readImage(ResourceLocation resource) {
        BufferedImage image = IMAGES.get(resource);

        if (image != null) {
            return image;
        }

        try {
            LOGGER.info("Reading image at {}", resource);

            image = ImageIO.read(resource.createInputStream());
            IMAGES.put(resource, image);

            return image;
        } catch (IOException e) {
            LOGGER.warn("Failed to load texture at {}", resource, e);

            return null;
        }
    }

    public static TextureManager getInstance() {
        return INSTANCE;
    }
}