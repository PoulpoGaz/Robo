package fr.poulpogaz.thegreatmachine;

import fr.poulpogaz.thegreatmachine.window.Window;

import java.awt.*;

public interface View {

    void start(Window window);

    void update(float delta);

    void render(Graphics2D g2d);

    void terminate();

    Canvas geCanvas();

    int getTPS();

    String getViewName();

    int getViewWidth();

    int getViewHeight();
}
