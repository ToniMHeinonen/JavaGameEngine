package fi.tuni.game;

import fi.tuni.engine.GObject;
import fi.tuni.engine.tools.*;

public class Player extends GObject {

    private int playerSlot = 1;
    private GObject target;

    public Player() {}

    public Player(int playerSlot, GObject target) {
        this.playerSlot = playerSlot;
        this.target = target;
    }

    @Override
    public void createEvent() {
        spriteCreate("images/player.png");
        spriteResize(32, 32);
    }

    @Override
    public void stepEvent() {
        if (playerSlot == 1) {
            if (Input.isKeyPressedHold("RIGHT")) {
                setX(getX() + 1);
            }
            if (Input.isKeyPressedHold("LEFT")) {
                setX(getX() - 1);
            }
        }

        if (playerSlot == 2) {
            if (Input.isKeyPressedHold("D")) {
                setX(getX() + 1);
            }
            if (Input.isKeyPressedHold("A")) {
                setX(getX() - 1);
            }
        }

        if (Input.isKeyPressed("I"))
            destroyInstance(Player.class);

        if (Input.isKeyPressed("O")) {
            if (playerSlot == 1)
                destroyInstance(this);
        }

        if (Input.isKeyPressed("U"))
            System.out.println("Pressed");
        
        if (Input.isKeyPressedHold("U"))
            System.out.println("Hold");

        if (Input.isKeyReleased("U"))
            System.out.println("Released");

        /*if (collidesWith(Player.class)) {
            System.out.println("Hit");
            for (GObject o : getCollidedObjects()) {
                destroyInstance(o);
            }
        }*/

        if (collidesWith(target)) {
            System.out.println("Target found");
            destroyInstance(getOther());
            target = null;
        }
    }

    @Override
    public void drawEvent() {
        drawBounds(1);
        spriteDraw();
    }
}