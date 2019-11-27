package fi.tuni.game;

import fi.tuni.engine.GEngine;
import fi.tuni.engine.tools.*;

public class Game extends GEngine {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void createEvent() {
        setWindowTitle("Game 1");
        setWindowSize(800, 600);
        Player one = createInstance(100, 50, Player.class);
        //Player two = createInstance(200, 50, new Player(2, one));
        Coin coin = createInstance(300, 50, Coin.class);
    }


    @Override
    public void stepEvent() {
        
    }

    @Override
    public void drawEvent() {
        
    }
}