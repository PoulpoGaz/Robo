package fr.poulpogaz.thegreatmachine.window;

import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Window extends JFrame {

    private static final Logger LOGGER = LogManager.getLogger(Window.class);

    private final View view;
    private final Canvas canvas;

    private Thread thread;
    private boolean running = false;

    private final MouseHandler mouse;
    private final KeyHandler key;

    private int ticks = 0;

    public Window(View view) {
        this.view = Objects.requireNonNull(view);
        canvas = view.geCanvas();

        mouse = new MouseHandler();
        key = new KeyHandler();

        initWindow();
    }

    private void initWindow() {
        setName(view.getViewName());
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowHandler());

        canvas.setFocusTraversalKeysEnabled(false);
        canvas.setPreferredSize(new Dimension(view.getWindowWidth(), view.getWindowHeight()));
        canvas.addMouseWheelListener(mouse);
        canvas.addMouseMotionListener(mouse);
        canvas.addMouseListener(mouse);
        canvas.addKeyListener(key);

        add(canvas);
        pack();

        setLocationRelativeTo(null);
    }

    public synchronized void start() {
        if (!running) {
            running = true;

            view.start(this);
            LOGGER.info("Starting thread");

            thread = new Thread(this::run);
            thread.setName("Game thread");
            thread.start();

            setVisible(true);
            requestFocus();
        }
    }

    public synchronized void stop() {
        if (running) {
            LOGGER.info("Stopping thread");
            running = false;
        }
    }

    private void run() {
        long lastTime = System.nanoTime();
        float ns = 1000000000.000f / view.getTPS();
        float delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        int ticks = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                ticks++;
                this.ticks++;
                view.update(delta);

                mouse.reset();
                key.reset();
                delta--;
            }

            if (running) {
                render();
            }

            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;

                LOGGER.info("FPS: {}, TPS: {}", frames, ticks);

                frames = 0;
                ticks = 0;
            }
        }

        view.terminate();
        dispose();
    }

    private void render() {
        BufferStrategy bs = canvas.getBufferStrategy();
        if(bs == null) {
            canvas.createBufferStrategy(2);
            return;
        }

        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();

        // disable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

        g2d.scale(TheGreatMachine.SCALE_FACTOR, TheGreatMachine.SCALE_FACTOR);

        view.render(g2d);

        g2d.dispose();
        bs.show();
    }

    public MouseHandler getMouseHandler() {
        return mouse;
    }

    public KeyHandler getKeyHandler() {
        return key;
    }

    public boolean isRunning() {
        return running;
    }

    public int getTicks() {
        return ticks;
    }

    private class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            stop();
        }
    }
}