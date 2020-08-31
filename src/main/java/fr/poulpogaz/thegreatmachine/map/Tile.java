package fr.poulpogaz.thegreatmachine.map;

import fr.poulpogaz.thegreatmachine.utils.ISprite;
import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;

public class Tile implements ISprite {

    private ResourceLocation resourceLocation;

    public Tile(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isSolid() {
        return false;
    }

    public boolean isVoid() {
        return false;
    }

    public boolean isDestination() {
        return false;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
}