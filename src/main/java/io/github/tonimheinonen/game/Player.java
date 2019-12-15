package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class Player extends GObject {

    private int playerSlot = 1;
    private AnimatedImage playerDown, playerUp, playerLeft, playerRight;
    private double coinsX;
    private double coinsY = 50;
    private String down, up, right, left;
    private double speed = 0.2;
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

    private boolean movementStarted;

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
        spriteSpeed(0, false);
        setBounds(9, getHeight()/2, 30, getHeight()/2);

        if (playerSlot == 1) {
            down = "S";
            up = "W";
            right = "D";
            left = "A";
            coinsX = global().getWindowWidth() / 4;
        } else {
            down = "Down";
            up = "Up";
            left = "Left";
            right = "Right";
            coinsX = (global().getWindowWidth() / 4) * 3;
        }

        setFriction(0.15);
    }

    /**
     * Checks for input, keyboard presses and collisions.
     */
    @Override
    public void stepEvent() {
        setDepth((int)-getY()); // Sets depth using y value
        
        checkStunned();
        checkImmune();

        movePlayer();

        collisions();

        wrap(true, true, getWidth() / 2 - 2, getHeight() / 2 - 2);

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
        
        drawPlayer();
        drawHud();
    }

    /**
     * Draws player with or without hue.
     */
    private void drawPlayer() {
        if (playerSlot == 2)
            Draw.setHue(-10);
        drawSelf();
        Draw.setHue(0);
    }

    /**
     * Draws hud elements on screen.
     */
    private void drawHud() {
        Draw.setHorizontalAlign(HA_CENTER);
        Draw.setVerticalAlign(VA_CENTER);
        Draw.setTextFont("Verdana");
        Draw.setColor(C_WHITE);

        drawControls();

        // Draws player's score on top of the screen
        Draw.setTextSize(35);
        Draw.text(String.valueOf(coinsCollected), coinsX, coinsY);
    }

    /**
     * Draws controls when movement has not yet started.
     */
    private void drawControls() {
        if (!movementStarted) {
            Draw.setTextSize(25);
            int offset = 70;
            Draw.text(up, getX(), getY() - offset);
            Draw.text(down, getX(), getY() + offset);
            Draw.text(right, getX() + offset, getY());
            Draw.text(left, getX() - offset, getY());
        }
    }

    /**
     * Controls what happens when player is stunned.
     */
    private void checkStunned() {
        if (state != STUNNED)
            return;

        if (secondsPassed(startOfHit, stunTime))
            startImmunity();

        spriteIndex(0);
    }

    /**
     * Controls what happens when player is immune.
     */
    private void checkImmune() {
        if (state != IMMUNE)
            return;

        if (secondsPassed(startOfImmune, immuneTime))
            state = NORMAL;
    }

    /**
     * Starts immunity.
     */
    private void startImmunity() {
        state = IMMUNE;
        maxSpdCurrent = maxSpdNormal;
        startOfImmune = System.currentTimeMillis();
    }

    /**
     * Checks if hit happened this frame.
     */
    private void hitHappened() {
        if (applyStun) {
            applyStun = false;
            setSpeed(stunSpeed);
            setDirection(stunDirection);
        }
    }

    /**
     * Moves player and controls it's animation.
     */
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
            if (!movementStarted)
                movementStarted = true;

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

    /**
     * Calculates the angle in degrees where to move the player.
     * @param x 1 to -1 to decide which direction to move
     * @param y 1 to -1 to decide which direction to move
     */
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

    /**
     * Checks collisions with other players and coins.
     */
    private void collisions() {
        if (collidesWith(Player.class)) {
            Player other = (Player) getCollidedObjects().get(0);
        
            if (other.getState() == NORMAL) {
                // Stupid double value changes to 0.000000000000000000018
                double diff = getSpeed() - other.getSpeed() - 0.001;

                if (diff <= getFriction() && diff >= 0) {
                    // If speed is tied counting the friction
                    other.onHit(getSpeed(), getDirection());
                    onHit(getSpeed(), other.getDirection());
                } else if (diff > getFriction()) {
                    // Else if speed is faster than opponent's
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

    /**
     * Checks if coins are left, steal if there is.
     * @return if theft succeeded
     */
    public boolean stealCoin() {
        if (coinsCollected > 0) {
            coinsCollected--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Controls what happens when player is hit.
     * @param speed opponent's speed
     * @param direction opponent's direction
     */
    public void onHit(double speed, double direction) {
        Audio.playSound("sounds/ouch.wav", false);
        state = STUNNED;
        maxSpdCurrent = maxSpdOnHit;
        startOfHit = System.currentTimeMillis();

        applyStun = true;
        stunSpeed = speed * 2;
        stunDirection = direction;
    }

    /**
     * Returns current state.
     * @return current state.
     */
    public int getState() {
        return state;
    }

    /**
     * Returns the amount of coins collected.
     * @return the amount of coins collected
     */
    public int getCoinsCollected() {
        return coinsCollected;
    }
}