package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class Player extends GObject {

    private int playerSlot = 1;
    private AnimatedImage playerDown, playerUp, playerLeft, playerRight;
    private String down, up, right, left;
    private double speed = 0.4;
    private double maxSpdNormal = 7;
    private double maxSpdOnHit = 10;
    private double maxSpdCurrent = maxSpdNormal;
    private int coinsCollected;

    private final int NORMAL = 0, IMMUNE = 1, STUNNED = 2;
    private int state = NORMAL;
    private long timeOfHit, timeOfHitEnded;
    private double stunTime = 2;
    private double immuneTime = 2;

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

        setFriction(0.2);
    }

    /**
     * Checks for input, keyboard presses and collisions.
     */
    @Override
    public void stepEvent() {
        checkStunned();
        checkImmune();

        if (state == NORMAL)
            movePlayer();

        collisions();

        wrap(true, true, getWidth(), getHeight());
    }

    /**
     * Draws player and it's collision bounds.
     */
    @Override
    public void drawEvent() {
        drawSelf();
        Draw.text(".", getX(), getY());
    }

    private void checkStunned() {
        if (state != STUNNED)
            return;

        if (secondsPassed(timeOfHit, stunTime)) {
            state = IMMUNE;
            maxSpdCurrent = maxSpdNormal;
            timeOfHitEnded = System.currentTimeMillis();
        }

        spriteIndex(0);
    }

    public void checkImmune() {
        if (state != IMMUNE)
            return;

        if (secondsPassed(timeOfHitEnded, immuneTime)) {
            state = NORMAL;
        }
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

            if (getSpeed() > maxSpdCurrent)
                setSpeed(maxSpdCurrent);

            setDirection(calculateDirection(x, y));
        }

        // Control animations when stopping
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
            if (other.getState() == NORMAL) {
                if (other.getSpeed() == getSpeed()) {
                    double dir = other.getDirection();
                    other.onHit();
                    onHit();
                    other.setDirection(getDirection());
                    setDirection(dir);
                    other.setSpeed(getSpeed() * 2);
                    setSpeed(getSpeed() * 2);
                } else if (other.getSpeed() < getSpeed()) {
                    other.onHit();
                    other.setDirection(getDirection());
                    other.setSpeed(getSpeed() * 2);
                    other.applyForces();
                    
                    if (other.stealCoin()) {
                        coinsCollected++;
                    }
                }
            }
        }

        if (collidesWith(Coin.class)) {
            coinsCollected++;
        }
    }

    public int getState() {
        return state;
    }

    public boolean stealCoin() {
        if (coinsCollected > 0) {
            coinsCollected--;
            return true;
        } else {
            return false;
        }
    }

    public void onHit() {
        state = STUNNED;
        maxSpdCurrent = maxSpdOnHit;
        timeOfHit = System.currentTimeMillis();
    }
}