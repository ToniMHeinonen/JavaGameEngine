package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.*;

public class Enemy extends GObject {

    private Game main;

    public Enemy(Game main) {
        this.main = main;
    }

    @Override
    public void createEvent() {
        String[] sprites = new String[] {"red.png", "green.png", "blue.png"};

        String sprite = sprites[randomRange(0, sprites.length - 1)];
        spriteSet(spriteCreate("images/" + sprite, 3, 1, 3, 64, 64, 5), true);
        setX(randomRange(0, global().getWindowWidth()));
        setY(randomRange(0, global().getWindowHeight()));
        setSpeed(2);
        setDirection(randomRange(0, 359));
    }

    @Override
    public void stepEvent() {
        wrap(true, true, getWidth() / 2, getHeight() / 2);
        setDepth((int)-getY());

        if(mousePressed()) {
            main.enemyClicked();
        }
    }

    @Override
    public void drawEvent() {
        drawSelf();
    }
}