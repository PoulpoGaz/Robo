package fr.poulpogaz.thegreatmachine.gui;

import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;

import java.awt.*;

public class Button extends GuiElement {

    private boolean active = true;
    private Runnable releaseListener = () -> {};

    public Button(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public void render(Graphics2D g2d) {
        if (!active) {
            Graphics2D g = (Graphics2D) g2d.create();

            try {
                super.render(g);

                Composite old = g.getComposite();

                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g.setColor(new Color(45, 45, 45));
                g.fillRect(x, y, width, height);

                g.setComposite(old);
            } finally {
                g.dispose();
            }
        } else {
            super.render(g2d);
        }
    }

    public void update() {
        if (active) {
            super.update();

            if (released) {
                releaseListener.run();
            }
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    public boolean isReleased() {
        return released;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;

        if (!active) {
            pressed = false;
            released = false;
            hovered = false;
        }
    }

    public Runnable getReleaseListener() {
        return releaseListener;
    }

    public void setReleaseListener(Runnable releaseListener) {
        this.releaseListener = releaseListener;
    }
}