package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.*;
import io.github.tonimheinonen.engine.tools.*;

public class Game extends GEngine {

    /**
     * Launches game.
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
        setWindowTitle("Game 1");
        setWindowSize(800, 600);
        setBackgroundImage("images/background.jpg");
        Player one = createInstance(100, 50, Player.class);
        //Player two = createInstance(200, 50, new Player(2, one));
        Coin coin = createInstance(300, 50, Coin.class);
        Audio.playSound("music/music.mp3", true);
        GButton btn = createButton("text", 10, 10, MenuButton.class);
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
}