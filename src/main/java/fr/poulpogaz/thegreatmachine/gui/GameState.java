package fr.poulpogaz.thegreatmachine.gui;

import fr.poulpogaz.thegreatmachine.level.LevelManager;
import fr.poulpogaz.thegreatmachine.level.LevelRenderer;
import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;

import java.awt.*;

import static fr.poulpogaz.thegreatmachine.main.TheGreatMachine.TILE_SIZE;

public class GameState extends State {

    private static final LevelManager levelManager = LevelManager.getInstance();
    private static final LevelRenderer levelRenderer = LevelRenderer.getInstance();

    private static final int SCRIPT_GUI_WIDTH = 192;
    private static final int LEVEL_RENDER_WIDTH = TheGreatMachine.WIDTH - SCRIPT_GUI_WIDTH;

    private final ScriptGUI scriptGUI;
    private Button playButton;
    private Button stopButton;

    public GameState() {
        super("GameState");

        scriptGUI = new ScriptGUI(SCRIPT_GUI_WIDTH, TheGreatMachine.HEIGHT);
        playButton = new Button(new ResourceLocation("play", ResourceLocation.GUI_ELEMENT));
        playButton.setBounds(5, 5, TILE_SIZE, TILE_SIZE);
        playButton.setReleaseListener(this::play);

        stopButton = new Button(new ResourceLocation("stop", ResourceLocation.GUI_ELEMENT));
        stopButton.setBounds(15 + TILE_SIZE, 5, TILE_SIZE, TILE_SIZE);
        stopButton.setActive(false);
        stopButton.setReleaseListener(this::stop);
    }

    @Override
    protected void renderBackground(Graphics2D g2d) {
        levelRenderer.render(g2d, levelManager.getCurrentLevel(), LEVEL_RENDER_WIDTH, TheGreatMachine.HEIGHT);
        playButton.render(g2d);
        stopButton.render(g2d);

        scriptGUI.render(g2d, LEVEL_RENDER_WIDTH, 0);
    }

    @Override
    public void update(float delta) {
        scriptGUI.update();

        playButton.update();
        stopButton.update();
    }

    private void play() {
        playButton.setActive(false);
        stopButton.setActive(true);
    }

    private void stop() {
        playButton.setActive(true);
        stopButton.setActive(false);
    }
}