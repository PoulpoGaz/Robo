package fr.poulpogaz.thegreatmachine.robot;

import fr.poulpogaz.thegreatmachine.utils.ISprite;
import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;

import static fr.poulpogaz.thegreatmachine.main.TheGreatMachine.TILE_SIZE;

public class Robot implements ISprite, Cloneable {

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

    @Override
    public Object clone(){
        try {
            Robot robot = (Robot) super.clone();
            robot.pos = new Pos(pos.x, pos.y);

            return robot;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}