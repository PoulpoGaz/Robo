package fr.poulpogaz.thegreatmachine.gui;

import fr.poulpogaz.thegreatmachine.level.LevelManager;
import fr.poulpogaz.thegreatmachine.level.LevelRenderer;
import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;

import java.awt.*;

public class GameState extends State {

    private static final LevelManager levelManager = LevelManager.getInstance();
    private static final LevelRenderer levelRenderer = LevelRenderer.getInstance();

    private ScriptGUI scriptGUI;

    public GameState() {
        super("GameState");

        scriptGUI = new ScriptGUI(192, TheGreatMachine.HEIGHT);
    }

    @Override
    protected void renderBackground(Graphics2D g2d) {
        levelRenderer.render(g2d, levelManager.getCurrentLevel());

        scriptGUI.render(g2d, TheGreatMachine.WIDTH - scriptGUI.getWidth(), 0);
    }

    @Override
    public void update(float delta) {
        scriptGUI.update();
    }
}