package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.utils.ISprite;
import fr.poulpogaz.robo.utils.ResourceLocation;

public class Robot implements ISprite {

    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("robot", ResourceLocation.ROBOT);

    private Pos pos;

    public Robot(int x, int y) {
        pos = new Pos(x, y);
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return RESOURCE_LOCATION;
    }
}