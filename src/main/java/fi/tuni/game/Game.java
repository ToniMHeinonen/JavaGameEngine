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
        createObject(100, 50, new Player());
    }


    @Override
    public void stepEvent() {
        
    }

    @Override
    public void drawEvent() {
        
    }
}