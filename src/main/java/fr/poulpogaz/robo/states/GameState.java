package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.gui.*;
import fr.poulpogaz.robo.level.Level;
import fr.poulpogaz.robo.level.LevelManager;
import fr.poulpogaz.robo.level.LevelRenderer;
import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.robot.ExecuteReport;
import fr.poulpogaz.robo.robot.Report;
import fr.poulpogaz.robo.robot.ScriptThread;
import fr.poulpogaz.robo.utils.ResourceLocation;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static fr.poulpogaz.robo.main.Robo.HEIGHT;
import static fr.poulpogaz.robo.main.Robo.TILE_SIZE;

public class GameState extends State {

    private static final LevelManager levelManager = LevelManager.getInstance();
    private static final LevelRenderer levelRenderer = LevelRenderer.getInstance();

    private static final int SCRIPT_GUI_WIDTH = 192;
    private static final int LEVEL_RENDER_WIDTH = Robo.WIDTH - SCRIPT_GUI_WIDTH;

    private final ScriptGUI scriptGUI;
    private final TexturedButton playButton;
    private final TexturedButton stopButton;
    private final TexturedButton showInfo;
    private final TexturedButton backButton;
    private final GUIBox infoGui;

    private final ReportGui reportGui;
    private Future<Report> scriptParser;
    private boolean isParsing = false;
    private boolean isRunning = false;

    public GameState() {
        super("GameState");

        scriptGUI = new ScriptGUI();
        scriptGUI.setBounds(LEVEL_RENDER_WIDTH, 0, SCRIPT_GUI_WIDTH, Robo.HEIGHT);

        infoGui = new GUIBox();
        infoGui.setTitle("** INFO **");
        infoGui.addButton(new StringButton("OK", () -> infoGui.setVisible(false)));

        playButton = new TexturedButton(new ResourceLocation("play", ResourceLocation.GUI_ELEMENT));
        playButton.setBounds(5, 5, TILE_SIZE, TILE_SIZE);
        playButton.setReleaseListener(this::play);

        stopButton = new TexturedButton(new ResourceLocation("stop", ResourceLocation.GUI_ELEMENT));
        stopButton.setBounds(15 + TILE_SIZE, 5, TILE_SIZE, TILE_SIZE);
        stopButton.setActive(false);
        stopButton.setReleaseListener(this::stop);

        showInfo = new TexturedButton(new ResourceLocation("info", ResourceLocation.GUI_ELEMENT));
        showInfo.setBounds(5, 15 + TILE_SIZE, TILE_SIZE, TILE_SIZE);
        showInfo.setReleaseListener(this::showInfo);

        backButton = new TexturedButton(new ResourceLocation("back", ResourceLocation.GUI_ELEMENT));
        backButton.setBounds(5, HEIGHT - TILE_SIZE - 5, TILE_SIZE, TILE_SIZE);
        backButton.setReleaseListener(this::back);

        reportGui = new ReportGui();
        reportGui.setX(stopButton.getX() + stopButton.getWidth() + 60);
        reportGui.setY(5);
        reportGui.setMaxWidth(200);
    }

    @Override
    public void show() {
        levelManager.initLevel();

        Level level = levelManager.getCurrentLevel();

        infoGui.setText(level.getDescription());

        if (level.getScript() != null) {
            scriptGUI.setScript(level.getScript());
        }
    }

    @Override
    protected void renderBackground(Graphics2D g2d) {
        levelRenderer.render(g2d, levelManager.getCurrentLevel(), LEVEL_RENDER_WIDTH, Robo.HEIGHT);
        playButton.render(g2d);
        stopButton.render(g2d);
        showInfo.render(g2d);
        backButton.render(g2d);

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
            backButton.update();
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
        if (Robo.getInstance().getTicks() % 30 == 0) {
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

        if (isRunning) {
            isRunning = false;
            scriptGUI.highlightLine(-1);
            levelManager.initLevel();
        } else if (isParsing) {
            if (!scriptParser.isDone()) {
                scriptParser.cancel(true);
            }
        }

        reportGui.setVisible(false);
    }

    private void showInfo() {
        infoGui.setVisible(true);
    }

    private void back() {
        if (isRunning || isParsing) {
            stop();
        }

        levelManager.getCurrentLevel().setScript(scriptGUI.getScript());

        manager.switchState(MainMenu.class);
    }

    @Override
    public void hide() {
        levelManager.save();
    }
}