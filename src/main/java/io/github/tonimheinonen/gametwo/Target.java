package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.*;
import io.github.tonimheinonen.engine.tools.*;

public class Target extends GObject {

    private Game main;
    private boolean clicked;
    private int score;

    // Movement
    private double changeMovementTime = 2;
    private AnimatedImage playerDown, playerUp, playerLeft, playerRight;
    private double maxSpd = 7;
    private long lastMovement;

    // Timer
    private int timeleft = 10;
    private long lastSecond;

    // Spawn enemy timer
    private int spawnTime = 10;
    private long lastSpawn;

    public Target(Game main) {
        this.main = main;
    }

    @Override
    public void createEvent() {
        playerDown = spriteCreate("images/playerDown.png", 4, 1, 4, 48, 70, 10);
        playerUp = spriteCreate("images/playerUp.png", 4, 1, 4, 48, 70, 10);
        playerLeft = spriteCreate("images/playerLeft.png", 4, 1, 4, 48, 70, 10);
        playerRight = spriteCreate("images/playerRight.png", 4, 1, 4, 48, 70, 10);
        spriteSet(playerDown, true);
        int margin = 9;
        setBounds(margin, margin, playerDown.getFrameWidth() - margin * 2,
                     playerDown.getFrameHeight() - margin * 2);

        setX(randomRange(0, global().getWindowWidth()));
        setY(randomRange(0, global().getWindowHeight()));

        lastSecond = System.currentTimeMillis();
        lastSpawn = System.currentTimeMillis();
    }

    @Override
    public void stepEvent() {
        clicked = false;        // Reset clicked so that flash white works
        wrap(true, true, getWidth() / 2, getHeight() / 2);  // Set wrapping
        setDepth((int)-getY()); // Set depth depending on Y coordinate
        
        movementTimer();

        // Target hit
        if (mousePressed()) {
            clicked = true;
            score++;
            timeleft++;
            Audio.playSound("sounds/hit.wav", false);
        }

        spawnTimer();
        countdownTimer();
    }

    @Override
    public void drawEvent() {
        Draw.setTextSize(35);
        Draw.setColor(C_BLACK);
        drawTarget();
        drawScore();
        drawTimer();
    }

    private void drawTarget() {
        if (clicked)
            Draw.setHSL(0, 0, 1);
        drawSelf();
        Draw.setHSL(0, 0, 0);
    }

    private void drawScore() {
        double x = global().getWindowWidth()/2;
        double y = 50;
        Draw.text("Score: " + String.valueOf(score), x, y);
    }

    private void drawTimer() {
        Draw.setHorizontalAlign(HA_LEFT);
        Draw.text("Time: " + String.valueOf(timeleft), 50, 50);
        Draw.setHorizontalAlign(HA_CENTER);
    }

    private void movementTimer() {
        if (secondsPassed(lastMovement, changeMovementTime)) {
            lastMovement = System.currentTimeMillis();
            changeSpdAndDir();
        }
    }

    private void spawnTimer() {
        if (secondsPassed(lastSpawn, spawnTime)) {
            lastSpawn = System.currentTimeMillis();
            createInstance(0, 0, new Enemy(main));
        }
    }

    private void countdownTimer() {
        if (secondsPassed(lastSecond, 1)) {
            lastSecond = System.currentTimeMillis();
            timeleft--;

            if (timeleft <= 0) {
                main.gameEnded();
            }
        }
    }

    private void changeSpdAndDir() {
        // Movement and animation
        int x = 0, y = 0;

        while (x == 0 && y == 0) {
            x = randomRange(-1, 1);
            y = randomRange(-1, 1);
        }

        if (x == 1)
            spriteSet(playerRight, false);
        else if (x == -1)
            spriteSet(playerLeft, false);
        else if (y == 1)
            spriteSet(playerDown, false);
        else
            spriteSet(playerUp, false);


        setSpeed(randomRange(0, maxSpd));
        setDirection(calculateDirection(x, y));
    }

    /**
     * Calculates the angle in degrees where to move the player.
     * @param x 1 to -1 to decide which direction to move
     * @param y 1 to -1 to decide which direction to move
     */
    private double calculateDirection(int x, int y) {
        // I am bad at mathematics, so I can't figure out a better way to do this
        if (x == 1) {
            if (y == 1)
                return 45;
            else if (y == -1)
                return 315;
            else
                return 0;
        } else if (x == -1) {
            if (y == 1)
                return 135;
            else if (y == -1)
                return 225;
            else
                return 180;
        } else {
            if (y == 1)
                return 90;
            else if (y == -1)
                return 270;
        }

        return -1;
    }

    public int getScore() {
        return score;
    }
}