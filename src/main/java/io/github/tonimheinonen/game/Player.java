package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;
import javafx.scene.image.Image;

public class Player extends GObject {

    private int playerSlot = 1;
    private Image img;
    private AnimatedImage whole, playerDown, playerUp, playerLeft, playerRight;
    private String down, up, right, left;
    private GObject target;
    private double maxSpd = 5;
    private double friction = 0.1;

    /**
     * Default constructor for instancing.
     */
    public Player() {}

    /**
     * Gets playerslot and target to look out for.
     * @param playerSlot player number
     * @param target opponent
     */
    public Player(int playerSlot) {
        this.playerSlot = playerSlot;
    }

    /**
     * Instantiate necessary variables.
     */
    @Override
    public void createEvent() {
        playerDown = spriteCreate("images/playerDown.png", 4, 1, 4, 48, 70, 10);
        playerUp = spriteCreate("images/playerUp.png", 4, 1, 4, 48, 70, 10);
        playerLeft = spriteCreate("images/playerLeft.png", 4, 1, 4, 48, 70, 10);
        playerRight = spriteCreate("images/playerRight.png", 4, 1, 4, 48, 70, 10);
        spriteSet(playerDown, true);

        if (playerSlot == 1) {
            down = "down";
            up = "up";
            right = "right";
            left = "left";
        } else {
            down = "S";
            up = "W";
            left = "A";
            right = "D";
        }
    }

    /**
     * Checks for input, keyboard presses and collisions.
     */
    @Override
    public void stepEvent() {
        movePlayer();
    }

    /**
     * Draws player and it's collision bounds.
     */
    @Override
    public void drawEvent() {
        drawBounds(1, C_RED);
        drawSelf();
    }

    private void movePlayer() {
        // Movement and animation
        double x = 0;
        double y = 0;

        if (Input.isKeyPressedHold(right)) {
            spriteSet(playerRight, false);
            x++;
        }
        if (Input.isKeyPressedHold(left)) {
            spriteSet(playerLeft, false);
            x--;
        }
        if (Input.isKeyPressedHold(down)) {
            spriteSet(playerDown, false);
            y++;
        }
        if (Input.isKeyPressedHold(up)) {
            spriteSet(playerUp, false);
            y--;
        }

        // If player is moving
        if (x != 0 || y != 0) {

        }
    }
}