package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.*;
import io.github.tonimheinonen.engine.tools.*;

public class CoinFight extends GEngine {

    private Player one, two;
    private int targetScore = 10;
    private int oneScore, twoScore;
    private boolean gameOver;
    private String winner;

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
        setWindowTitle("Game 1");
        setWindowSize(800, 608);
        setBackgroundImage("images/background.png");
        Audio.playSound("music/music.mp3", true);
        startGame();
    }

    /**
     * Runs every frame.
     */
    @Override
    public void stepEvent() {
        boolean oneWon = one.getCoinsCollected() >= targetScore;
        boolean twoWon = two.getCoinsCollected() >= targetScore;
        
        if (!gameOver && (oneWon || twoWon)) {
            oneScore = one.getCoinsCollected();
            twoScore = two.getCoinsCollected();
            destroyInstance(Player.class);
            destroyInstance(Coin.class);
            gameOver = true;

            createInstance(getWindowWidth() / 2, 
                getWindowHeight() / 2 + 200, new RestartBtn(this));

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

    /**
     * Used for starting and restarting the game.
     */
    private void startGame() {
        targetScore += 10;
        one = createInstance(getWindowWidth() / 3, getWindowHeight() / 2, Player.class);
        two = createInstance(getWindowWidth() / 3 * 2, getWindowHeight() / 2, new Player(2));
        createInstance(getWindowWidth() / 2, 50, Coin.class);
        gameOver = false;
    }

    /**
     * Draws game over screen.
     */
    private void drawGameOver() {
        if (gameOver) {
            int space = 50;
            Draw.setTextSize(35);
            Draw.setColor(C_WHITE);
            Draw.setHorizontalAlign(HA_CENTER);
            Draw.setVerticalAlign(VA_CENTER);
            double wCenter = getWindowWidth() / 2;
            double startY = 200;
            Draw.text("GAME OVER", wCenter, startY);
            Draw.text(winner, wCenter, startY + space);
            Draw.text("Player one score: " + oneScore, wCenter, startY + space*2);
            Draw.text("Player two score: " + twoScore, wCenter, startY + space*3);
        }
    }

    /**
     * Restarts the game.
     */
    public void restartGame() {
        startGame();
    }
}