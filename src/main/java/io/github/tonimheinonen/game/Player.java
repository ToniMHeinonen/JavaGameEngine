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
    private long startOfHit, startOfImmune;
    private double stunTime = 2;
    private double immuneTime = 1;
    private double stunSpeed, stunDirection;
    private boolean applyStun;

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

        movePlayer();

        collisions();

        wrap(true, true, getWidth(), getHeight());

        hitHappened();
    }

    /**
     * Draws player and it's collision bounds.
     */
    @Override
    public void drawEvent() {
        if (state == IMMUNE)
            drawBounds(0.5, C_WHITE);
        else if (state == STUNNED)
            drawBounds(0.5, C_BLUE);
        
        drawSelf();
    }

    private void checkStunned() {
        if (state != STUNNED)
            return;

        if (secondsPassed(startOfHit, stunTime))
            startImmunity();

        spriteIndex(0);
    }

    private void checkImmune() {
        if (state != IMMUNE)
            return;

        if (secondsPassed(startOfImmune, immuneTime))
            state = NORMAL;
    }

    private void startImmunity() {
        state = IMMUNE;
        maxSpdCurrent = maxSpdNormal;
        startOfImmune = System.currentTimeMillis();
    }

    private void hitHappened() {
        if (applyStun) {
            applyStun = false;
            setSpeed(stunSpeed);
            setDirection(stunDirection);
        }
    }

    private void movePlayer() {
        if (state == STUNNED)
            return;
        
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
                    other.onHit(getSpeed(), getDirection());
                    onHit(getSpeed(), other.getDirection());
                } else if (other.getSpeed() < getSpeed()) {
                    startImmunity();
                    other.onHit(getSpeed(), getDirection());
                    
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

    public void onHit(double speed, double direction) {
        state = STUNNED;
        maxSpdCurrent = maxSpdOnHit;
        startOfHit = System.currentTimeMillis();

        applyStun = true;
        stunSpeed = speed * 2;
        stunDirection = direction;
    }
}