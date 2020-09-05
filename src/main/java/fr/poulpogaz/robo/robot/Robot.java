package fr.poulpogaz.robo.robot;

import fr.poulpogaz.robo.utils.ResourceLocation;

public class Robot {

    private static final ResourceLocation ROBOT_NORMAL = new ResourceLocation("robot_normal", ResourceLocation.ROBOT);
    private static final ResourceLocation ROBOT_HAPPY = new ResourceLocation("robot_happy", ResourceLocation.ROBOT);
    private static final ResourceLocation ROBOT_SAD = new ResourceLocation("robot_sad", ResourceLocation.ROBOT);
    private static final ResourceLocation ROBOT_NOT_RUNNING = new ResourceLocation("robot_not_running", ResourceLocation.ROBOT);

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

    public ResourceLocation getResourceLocation(boolean levelFailed, boolean levelFinished, boolean running) {
        if (levelFailed) {
            return ROBOT_SAD;
        } else if (levelFinished) {
            return ROBOT_HAPPY;
        } else if (running) {
            return ROBOT_NORMAL;
        } else {
            return ROBOT_NOT_RUNNING;
        }
    }
}