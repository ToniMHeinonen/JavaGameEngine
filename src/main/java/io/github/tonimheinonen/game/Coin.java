package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;
import javafx.scene.paint.Color;

public class Coin extends GObject {

    /**
     * Creates necessary sprite.
     */
    @Override
    public void createEvent() {
        spriteCreate("images/Coin.png");
        spriteResize(32, 32);
    }

    /**
     * Check for collision with the player.
     */
    @Override
    public void stepEvent() {
        if (collidesWith(Player.class)) {
            System.out.println("collect");
            destroyInstance(this);
        }
    }

    /**
     * Draws the coin and collision border.
     */
    @Override
    public void drawEvent() {
        drawBounds(1, Color.GREEN);
        drawSelf();
    }
}