package fr.poulpogaz.thegreatmachine.utils;

import java.io.InputStream;

public class ResourceLocation {

    public static final int TILE = 0;
    public static final int TEXTURE = 1;
    public static final int LEVEL = 2;
    public static final int FONT = 3;
    public static final int ROBOT = 4;
    public static final int GUI_ELEMENT = 5;

    private static final String[] LOCATIONS = new String[] {"/tiles/%s.json", "/textures/%s.png", "/levels/%s.json", "/fonts/%s.font", "/robot/%s.json", "/gui/%s.json"};

    private String name;
    private int type;

    public ResourceLocation(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public ResourceLocation(String resource) {
        int colon = resource.indexOf(':');

        String type = resource.substring(0, colon);

        this.type = switch (type) {
            case "tile" -> TILE;
            case "texture" -> TEXTURE;
            case "level" -> LEVEL;
            case "font" -> FONT;
            case "robot" -> ROBOT;
            case "gui" -> GUI_ELEMENT;
            default -> throw new IllegalStateException();
        };

        name = resource.substring(colon + 1);
    }

    public InputStream createInputStream() {
        return createInputStreamStatic(name, type);
    }

    public String getPath() {
        return getPathStatic(name, type);
    }

    public static String getPathStatic(String resource, int type) {
        return String.format(LOCATIONS[type], resource);
    }

    public static InputStream createInputStreamStatic(String resource, int type) {
        String path = getPathStatic(resource, type);

        return ResourceLocation.class.getResourceAsStream(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    private String getResource() {
        return switch (type)  {
            case TILE -> "tile:" + name;
            case TEXTURE -> "texture:" + name;
            case LEVEL -> "level:" + name;
            case FONT -> "font:" + name;
            case ROBOT -> "robot:" + name;
            case GUI_ELEMENT -> "gui:" + name;
            default -> throw new IllegalStateException();
        };
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toString() {
        return String.format("%s (%s)", getPath(), getResource());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceLocation)) {
            return false;
        }

        ResourceLocation that = (ResourceLocation) o;

        return getResource().equals(that.getResource());
    }

    @Override
    public int hashCode() {
        return getResource().hashCode();
    }
}