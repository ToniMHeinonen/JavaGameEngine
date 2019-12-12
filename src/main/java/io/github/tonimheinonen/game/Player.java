package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class Player extends GObject {

    private int playerSlot = 1;
    private AnimatedImage playerDown, playerUp, playerLeft, playerRight;
    private String down, up, right, left;
    private double speed = 0.5;
    private boolean immune;
    private int coinsCollected;

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

        setMaxSpeed(5);
        setFriction(0.2);
    }

    /**
     * Checks for input, keyboard presses and collisions.
     */
    @Override
    public void stepEvent() {
        immune = false;
        
        movePlayer();
        collisions();
    }

    /**
     * Draws player and it's collision bounds.
     */
    @Override
    public void drawEvent() {
        drawSelf();
    }

    private void movePlayer() {
        // Movement and animation
        int x = 0;
        int y = 0;

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
            setSpeed(getSpeed() + speed);
            setDirection(calculateDirection(x, y));
        }

        if (getSpeed() == 0) {
            
            if (spriteAnimationEnded()) {
                spriteSpeed(0, false);
                spriteIndex(0);
            }
        } else {
            spriteSpeed(10, false);
        }
    }

    private double calculateDirection(int x, int y) {
        // I am bad at mathematics, so I can't figure out a better way to do this
        if (x == 1) {
            if (y == 1)
                return 45;
            else if (y == -1)
                return 315;
            else
                return 0;
        } else if (x == -1) {
            if (y == 1)
                return 135;
            else if (y == -1)
                return 225;
            else
                return 180;
        } else {
            if (y == 1)
                return 90;
            else if (y == -1)
                return 270;
        }

        return -1;
    }

    private void collisions() {
        if (collidesWith(Player.class)) {
            Player other = (Player) getCollidedObjects().get(0);
            if (other.getSpeed() < getSpeed()) {
                if (!other.isImmune()) {
                    other.setDirection(getDirection());
                    other.setSpeed(getSpeed() * 2);

                    if (other.stealCoin()) {
                        coinsCollected++;
                    }

                    immune = true;
                }
            }
        }

        if (collidesWith(Coin.class)) {
            coinsCollected++;
        }
    }

    public boolean isImmune() {
        return immune;
    }

    public boolean stealCoin() {
        if (coinsCollected > 0) {
            coinsCollected--;
            return true;
        } else {
            return false;
        }
    }
}