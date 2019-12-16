package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.*;

public class Enemy extends GObject {

    private Game main;

    // Size
    private double size;
    private double maxSize = 2;
    private double minSize = 0.5;
    private double sizeSpd = 0.01;
    private boolean increase;

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
        size = randomRange(0.5, 1.5);
    }

    @Override
    public void stepEvent() {
        wrap(true, true, getWidth() / 2, getHeight() / 2);
        setDepth((int)-getY());

        resize();

        if(mousePressed()) {
            main.gameEnded();
        }
    }

    @Override
    public void drawEvent() {
        drawSelf();
    }

    private void resize() {
        spriteResize(size);

        if (increase) {
            if (size < maxSize)
                size += sizeSpd;
            else
                increase = false;
        } else {
            if (size > minSize)
                size -= sizeSpd;
            else
                increase = true;
        }
    }
}