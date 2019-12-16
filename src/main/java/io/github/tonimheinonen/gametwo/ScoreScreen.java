package io.github.tonimheinonen.gametwo;

import java.util.ArrayList;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class ScoreScreen extends GObject {

    private Game main;
    private ArrayList<Double> scores;
    private double middleX;
    private double space = 50;
    private double startY = 100;

    public ScoreScreen(Game main) {
        this.main = main;
    }

    @Override
    public void createEvent() {
        createInstance(100, 100, new MenuButton(main, "Back"));
        Highscore.sortByDescendingOrder(true);
        scores = Highscore.getScores();
        middleX = global().getWindowWidth() / 2;
    }

    @Override
    public void stepEvent() {

    }

    @Override
    public void drawEvent() {
        Draw.setColor(C_ORANGE);
        Draw.setTextSize(35);
        if (scores.isEmpty()) {
            Draw.text("No scores yet!", middleX, 200);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String number = String.valueOf(i + 1);
                String score = String.valueOf(Math.round(scores.get(i)));
                Draw.text(number + ": " + score , middleX, startY + (space * i));
            }
        }
    }
}