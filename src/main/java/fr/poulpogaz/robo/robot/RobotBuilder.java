package fr.poulpogaz.robo.robot;

public class RobotBuilder {

    private int x;
    private int y;

    public RobotBuilder() {

    }

    public Robot build() {
        return new Robot(x, y);
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
}