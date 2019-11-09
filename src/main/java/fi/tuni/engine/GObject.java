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