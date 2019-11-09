package fi.tuni.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GObject {
    
    private int x, y;
    private double width, height;
    private GraphicsContext gc;
    private Image sprite;

    public abstract void createEvent();

    public abstract void stepEvent();

    public abstract void drawEvent();

    /*************************
        SPRITE
    **************************/
    public void spriteCreate(String path) {
        String p = "fi/tuni/game/" + path;
        sprite = new Image(p);
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void spriteResize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void spriteDraw() {
        gc.drawImage(sprite, x, y, width, height);
    }


    /*************************
        GETTERS & SETTERS
    **************************/
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }
}