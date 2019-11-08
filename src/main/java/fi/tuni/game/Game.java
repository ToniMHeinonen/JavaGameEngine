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
    }


    int time = 0;
    @Override
    public void stepEvent() {
        time++;
        System.out.println("here");

        if (time > 500) {
            closeProgram();
        }
    }

    @Override
    public void drawEvent() {
        
    }
}