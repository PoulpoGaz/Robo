package fr.poulpogaz.thegreatmachine.gui;

import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import fr.poulpogaz.thegreatmachine.robot.Report;

import java.awt.*;

public class ReportGui extends GuiElement {

    private static final int _1_SEC = TheGreatMachine.TPS;
    private static final int _3_SEC = _1_SEC * 3;
    private static final int _4_SEC = _1_SEC * 4;

    private int maxWidth;

    private Report report;
    private boolean isVisible;

    private boolean isValid;

    private Color out;
    private Color in;

    private String text;

    private boolean showClickToHide = true;

    private int tickCount;

    public ReportGui() {
        super(null);
    }

    @Override
    protected void load() {

    }

    @Override
    public void render(Graphics2D g2d) {
        if (isVisible) {
            if (!isValid) {
                computeBounds(g2d.getFontMetrics());
                setColors();

                isValid = true;
            }

            Composite old = g2d.getComposite();

            boolean decreaseAlpha = tickCount > _3_SEC;

            if (decreaseAlpha) {
                float alpha = 1 - ((float) tickCount - _3_SEC) / _1_SEC;

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            }

            FontMetrics fm = g2d.getFontMetrics();

            g2d.setColor(out);
            g2d.fillRect(x, y, width, height);
            g2d.setColor(in);
            g2d.fillRect(x + 5, y + 5, width - 10, height - 10);


            String[] lines = text.split("\n");
            g2d.setColor(FontColor.FOREGROUND_LIGHT);

            int yText = y + 20 - fm.getAscent();
            for (int i = 0, length = lines.length; i < length; i++) {
                String line = lines[i];

                if (i == length - 1) {
                    g2d.setColor(FontColor.FOREGROUND);
                }

                g2d.drawString(line, x + 8, yText);

                yText += fm.getHeight();
            }

            g2d.setComposite(old);
        }
    }

    private void setColors() {
        if (report.isSuccess()) {
            out = new Color(56, 118, 69);
            in = new Color(70, 147, 80);
        } else {
            out = new Color(146, 68, 68);
            in = new Color(170, 81, 81);
        }
    }

    private void computeBounds(FontMetrics fm) {
        width = 20;
        height = 10;

        if (report.isSuccess()) {
            text = "Build in " + report.getTime() + " ms";

            width += fm.stringWidth(text);
            height += fm.getHeight();
        } else {
            text = report.getError();

            width += fm.stringWidth(text);

            if (width > maxWidth) {
                int nLines = (int) Math.ceil((float) width / maxWidth);

                int charWidth = fm.charWidth('a'); // monospaced font

                for (int i = 1; i < nLines; i++) {
                    int clipAt = i * width / (nLines * charWidth);

                    clipAt = text.lastIndexOf(' ', clipAt);

                    text = text.substring(0, clipAt) + "\n" + text.substring(clipAt + 1); // insert line feed and remove space
                }

                width = maxWidth;
                height += fm.getHeight() * nLines;
            } else {
                height += fm.getHeight();
            }
        }

        if (showClickToHide) {
            String clickToHide = "\nAuto hide after 4 sec or click";

            text += clickToHide;

            width = Math.max(fm.stringWidth(clickToHide) + 20, width);
            height += fm.getHeight();
        }
    }

    @Override
    public void update() {
        if (isVisible && isValid) {
            super.update();

            if (released) {
                isVisible = false;
                showClickToHide = false;
            }

            tickCount++;

            if (tickCount % _4_SEC == 0) {
                isVisible = false;
                tickCount = 0;
            }
        }
    }

    public void setReport(Report report) {
        this.report = report;

        isVisible = true;
        isValid = false;
        tickCount = 0;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
}