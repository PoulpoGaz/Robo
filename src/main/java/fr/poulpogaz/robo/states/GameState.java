package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.gui.*;
import fr.poulpogaz.robo.gui.Button;
import fr.poulpogaz.robo.level.Level;
import fr.poulpogaz.robo.level.LevelManager;
import fr.poulpogaz.robo.level.LevelRenderer;
import fr.poulpogaz.robo.main.TheGreatMachine;
import fr.poulpogaz.robo.robot.ExecuteReport;
import fr.poulpogaz.robo.robot.Report;
import fr.poulpogaz.robo.robot.ScriptThread;
import fr.poulpogaz.robo.utils.ResourceLocation;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static fr.poulpogaz.robo.main.TheGreatMachine.TILE_SIZE;

public class GameState extends State {

    private static final LevelManager levelManager = LevelManager.getInstance();
    private static final LevelRenderer levelRenderer = LevelRenderer.getInstance();

    private static final int SCRIPT_GUI_WIDTH = 192;
    private static final int LEVEL_RENDER_WIDTH = TheGreatMachine.WIDTH - SCRIPT_GUI_WIDTH;

    private final ScriptGUI scriptGUI;
    private final Button playButton;
    private final Button stopButton;
    private final Button showInfo;
    private final InfoGUI infoGui;

    private final ReportGui reportGui;
    private Future<Report> scriptParser;
    private boolean isParsing = false;
    private boolean isRunning = false;

    public GameState() {
        super("GameState");

        levelManager.initLevel();

        scriptGUI = new ScriptGUI();
        scriptGUI.setBounds(LEVEL_RENDER_WIDTH, 0, SCRIPT_GUI_WIDTH, TheGreatMachine.HEIGHT);

        infoGui = new InfoGUI();
        infoGui.setText(levelManager.getCurrentLevel().getDescription());

        playButton = new Button(new ResourceLocation("play", ResourceLocation.GUI_ELEMENT));
        playButton.setBounds(5, 5, TILE_SIZE, TILE_SIZE);
        playButton.setReleaseListener(this::play);

        stopButton = new Button(new ResourceLocation("stop", ResourceLocation.GUI_ELEMENT));
        stopButton.setBounds(15 + TILE_SIZE, 5, TILE_SIZE, TILE_SIZE);
        stopButton.setActive(false);
        stopButton.setReleaseListener(this::stop);

        showInfo = new Button(new ResourceLocation("info", ResourceLocation.GUI_ELEMENT));
        showInfo.setBounds(5, 15 + TILE_SIZE, TILE_SIZE, TILE_SIZE);
        showInfo.setReleaseListener(this::showInfo);

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
        showInfo.render(g2d);

        scriptGUI.render(g2d);

        if (isParsing || isRunning) {
            g2d.setColor(FontColor.FOREGROUND);

            if (isParsing) {
                g2d.drawString("Parsing...", 50, 15);
            } else if (isRunning) {
                g2d.drawString("Running...", 50, 15);
            }
        }

        reportGui.render(g2d);
        infoGui.render(g2d);
    }

    @Override
    public void update(float delta) {
        if (!infoGui.isVisible()) {
            scriptGUI.update();

            playButton.update();
            stopButton.update();
            showInfo.update();
        }

        if (isParsing) {
            parse();
        } else if (isRunning) {
            run();
        }

        reportGui.update();
        infoGui.update();
    }

    private void parse() {
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
    }

    private void run() {
        if (TheGreatMachine.getInstance().getTicks() % 30 == 0) {
            Level currentLevel = levelManager.getCurrentLevel();

            ExecuteReport report = ScriptThread.executeOneLine(currentLevel.getMap(), currentLevel.getRobot());
            boolean levelFinished = currentLevel.check();

            if (report.isEnd() || levelFinished) {
                isRunning = false;

                if (levelFinished) {
                    manager.exit();
                } else {
                    stop();
                }
            } else {
                scriptGUI.highlightLine(report.getLine());
            }
        }
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

        isRunning = false;
        scriptGUI.highlightLine(-1);
        levelManager.initLevel();
    }

    private void showInfo() {
        infoGui.setVisible(true);
    }
}