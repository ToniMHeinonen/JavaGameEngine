package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.*;
import io.github.tonimheinonen.engine.tools.Audio;

public class Enemy extends GObject {

    private ClickTheTarget main;

    // Size
    private double size;
    private double maxSize = 2;
    private double minSize = 0.5;
    private double sizeSpd = 0.01;
    private boolean increase;

    /**
     * Gets main class reference.
     * @param main engine class
     */
    public Enemy(ClickTheTarget main) {
        this.main = main;
    }

    /**
     * Creates
     */
    @Override
    public void createEvent() {
        String[] sprites = new String[] {"red.png", "green.png", "blue.png"};

        // Select random sprite for the enemy
        String sprite = sprites[randomRange(0, sprites.length - 1)];
        spriteSet(spriteCreate("images/" + sprite, 3, 1, 3, 64, 64, 5), true);

        // Set random x and y position
        setX(randomRange(0, global().getWindowWidth()));
        setY(randomRange(0, global().getWindowHeight()));

        setSpeed(2);                        // Set speed for enemy
        setDirection(randomRange(0, 359));  // Set random direction
        size = randomRange(0.5, 1.5);       // Set random starting size
    }

    /**
     * Run these every frame.
     */
    @Override
    public void stepEvent() {
        // Teleport enemy to the other side
        wrap(true, true, getWidth() / 2, getHeight() / 2);

        setDepth((int)-getY()); // Set drawing depth depending on y

        resize();               // Pulsate enemy size

        // End game if enemy is clicked
        if(mousePressed()) {
            Audio.playSound("sounds/ouch.wav", false);
            main.gameEnded();
        }
    }

    /**
     * Draw self.
     */
    @Override
    public void drawEvent() {
        drawSelf();
    }

    /**
     * Pulsate enemy size.
     */
    private void resize() {
        spriteResize(size);

        if (increase) {
            if (size < maxSize)
                size += sizeSpd;
            else
                increase = false;
        } else {
            if (size > minSize)
                size -= sizeSpd;
            else
                increase = true;
        }
    }
}