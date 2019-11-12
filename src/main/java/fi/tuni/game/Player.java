package fi.tuni.game;

import fi.tuni.engine.*;

public class Player extends GObject {

    @Override
    public void createEvent() {
        spriteCreate("player.png");
        spriteResize(32, 32);
    }

    @Override
    public void stepEvent() {
        if (isKeyPressed("RIGHT"))
            setX(getX() + 1);
        if (isKeyPressed("A"))
            setX(getX() - 1);

        if (isKeyReleased("LEFT"))
            System.out.println("Yes");
    }

    @Override
    public void drawEvent() {
        spriteDraw();
    }
    
}