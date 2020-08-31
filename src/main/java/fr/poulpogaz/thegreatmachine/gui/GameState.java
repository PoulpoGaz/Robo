package fr.poulpogaz.thegreatmachine.gui;

import fr.poulpogaz.thegreatmachine.level.Level;
import fr.poulpogaz.thegreatmachine.level.LevelManager;
import fr.poulpogaz.thegreatmachine.level.LevelRenderer;
import fr.poulpogaz.thegreatmachine.main.TheGreatMachine;
import fr.poulpogaz.thegreatmachine.robot.Report;
import fr.poulpogaz.thegreatmachine.robot.ScriptExecutor;
import fr.poulpogaz.thegreatmachine.robot.ScriptThread;
import fr.poulpogaz.thegreatmachine.utils.ResourceLocation;
import org.apache.logging.log4j.spi.LoggerRegistry;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static fr.poulpogaz.thegreatmachine.main.TheGreatMachine.TILE_SIZE;

public class GameState extends State {

    private static final LevelManager levelManager = LevelManager.getInstance();
    private static final LevelRenderer levelRenderer = LevelRenderer.getInstance();

    private static final int SCRIPT_GUI_WIDTH = 192;
    private static final int LEVEL_RENDER_WIDTH = TheGreatMachine.WIDTH - SCRIPT_GUI_WIDTH;

    private final ScriptGUI scriptGUI;
    private Button playButton;
    private Button stopButton;

    private ReportGui reportGui;
    private Future<Report> scriptParser;
    private boolean isParsing = false;
    private boolean isRunning = false;

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

        reportGui = new ReportGui();
        reportGui.setX(stopButton.getX() + stopButton.getWidth() + 60);
        reportGui.setY(5);
        reportGui.setMaxWidth(200);
    }

    @Override
    protected void renderBackground(Graphics2D g2d) {
        levelRenderer.render(g2d, levelManager.getCurrentLevel(), LEVEL_RENDER_WIDTH, TheGreatMachine.HEIGHT);
        playButton.render(g2d);
        stopButton.render(g2d);

        scriptGUI.render(g2d, LEVEL_RENDER_WIDTH, 0);

        if (isParsing || isRunning) {
            g2d.setColor(FontColor.FOREGROUND);

            if (isParsing) {
                g2d.drawString("Parsing...", 50, 15);
            } else if (isRunning) {
                g2d.drawString("Running...", 50, 15);
            }
        }

        reportGui.render(g2d);
    }

    @Override
    public void update(float delta) {
        scriptGUI.update();

        playButton.update();
        stopButton.update();

        if (isParsing) {
            if (scriptParser.isDone()) {
                isParsing = false;

                Report report;
                try {
                    report = scriptParser.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();

                    report = new Report("Internal error", -1, GameState.class);
                }

                reportGui.setReport(report);

                if (report.isSuccess()) {
                    isRunning = true;
                }
            }
        } else if (isRunning) {
            if (TheGreatMachine.getInstance().getTicks() % 30 == 0) {
                Level currentLevel = levelManager.getCurrentLevel();

                boolean levelFinished = currentLevel.check();

                if (ScriptThread.executeOneLine(currentLevel.getMap(), currentLevel.getRobot()) || levelFinished) {
                    isRunning = false;

                    if (levelFinished) {
                        System.out.println("LEVEL FINISHED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                }
            }
        }

        reportGui.update();
    }

    private void play() {
        playButton.setActive(false);
        stopButton.setActive(true);

        scriptParser = ScriptThread.parse(scriptGUI.getScript());
        isParsing = true;
    }

    private void stop() {
        playButton.setActive(true);
        stopButton.setActive(false);
    }
}