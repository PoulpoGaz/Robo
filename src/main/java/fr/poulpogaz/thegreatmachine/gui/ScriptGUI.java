package fr.poulpogaz.thegreatmachine.gui;

import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import fr.poulpogaz.thegreatmachine.robot.Pos;
import fr.poulpogaz.thegreatmachine.window.KeyHandler;

import java.awt.*;

import static java.awt.event.KeyEvent.*;

public class ScriptGUI extends GuiElement {

    private static final KeyHandler keyHandler = TheGreatMachine.getInstance().getKeyHandler();

    private int caretPos = 0;
    private boolean isCaretMoving = false;
    private boolean hideCaret = false;

    private String script = "";
    private String[] linesCached;
    private int nLines = 1;

    public ScriptGUI() {

    }

    public void renderImpl(Graphics2D g2d) {
        drawBackground(g2d);

        g2d.setClip(10, 5, width - 20, height - 10);
        drawScript(g2d, 10, 20);
    }

    private void drawBackground(Graphics2D g2d) {
        g2d.setColor(new Color(25, 25, 25));
        g2d.fillRect(0, 0, width, height);

        FontMetrics fm = g2d.getFontMetrics();
        int maxNumberWidth = fm.stringWidth(Integer.toString(nLines)) + 8;

        g2d.setColor(new Color(33, 33, 33));
        g2d.fillRect(5, 5, maxNumberWidth, height - 10);

        g2d.setColor(new Color(44, 44, 44));
        g2d.fillRect(5 + maxNumberWidth, 5, width - 10 - maxNumberWidth, height - 10);
    }

    private void drawScript(Graphics2D g2d, int x, int y) {
        g2d.setColor(FontColor.FOREGROUND);

        String[] lines = split();

        FontMetrics fm = g2d.getFontMetrics();

        int maxNumberWidth = fm.stringWidth(Integer.toString(nLines)) + 3;

        int fontHeight = fm.getHeight();
        int ascent = fm.getAscent();
        int charCount = 0;
        for (int i = 0; i < nLines; i++) {
            g2d.setColor(FontColor.FOREGROUND_DARK); // draw the number
            g2d.drawString(Integer.toString(i + 1), x, y);

            g2d.setColor(FontColor.FOREGROUND);

            String line;

            if (i < lines.length) {
                line = lines[i];

                g2d.drawString(line, x + maxNumberWidth, y);
            } else {
                line = "";
            }

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

                    g2d.fillRect(x + width + maxNumberWidth, y - ascent, 1, ascent);
                }

                charCount++; // do not forget the line feed
            }

            y += fontHeight;
        }
    }

    public void updateImpl() {
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
            nLines++;
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
                if (script.charAt(caretPos - 1) == '\n') {
                    nLines--;
                }

                script = script.substring(0, script.length() - 1);
            } else {
                String firstPart = script.substring(0, caretPos - 1);
                String secondPart = script.substring(caretPos);

                script = firstPart + secondPart;


                if (script.charAt(caretPos) == '\n') {
                    nLines--;
                }
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

        isCaretMoving = true;
        hideCaret = false;
    }

    private void setCaretPos(Pos pos) {
        String[] lines = split();

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

    public String getScript() {
        return script;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}