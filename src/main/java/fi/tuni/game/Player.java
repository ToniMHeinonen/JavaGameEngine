package fi.tuni.game;

import fi.tuni.engine.GObject;
import fi.tuni.engine.tools.*;
import javafx.scene.image.Image;

public class Player extends GObject {

    private int playerSlot = 1;
    private GObject target;
    private Animation idle;

    public Player() {}

    public Player(int playerSlot, GObject target) {
        this.playerSlot = playerSlot;
        this.target = target;
    }

    @Override
    public void createEvent() {
        Image img = spriteCreate("images/player.png");
        //spriteResize(32, 32);

        AnimatedImage idle = spriteCreate("images/playerAnim.png", 4, 1, 4, 320, 320, 20);
        spriteSet(idle);
        spriteResize(32, 32);
    }

    @Override
    public void stepEvent() {
        // Player 1 movement
        if (playerSlot == 1) {
            if (Input.isKeyPressedHold("RIGHT")) {
                setX(getX() + 1);
            }
            if (Input.isKeyPressedHold("LEFT")) {
                setX(getX() - 1);
            }
        }

        // Player 2 movement
        if (playerSlot == 2) {
            if (Input.isKeyPressedHold("D")) {
                setX(getX() + 1);
            }
            if (Input.isKeyPressedHold("A")) {
                setX(getX() - 1);
            }
        }

        // Change animation speed
        if (Input.isKeyPressed("J"))
            idle.setFps(idle.getFps() - 1);

        if (Input.isKeyPressed("K"))
            idle.setFps(idle.getFps() + 1);

        // Destroy classes
        if (Input.isKeyPressed("I"))
            destroyInstance(Player.class);

        if (Input.isKeyPressed("O")) {
            if (playerSlot == 1)
                destroyInstance(this);
        }

        // Test input methods
        if (Input.isKeyPressed("U"))
            System.out.println("Pressed");
        
        if (Input.isKeyPressedHold("U"))
            System.out.println("Hold");

        if (Input.isKeyReleased("U"))
            System.out.println("Released");

        // Test collisions
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
        drawSelf();
    }
}