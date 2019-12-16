package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.*;
import io.github.tonimheinonen.engine.tools.*;

public class Game extends GEngine {

    private Target target;

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
        //setBackgroundImage("images/background.png");
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
       
    }

    public void createMenu() {
        double space = 200;
        double middleX = getWindowWidth() / 2;
        createInstance(middleX, space, new MenuButton(this, "Play"));
        createInstance(middleX, space * 2, new MenuButton(this, "Highscores"));
        createInstance(middleX, space * 3, new MenuButton(this, "Exit"));
    }

    public void play() {
        destroyInstance(MenuButton.class);

        // Create enemies
        for (int i = 0; i < 10; i++) {
            createInstance(0, 0, new Enemy(this));
        }

        // Create target
        target = createInstance(0, 0, new Target(this));
    }

    public void showHighscores() {
        destroyInstance(MenuButton.class);
        createInstance(0, 0, new ScoreScreen(this));
    }

    public void exit() {
        closeProgram();
    }

    public void gameEnded() {
        destroyInstance(Target.class);
        destroyInstance(Enemy.class);

        Highscore.addScore("", target.getScore());
        showHighscores();
    }
}