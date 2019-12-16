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
        setWindowSize(800, 608);
        setBackgroundImage("images/background.png");
        Audio.playSound("music/music.mp3", true);

        for (int i = 0; i < 10; i++) {
            createInstance(0, 0, new Enemy(this));
        }

        target = createInstance(0, 0, Target.class);
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

    public void enemyClicked() {

    }
}