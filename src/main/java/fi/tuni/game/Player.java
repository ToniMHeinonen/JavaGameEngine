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
        if (isKeyPressed("RIGHT")) {
            System.out.println("RIGHT");
            setX(getX() + 1);
        }
        if (isKeyPressed("LEFT")) {
            System.out.println("LEFT");
            setX(getX() - 1);
        }

        if (isKeyPressed("U"))
            System.out.println("Pressed");
        
        if (isKeyPressedHold("U"))
            System.out.println("Hold");

        if (isKeyReleased("U"))
            System.out.println("Released");
    }

    @Override
    public void drawEvent() {
        spriteDraw();
    }
    
}