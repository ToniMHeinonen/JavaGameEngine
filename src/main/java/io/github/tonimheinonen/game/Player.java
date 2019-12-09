package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player extends GObject {

    private int playerSlot = 1;
    private Image img;
    private AnimatedImage whole, playerDown, playerUp, playerLeft, playerRight;
    private GObject target;

    /**
     * Default constructor for intstanting.
     */
    public Player() {}

    /**
     * Gets playerslot and target to look out for.
     * @param playerSlot player number
     * @param target opponent
     */
    public Player(int playerSlot, GObject target) {
        this.playerSlot = playerSlot;
        this.target = target;
    }

    /**
     * Create necessary sprites.
     */
    @Override
    public void createEvent() {
        Image img = spriteCreate("images/player.png");
        this.img = img;

        playerDown = spriteCreate("images/playerDown.png", 4, 1, 4, 48, 70, 10);
        playerUp = spriteCreate("images/playerUp.png", 4, 1, 4, 48, 70, 10);
        playerLeft = spriteCreate("images/playerLeft.png", 4, 1, 4, 48, 70, 10);
        playerRight = spriteCreate("images/playerRight.png", 4, 1, 4, 48, 70, 10);
        spriteSet(playerDown, true);

        whole = spriteCreate("images/chrono.png", 4, 4, 16, 48, 72, 10);
        spriteSet(whole, true);
    }

    /**
     * Checks for input, keyboard presses and collisions.
     */
    @Override
    public void stepEvent() {
        
        // Player 1 movement
        if (playerSlot == 1) {
            if (Input.isKeyPressedHold("RIGHT")) {
                spriteSet(playerRight, false);
                setX(getX() + 1);
            }
            if (Input.isKeyPressedHold("LEFT")) {
                spriteSet(playerLeft, false);
                setX(getX() - 1);
            }
            if (Input.isKeyPressedHold("DOWN")) {
                spriteSet(playerDown, false);
                setY(getY() + 1);
            }
            if (Input.isKeyPressedHold("UP")) {
                spriteSet(playerUp, false);
                setY(getY() - 1);
            }

            if (Input.isKeyPressedHold("R")) {
                spriteSet(img);
            }
        }

        if (getCurrentAnimation() == whole) {
            if (spriteAnimationEnded()) {
                spriteSet(playerRight, false);
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
            spriteSpeed(1, true);

        if (Input.isKeyPressed("K"))
            spriteSpeed(-1, true);

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

    /**
     * Draws player and it's collision bounds.
     */
    @Override
    public void drawEvent() {
        drawBounds(1, Color.RED);
        drawSelf();
    }
}