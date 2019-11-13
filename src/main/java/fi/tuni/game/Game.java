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
        Player one = createObject(100, 50, Player.class);
        Player two = createObject(200, 50, new Player(2));
    }


    @Override
    public void stepEvent() {
        
    }

    @Override
    public void drawEvent() {
        
    }
}