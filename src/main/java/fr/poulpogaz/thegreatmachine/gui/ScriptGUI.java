package fr.poulpogaz.thegreatmachine.gui;

import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import fr.poulpogaz.thegreatmachine.robot.Pos;
import fr.poulpogaz.thegreatmachine.window.KeyHandler;

import java.awt.*;

import static java.awt.event.KeyEvent.*;

public class ScriptGUI {

    private static final KeyHandler keyHandler = TheGreatMachine.getInstance().getKeyHandler();

    private int width;
    private int height;

    private int caretPos = 0;
    private boolean isCaretMoving = false;
    private boolean hideCaret = false;

    private String script = "";
    private String[] linesCached;

    public ScriptGUI(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void render(Graphics2D g2d, int x, int y) {
        Shape clipBounds = g2d.getClipBounds();
        g2d.setClip(x, y, width, height);
        g2d.translate(x, y);

        drawBackground(g2d);

        g2d.setClip(5, 5, width - 10, height - 10);
        drawScript(g2d);

        g2d.setClip(clipBounds);
        g2d.translate(-x, -y);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(new Color(25, 25, 25));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(new Color(44, 44, 44));
        g2d.fillRect(5, 5, width - 10, height - 10);
    }

    private void drawScript(Graphics2D g2d) {
        g2d.setColor(FontColor.FOREGROUND);

        String[] lines = split();

        FontMetrics fm = g2d.getFontMetrics();

        int fontHeight = fm.getHeight();
        int charCount = 0;
        int y = 20;
        for (String line : lines) {
            g2d.drawString(line, 10, y);

            if (!hideCaret) {
                int lastCharCount = charCount;
                charCount += line.length();

                if (lastCharCount <= caretPos && caretPos <= charCount) {
                    int indexOfCaret = line.length() - (charCount - caretPos);

                    int width;
                    if (indexOfCaret >= line.length()) {
                        width = fm.stringWidth(line);
                    } else {
                        width = fm.stringWidth(line.substring(0, indexOfCaret));
                    }

                    g2d.fillRect(10 + width, y - fontHeight, 1, fontHeight);
                }

                charCount++; // do not forget the line feed
            }

            y += fontHeight;
        }
    }

    public void update() {
        if (TheGreatMachine.getInstance().getTicks() % 15 == 0 && !isCaretMoving) {
            hideCaret = !hideCaret;
        }

        for (int key = VK_A; key <= VK_Z; key++) {
            if (keyPressed(key)) {
                insertChar((char) (key + 32)); // lower
            }
        }
        for (int key = VK_0; key <= VK_9; key++) {
            if (keyPressed(key)) {
                insertChar((char) key);
            }
        }

        if (keyPressed(VK_ENTER)) {
            insertChar('\n');
        }
        if (keyPressed(VK_SPACE)) {
            insertChar(' ');
        }

        if (keyPressed(VK_BACK_SPACE)) {
            removeChar();
        }

        isCaretMoving = false;

        if (keyPressed(VK_LEFT)) {
            caretPos--;

            if (caretPos < 0) {
                caretPos = 0;
            }

            isCaretMoving = true;
            hideCaret = false;
        }

        if (keyPressed(VK_RIGHT)) {
            caretPos++;

            if (caretPos > script.length()) {
                caretPos = script.length();
            }

            isCaretMoving = true;
            hideCaret = false;
        }

        if (keyPressed(VK_UP)) {
            moveCaret(true);
        }

        if (keyPressed(VK_DOWN)) {
            moveCaret(false);
        }
    }

    private boolean keyPressed(int key) {
        return keyHandler.isKeyPressed(key, 2) || keyHandler.isKeyReleased(key);
    }

    private void insertChar(char c) {
        if (caretPos == script.length()) {
            script += c;
        } else if (caretPos == 0) {
            script = c + script;
        } else {
            String firstPart = script.substring(0, caretPos);
            String secondPart = script.substring(caretPos);

            script = firstPart + c + secondPart;
        }
        caretPos++;

        linesCached = null;
    }

    private void removeChar() {
        if (caretPos != 0 && script.length() != 0) {
            if (caretPos == script.length()) {
                script = script.substring(0, script.length() - 1);
            } else {
                String firstPart = script.substring(0, caretPos - 1);
                String secondPart = script.substring(caretPos);

                script = firstPart + secondPart;
            }
            caretPos--;
            linesCached = null;
        }
    }

    private void moveCaret(boolean up) {
        Pos pos = getCaretPos();

        if (up) {
            if (pos.y == 0) {
                return;
            }

            pos.y--;
        } else {
            if (pos.y == split().length - 1) {
                return;
            }

            pos.y++;
        }

        setCaretPos(pos);
    }

    private void setCaretPos(Pos pos) {
        String[] lines = split();

        System.out.println(caretPos);

        int charCount = 0;
        int y = 0;
        for (String line : lines) {
            if (y == pos.y) {
                if (pos.x >= line.length()) {
                    caretPos = charCount + line.length();
                } else {
                    caretPos = charCount + pos.x;
                }

                break;
            }

            charCount = charCount + line.length() + 1; // do not forget the line feed
            y++;
        }

        System.out.println(caretPos);
    }

    private Pos getCaretPos() {
        String[] lines = split();

        int charCount = 0;
        int x = 0;
        int y = 0;
        for (String line : lines) {
            int lastCharCount = charCount;
            charCount += line.length();

            if (lastCharCount <= caretPos && caretPos <= charCount) {
                x = line.length() - (charCount - caretPos);

                break;
            }

            charCount++; // do not forget the line feed
            y++;
        }

        return new Pos(x, y);
    }

    private String[] split() {
        if (linesCached == null) {
            linesCached = script.split("\n");
        }

        return linesCached;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}