package fr.poulpogaz.thegreatmachine.window;

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

    int getWindowWidth();

    int getWindowHeight();
}
