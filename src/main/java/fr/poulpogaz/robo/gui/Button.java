package fr.poulpogaz.robo.gui;

import fr.poulpogaz.robo.utils.ResourceLocation;

import java.awt.*;

public class Button extends TexturedGuiElement {

    private boolean active = true;
    private Runnable releaseListener = () -> {};

    public Button(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    protected void renderImpl(Graphics2D g2d) {
        if (!active) {
            Graphics2D g = (Graphics2D) g2d.create();

            try {
                super.renderImpl(g);

                Composite old = g.getComposite();

                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g.setColor(new Color(45, 45, 45));
                g.fillRect(0, 0, width, height);

                g.setComposite(old);
            } finally {
                g.dispose();
            }
        } else {
            super.renderImpl(g2d);
        }
    }

    protected void updateImpl() {
        if (active) {
            if (released) {
                releaseListener.run();
            }
        } else {
            pressed = false;
            hovered = false;
            released = false;
        }
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