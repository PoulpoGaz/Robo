package fr.poulpogaz.robo.states;

import fr.poulpogaz.robo.gui.*;
import fr.poulpogaz.robo.level.CheckReport;
import fr.poulpogaz.robo.level.Level;
import fr.poulpogaz.robo.level.LevelRenderer;
import fr.poulpogaz.robo.main.Robo;
import fr.poulpogaz.robo.robot.ExecuteReport;
import fr.poulpogaz.robo.robot.Report;
import fr.poulpogaz.robo.robot.ScriptThread;
import fr.poulpogaz.robo.timeline.Timeline;
import fr.poulpogaz.robo.utils.ResourceLocation;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static fr.poulpogaz.robo.main.Robo.HEIGHT;
import static fr.poulpogaz.robo.main.Robo.TILE_SIZE;

public class GameState extends State {

    private static final Timeline timeline = Timeline.getInstance();
    private static final LevelRenderer levelRenderer = LevelRenderer.getInstance();

    private static final int SCRIPT_GUI_WIDTH = 192;
    private static final int LEVEL_RENDER_WIDTH = Robo.WIDTH - SCRIPT_GUI_WIDTH;

    private final ScriptGUI scriptGUI;
    private final TexturedButton playButton;
    private final TexturedButton stopButton;
    private final TexturedButton showInfo;
    private final TexturedButton backButton;
    private final GUIBox infoGui;
    private final GUIBox levelFinished;
    private final GUIBox levelFailed;

    private final StringButton speedButton;
    private int speed = 1;

    private final ReportGui reportGui;
    private Future<Report> scriptParser;
    private boolean isParsing = false;
    private boolean isRunning = false;

    private Level currentLevel;

    public GameState() {
        scriptGUI = new ScriptGUI();
        scriptGUI.setBounds(LEVEL_RENDER_WIDTH, 0, SCRIPT_GUI_WIDTH, Robo.HEIGHT);

        infoGui = new GUIBox();
        infoGui.setTitle("** INFO **");
        infoGui.addButton(new StringButton("OK", () -> infoGui.setVisible(false)));

        levelFinished = new GUIBox();
        levelFinished.setTitle("Level finished!");
        levelFinished.addButton(new StringButton("OK", this::levelFinished));

        levelFailed = new GUIBox();
        levelFailed.setTitle("Level failed...");
        levelFailed.addButton(new StringButton("OK", () -> {
            stop();
            levelFailed.setVisible(false);
        }));

        boxes.add(infoGui);
        boxes.add(levelFailed);
        boxes.add(levelFinished);

        playButton = new TexturedButton(new ResourceLocation("play", ResourceLocation.GUI_ELEMENT));
        playButton.setBounds(5, 5, TILE_SIZE, TILE_SIZE);
        playButton.setReleaseListener(this::play);

        stopButton = new TexturedButton(new ResourceLocation("stop", ResourceLocation.GUI_ELEMENT));
        stopButton.setBounds(15 + TILE_SIZE, 5, TILE_SIZE, TILE_SIZE);
        stopButton.setActive(false);
        stopButton.setReleaseListener(this::stop);

        showInfo = new TexturedButton(new ResourceLocation("info", ResourceLocation.GUI_ELEMENT));
        showInfo.setBounds(5, 15 + TILE_SIZE, TILE_SIZE, TILE_SIZE);
        showInfo.setReleaseListener(() -> infoGui.setVisible(true));

        backButton = new TexturedButton(new ResourceLocation("back", ResourceLocation.GUI_ELEMENT));
        backButton.setBounds(5, HEIGHT - TILE_SIZE - 5, TILE_SIZE, TILE_SIZE);
        backButton.setReleaseListener(this::back);

        reportGui = new ReportGui();
        reportGui.setX(stopButton.getX() + stopButton.getWidth() + 60);
        reportGui.setY(5);
        reportGui.setMaxWidth(200);

        speedButton = new StringButton();
        speedButton.setX(5);
        speedButton.setY(20 + 2 *TILE_SIZE);
        speedButton.setReleaseListener(this::increaseSpeed);

        levelRenderer.init();
    }

    @Override
    public void show() {
        currentLevel = (Level) timeline.getCurrentNode();
        currentLevel.init();

        levelFinished.setVisible(false);
        levelFailed.setVisible(false);

        infoGui.setText(currentLevel.getDescription());
        infoGui.setVisible(true);

        speed = 1;
        speedButton.setText("Speed x" + speed);

        scriptGUI.reset();

        if (currentLevel.getScript() != null) {
            scriptGUI.setScript(currentLevel.getScript());
        } else {
            scriptGUI.setScript("");
        }
    }

    @Override
    protected void renderBackground(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, LEVEL_RENDER_WIDTH, HEIGHT);

        levelRenderer.render(g2d, currentLevel, LEVEL_RENDER_WIDTH, HEIGHT, levelFailed.isVisible(), levelFinished.isVisible(), isRunning); // :/
        playButton.render(g2d);
        stopButton.render(g2d);
        showInfo.render(g2d);
        backButton.render(g2d);
        speedButton.render(g2d);

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
    }

    @Override
    public void update(float delta) {
        if (!updateGUIBoxes()) {
            scriptGUI.update();

            playButton.update();
            stopButton.update();
            showInfo.update();
            backButton.update();
            speedButton.update();
        }

        if (isParsing) {
            parse();
        } else if (isRunning) {
            run();
        }

        reportGui.update();
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

            if (report.isSuccess()) {
                isRunning = true;
            } else {
                stop();
            }

            reportGui.setReport(report);
        }
    }

    private void run() {
        if (Robo.getInstance().getTicks() % (30 - speed) == 0) {
            ExecuteReport report = ScriptThread.executeOneLine(currentLevel.getMap(), currentLevel.getRobot());

            if (report.getError() != null) {
                // ERROR :/

                stop();
                reportGui.setReport(new Report(report));
            } else {
                CheckReport checkReport = currentLevel.check();

                if (!report.isEnd()) {
                    scriptGUI.highlightLine(report.getLine());
                }

                if (checkReport != CheckReport.OK || report.isEnd()) {
                    isRunning = false;

                    if (checkReport == CheckReport.LEVEL_FINISHED) {
                        levelFinished.setVisible(true);
                    } else if (checkReport == CheckReport.LEVEL_FAILED || report.isEnd()) {
                        levelFailed.setVisible(true);
                    }
                }
            }
        }
    }

    private void play() {
        playButton.setActive(false);
        stopButton.setActive(true);

        scriptParser = ScriptThread.parse(scriptGUI.getScript(), currentLevel.getAvailableOperations());
        isParsing = true;
    }

    private void stop() {
        playButton.setActive(true);
        stopButton.setActive(false);

        scriptGUI.highlightLine(-1);
        currentLevel.init();

        if (isParsing) {
            if (!scriptParser.isDone()) {
                scriptParser.cancel(true);
                scriptParser = null;
            }
        }

        isRunning = false;
        isParsing = false;

        reportGui.setVisible(false);
    }

    private void levelFinished() {
        timeline.nextNode();
        back();
    }

    private void back() {
        stop();

        currentLevel.setScript(scriptGUI.getScript());

        timeline.swap();
    }

    private void increaseSpeed() {
        speed += 6;

        if (speed > 26) {
            speed = 1;
        }

        speedButton.setText("Speed x" + speed);
    }

    @Override
    public void hide() {
        currentLevel.clear();
    }
}