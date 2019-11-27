package fi.tuni.game;

import fi.tuni.engine.GObject;
import fi.tuni.engine.tools.*;

public class Coin extends GObject {

    @Override
    public void createEvent() {
        spriteCreate("images/Coin.png");
        spriteResize(32, 32);
    }

    @Override
    public void stepEvent() {
        if (collidesWith(Player.class)) {
            System.out.println("collect");
            destroyInstance(this);
        }
    }

    @Override
    public void drawEvent() {
        drawBounds(1);
        drawSelf();
    }
}