package io.github.tonimheinonen.gametwo;

import java.util.ArrayList;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class ScoreScreen extends GObject {

    private Game main;
    private ArrayList<Double> scores;
    private double middleX;
    private double space = 100;
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
        Draw.setTextSize(50);
        if (scores.isEmpty()) {
            Draw.setColor(C_WHITE);
            Draw.text("No scores yet!", middleX, 200);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                if (i > 6)
                    break;
                String number = String.valueOf(i + 1);
                String score = String.valueOf(Math.round(scores.get(i)));

                double drawY = startY + (space * i);

                // Draw rectangle
                Draw.setColor(C_BLACK);
                double xOffset = 100;
                double yOffset = 40;
                Draw.rectangle(middleX - xOffset, drawY - yOffset,
                                middleX + xOffset, drawY + yOffset);

                // Draw score
                Draw.setColor(C_WHITE);
                Draw.text(number + ": " + score , middleX, drawY);
            }
        }
    }
}