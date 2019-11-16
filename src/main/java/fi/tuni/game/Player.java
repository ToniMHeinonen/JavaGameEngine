package fi.tuni.game;

import fi.tuni.engine.*;

public class Player extends GObject {

    private int playerSlot = 1;

    public Player() {}

    public Player(int playerSlot) {
        this.playerSlot = playerSlot;
    }

    @Override
    public void createEvent() {
        spriteCreate("images/player.png");
        spriteResize(32, 32);
    }

    @Override
    public void stepEvent() {
        if (playerSlot == 1) {
            if (isKeyPressedHold("RIGHT")) {
                setX(getX() + 1);
            }
            if (isKeyPressedHold("LEFT")) {
                setX(getX() - 1);
            }
        }

        if (playerSlot == 2) {
            if (isKeyPressedHold("D")) {
                setX(getX() + 1);
            }
            if (isKeyPressedHold("A")) {
                setX(getX() - 1);
            }
        }

        if (isKeyPressed("U"))
            System.out.println("Pressed");
        
        if (isKeyPressedHold("U"))
            System.out.println("Hold");

        if (isKeyReleased("U"))
            System.out.println("Released");

        if (collidesWith(Player.class)) {
            System.out.println("Hit");
        }
    }

    @Override
    public void drawEvent() {
        drawBounds(1);
        spriteDraw();
    }
}