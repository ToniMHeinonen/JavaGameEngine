package fi.tuni.game;

import fi.tuni.engine.*;

public class Game extends GEngine {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void createEvent() {
        setWindowTitle("Game 1");
        setWindowSize(800, 600);
        Player one = createInstance(100, 50, Player.class);
        Player two = createInstance(200, 50, new Player(2, one));
    }


    @Override
    public void stepEvent() {
        
    }

    @Override
    public void drawEvent() {
        
    }
}