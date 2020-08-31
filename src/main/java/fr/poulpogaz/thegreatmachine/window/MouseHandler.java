package fr.poulpogaz.thegreatmachine.window;

import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseHandler extends MouseAdapter {

    public static int RIGHT_BUTTON = 0;
    public static int MIDDLE_BUTTON = 1;
    public static int LEFT_BUTTON = 2;

    public static boolean[] press = new boolean[3];
    public static boolean[] release = new boolean[3];
    public static int wheelFactor = 0;

    public static int mouseX = 0;
    public static int mouseY = 0;

    private boolean mouseDragged = false;

    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            press[0] = true;
            release[0] = false;
        } else if(SwingUtilities.isMiddleMouseButton(e)) {
            press[1] = true;
            release[1] = false;
        } else if(SwingUtilities.isLeftMouseButton(e)) {
            press[2] = true;
            release[2] = false;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            press[0] = false;
            release[0] = true;
        } else if(SwingUtilities.isMiddleMouseButton(e)) {
            press[1] = false;
            release[1] = true;
        } else if(SwingUtilities.isLeftMouseButton(e)) {
            press[2] = false;
            release[2] = true;
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelFactor += e.getWheelRotation();
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX() / TheGreatMachine.SCALE_FACTOR;
        mouseY = e.getY() / TheGreatMachine.SCALE_FACTOR;

        mouseDragged = true;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX() / TheGreatMachine.SCALE_FACTOR;
        mouseY = e.getY() / TheGreatMachine.SCALE_FACTOR;
    }

    public void reset() {
        for(int i = 0; i < 3; i++) {
            if(!press[i]) {
                release[i] = false;
            }
        }

        mouseDragged = false;
        wheelFactor = 0;
    }

    public boolean isMouseReleased(int mouseButton) {
        return release[mouseButton];
    }

    public boolean isMousePressed(int mouseButton) {
        return press[mouseButton];
    }

    public int wheel() {
        return wheelFactor;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isMouseDragged() {
        return mouseDragged;
    }
}