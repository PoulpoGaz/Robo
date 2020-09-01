package fr.poulpogaz.robo.gui;

import fr.poulpogaz.robo.utils.StringUtils;

import java.awt.*;

public class StringButton extends GuiElement {

    private boolean isValid;
    private boolean active = true;
    private Runnable releaseListener = () -> {};

    private String text;

    public StringButton() {

    }

    public StringButton(String text, Runnable releaseListener) {
        setText(text);
        setReleaseListener(releaseListener);
    }

    @Override
    protected void renderImpl(Graphics2D g2d) {
        if (!active) {
            Graphics2D g = (Graphics2D) g2d.create();

            try {
                drawButton(g2d);

                Composite old = g.getComposite();

                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g.setColor(new Color(23, 23, 23));
                g.fillRect(0, 0, width, height);

                g.setComposite(old);
            } finally {
                g.dispose();
            }
        } else {
            drawButton(g2d);
        }
    }

    private void drawButton(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();

        if (!isValid) {
            computeBounds(fm);
        }

        drawBackground(g2d, 3, new Color(44, 44, 44), new Color(25, 25, 25));

        g2d.setColor(FontColor.FOREGROUND);

        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height + fm.getAscent()) / 2;

        g2d.drawString(text, x, y);
    }

    void computeBounds(FontMetrics fm) {
        Dimension dimension = StringUtils.computeDimension(fm, text);

        width = dimension.width + 14;
        height = dimension.height + 4;

        isValid = true;
    }

    @Override
    protected void updateImpl() {
        if (isValid) {
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
    }

    public Runnable getReleaseListener() {
        return releaseListener;
    }

    public void setReleaseListener(Runnable releaseListener) {
        this.releaseListener = releaseListener;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        isValid = false;
    }
}