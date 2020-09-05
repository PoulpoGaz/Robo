package fr.poulpogaz.robo.gui;

import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.utils.StringUtils;

import java.awt.*;
import java.util.ArrayList;

public class GUIBox extends GuiElement {

    private String title = "";

    private String text = "";
    private boolean isValid = false;

    private ArrayList<StringButton> buttons;

    public GUIBox() {
        buttons = new ArrayList<>();

        isVisible = false;
    }

    @Override
    protected void renderImpl(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();

        if (!isValid) {
            for (StringButton button : buttons) {
                button.computeBounds(fm);
            }

            computeBounds(fm);

            isValid = true;
        }

        drawBackground(g2d, 5, new Color(44, 44, 44), new Color(25, 25, 25));

        g2d.setColor(FontColor.FOREGROUND);

        int yText = 20 - fm.getAscent();
        int x = (width - fm.stringWidth(title)) / 2;
        g2d.drawString(title, x, yText);

        if (!text.isEmpty()) {
            String[] lines = text.split("\n");

            int fontHeight = fm.getHeight();
            for (String line : lines) {
                yText += fontHeight;

                g2d.drawString(line, 8, yText);
            }
        }

        x = (width - (getButtonsWidth() + (buttons.size() - 1) * 5)) / 2;
        int yButton = yText + 5;

        for (StringButton button : buttons) {
            button.setX(x);
            button.setY(yButton);

            button.render(g2d);

            button.setX(x + getX());
            button.setY(yButton + getY());

            x += button.getWidth() + 5;
        }
    }

    @Override
    protected void updateImpl() {
        if (isValid) {
            buttons.forEach(GuiElement::update);
        }
    }

    private void computeBounds(FontMetrics fm) {
        Dimension dimension = StringUtils.computeDimension(fm, title + "\n" + text);

        width = Math.max(dimension.width, getButtonsWidth()) + 20;
        height = dimension.height + getButtonsHeight() + 15;

        x = (Robo.WIDTH - width) / 2;
        y = (Robo.HEIGHT - height) / 2;
    }

    private int getButtonsWidth() {
        int width = 0;

        for (StringButton button : buttons) {
            width += button.getWidth();
        }

        return width;
    }

    private int getButtonsHeight() {
        int height = 0;

        for (StringButton button : buttons) {
            height = Math.max(button.getHeight(), height);
        }

        return height;
    }

    public void addButton(StringButton button) {
        buttons.add(button);
    }

    public void setTitle(String title) {
        this.title = title;
        this.isValid = false;
    }

    public void setText(String text) {
        this.text = text;

        isValid = false;
    }
}