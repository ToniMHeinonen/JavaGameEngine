package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.*;
import io.github.tonimheinonen.engine.tools.*;

public class Game extends GEngine {

    private Target target;
    private double middleX;
    private double middleY;
    private double ySpace;

    private int MENU = 0, PLAY = 1, SCORE = 2, GAMEOVER = 3;
    private int state = MENU;

    /**
     * Launches the game.
     * @param args given arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initialize all the necessary values and instances.
     */
    @Override
    public void createEvent() {
        setWindowTitle("Game 2");
        setWindowSize(1024, 800);
        middleX = getWindowWidth() / 2;
        middleY = getWindowHeight() / 2;
        ySpace = getWindowHeight() / 5;
        setBackgroundImage("images/bawback.jpg");
        Audio.playSound("music/music.mp3", true);

        createMenu();
    }

    /**
     * Runs every frame.
     */
    @Override
    public void stepEvent() {

    }

    /**
     * Draws every frame.
     */
    @Override
    public void drawEvent() {
        drawLogo();
        drawGameOver();
    }

    private void drawLogo() {
        if (state == MENU) {
            Draw.setTextSize(50);
            Draw.setColor(C_ORANGE);
            Draw.setHorizontalAlign(HA_CENTER);
            Draw.setVerticalAlign(VA_CENTER);
            Draw.text("CLICK THE TARGET", middleX, ySpace);
        }
    }

    private void drawGameOver() {
        if (state == GAMEOVER) {
            int space = 100;
            Draw.setTextSize(50);
            Draw.setColor(C_ORANGE);
            Draw.setHorizontalAlign(HA_CENTER);
            Draw.setVerticalAlign(VA_CENTER);
            Draw.text("GAME OVER", middleX, middleY - space);
            Draw.text("Your score: " + String.valueOf(target.getScore()), middleX, middleY);
        }
    }

    public void createMenu() {
        state = MENU;
        createInstance(middleX, ySpace * 2, new MenuButton(this, "Play"));
        createInstance(middleX, ySpace * 3, new MenuButton(this, "Highscores"));
        createInstance(middleX, ySpace * 4, new MenuButton(this, "Exit"));
    }

    public void play() {
        state = PLAY;
        destroyInstance(MenuButton.class);

        // Create enemies
        for (int i = 0; i < 10; i++) {
            createInstance(0, 0, new Enemy(this));
        }

        // Create target
        target = createInstance(0, 0, new Target(this));
    }

    public void showHighscores() {
        state = SCORE;
        destroyInstanceAll();
        createInstance(0, 0, new ScoreScreen(this));
    }

    public void gameEnded() {
        state = GAMEOVER;
        destroyInstance(Target.class);
        destroyInstance(Enemy.class);
        Highscore.addScore("", target.getScore());
        createInstance(middleX, middleY + 200, new MenuButton(this, "Results"));
    }

    public void exit() {
        closeProgram();
    }
}