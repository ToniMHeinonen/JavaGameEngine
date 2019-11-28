package fi.tuni.game;

import fi.tuni.engine.GObject;
import fi.tuni.engine.tools.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player extends GObject {

    private int playerSlot = 1;
    private Image img;
    private AnimatedImage playerDown, playerUp, playerLeft, playerRight;
    private GObject target;

    public Player() {}

    public Player(int playerSlot, GObject target) {
        this.playerSlot = playerSlot;
        this.target = target;
    }

    @Override
    public void createEvent() {
        Image img = spriteCreate("images/player.png");
        this.img = img;

        playerDown = spriteCreate("images/playerDown.png", 4, 1, 4, 48, 70, 10);
        playerUp = spriteCreate("images/playerUp.png", 4, 1, 4, 48, 70, 10);
        playerLeft = spriteCreate("images/playerLeft.png", 4, 1, 4, 48, 70, 10);
        playerRight = spriteCreate("images/playerRight.png", 4, 1, 4, 48, 70, 10);
        spriteSet(playerDown);
    }

    @Override
    public void stepEvent() {
        // Player 1 movement
        if (playerSlot == 1) {
            if (Input.isKeyPressedHold("RIGHT")) {
                spriteSet(playerRight);
                setX(getX() + 1);
            }
            if (Input.isKeyPressedHold("LEFT")) {
                spriteSet(playerLeft);
                setX(getX() - 1);
            }
            if (Input.isKeyPressedHold("DOWN")) {
                spriteSet(playerDown);
                setY(getY() + 1);
            }
            if (Input.isKeyPressedHold("UP")) {
                spriteSet(playerUp);
                setY(getY() - 1);
            }

            if (Input.isKeyPressedHold("R")) {
                spriteSet(img);
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
            spriteSpeed(getCurrentAnimation().getFps() + 1);

        if (Input.isKeyPressed("K"))
            spriteSpeed(getCurrentAnimation().getFps() - 1);

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
        drawBounds(1, Color.RED);
        drawSelf();
    }
}