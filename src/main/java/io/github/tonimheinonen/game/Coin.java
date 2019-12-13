package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;

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
            destroyInstance(this);
            int x = randomRange(0, global().getWindowWidth() - (int)getWidth());
            int y = randomRange(0, global().getWindowHeight() - (int)getHeight());
            createInstance(x, y, Coin.class);
        }
    }

    /**
     * Draws the coin and collision border.
     */
    @Override
    public void drawEvent() {
        drawSelf();
    }
}