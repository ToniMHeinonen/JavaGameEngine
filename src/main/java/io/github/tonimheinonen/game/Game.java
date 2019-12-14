package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.*;
import io.github.tonimheinonen.engine.tools.*;

public class Game extends GEngine {

    private Player one, two;
    private int maxScore = 5;
    private int oneScore, twoScore;
    private boolean gameOver;
    private String winner;

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
        Audio.playSound("music/music.mp3", true);
        startGame();
    }

    /**
     * Runs every frame.
     */
    @Override
    public void stepEvent() {
        boolean oneWon = one.getCoinsCollected() >= maxScore;
        boolean twoWon = two.getCoinsCollected() >= maxScore;
        
        if (oneWon || twoWon && !gameOver) {
            oneScore = one.getCoinsCollected();
            twoScore = two.getCoinsCollected();
            destroyInstance(Player.class);
            destroyInstance(Coin.class);
            gameOver = true;

            if (oneWon && twoWon) {
                winner = "DRAW!";
            } else if (oneWon) {
                winner = "PLAYER ONE WON!";
            } else if (twoWon) {
                winner = "PLAYER TWO WON!";
            }
        }
    }

    /**
     * Draws every frame.
     */
    @Override
    public void drawEvent() {
        drawGameOver();
    }

    private void startGame() {
        one = createInstance(getWindowWidth() / 3, getWindowHeight() / 2, Player.class);
        two = createInstance(getWindowWidth() / 3 * 2, getWindowHeight() / 2, new Player(2));
        Coin coin = createInstance(getWindowWidth() / 2, 50, Coin.class);
        gameOver = false;
    }

    /**
     * Draws game over screen.
     */
    private void drawGameOver() {
        if (gameOver) {
            int space = 50;
            Draw.setTextSize(35);
            Draw.setColor(C_RED);
            Draw.setHorizontalAlign(HA_CENTER);
            Draw.setVerticalAlign(VA_CENTER);
            double wCenter = getWindowWidth() / 2;
            double hCenter = getWindowHeight() / 2;
            Draw.text("GAME OVER", wCenter, hCenter - space);
            Draw.text(winner, wCenter, hCenter);
            Draw.text("Player one score: " + oneScore, wCenter, hCenter + space*2);
            Draw.text("Player two score: " + twoScore, wCenter, hCenter + space*3);
        }
    }

    public void restartGame() {
        startGame();
    }
}