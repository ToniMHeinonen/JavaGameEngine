package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.*;
import io.github.tonimheinonen.engine.tools.*;

public class Target extends GObject {

    private AnimatedImage playerDown, playerUp, playerLeft, playerRight;
    private double maxSpd = 7;
    private long lastMovement;
    private double changeMovementTime = 2;

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
    }

    @Override
    public void stepEvent() {
        wrap(true, true, getWidth() / 2, getHeight() / 2);
        setDepth((int)-getY());
        
        if (secondsPassed(lastMovement, changeMovementTime)) {
            lastMovement = System.currentTimeMillis();
            changeSpdAndDir();
        }

        if (mousePressed()) {

        }
    }

    @Override
    public void drawEvent() {
        drawSelf();
    }

    public void changeSpdAndDir() {
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
}