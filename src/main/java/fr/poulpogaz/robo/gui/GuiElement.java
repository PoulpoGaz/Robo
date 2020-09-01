package fr.poulpogaz.robo.gui;

import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.window.MouseHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class GuiElement {

    protected static final MouseHandler mouse = Robo.getInstance().getMouseHandler();

    protected boolean pressed;
    protected boolean hovered;
    protected boolean released;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    protected boolean isVisible = true;

    public GuiElement() {

    }

    public void render(Graphics2D g2d) {
        if (isVisible) {
            Rectangle oldClip = g2d.getClipBounds();

            g2d.setClip(x, y, width, height);
            g2d.translate(x, y);
            renderImpl(g2d);

            g2d.translate(-x, -y);
            g2d.setClip(oldClip);
        }
    }

    protected abstract void renderImpl(Graphics2D g2d);

    @SuppressWarnings("SuspiciousNameCombination")
    protected void drawBackground(Graphics2D g2d, int borderWidth, Color in, Color out) {
        Stroke old = g2d.getStroke();

        float halfWidth = borderWidth / 2f;
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.setColor(out);
        g2d.draw(new Rectangle2D.Float(halfWidth, halfWidth, width - borderWidth, height - borderWidth));

        g2d.setStroke(old);

        g2d.setColor(in);
        g2d.fillRect(borderWidth, borderWidth, width - borderWidth * 2, height - borderWidth * 2);
    }

    public void update() {
        if (isVisible) {
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

            updateImpl();
        }
    }

    protected abstract void updateImpl();

    public boolean isPressed() {
        return pressed;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean isReleased() {
        return released;
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}