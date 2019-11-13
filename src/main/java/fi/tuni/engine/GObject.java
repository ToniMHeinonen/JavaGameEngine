package fi.tuni.engine;

import fi.tuni.engine.tools.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GObject {
    
    private double x, y, width, height;
    private GraphicsContext gc;
    private Image sprite;
    private GEngine mainClass;
    private BoundingBox bounds = new BoundingBox();

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
        updateBounds();
    }

    public void spriteResize(double width, double height) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    public void spriteDraw() {
        gc.drawImage(sprite, x, y, width, height);
    }

    /*************************
        COLLISIONS
    **************************/
    private void updateBounds() {
        bounds.setBox(x, y, width, height);
    }

    public void drawBounds(double alpha) {
        gc.setGlobalAlpha(alpha);
        gc.fillRect(x, y, width, height);
        gc.setGlobalAlpha(1);
    }

    public boolean collidesWith(GObject other) {
        return bounds.intersects(other.getBounds());
    }

    /*************************
        ENGINE METHODS
    **************************/
    public void createObject(int x, int y, GObject type) {
        global().createObject(x, y, type);
    }

    public boolean isKeyPressed(String key) {
        return global().isKeyPressed(key);
    }

    public boolean isKeyPressedHold(String key) {
        return global().isKeyPressedHold(key);
    }

    public boolean isKeyReleased(String key) {
        return global().isKeyReleased(key);
    }

    /*************************
        GETTERS & SETTERS
    **************************/
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public GEngine global() {
        return mainClass;
    }

    public void setMainClass(GEngine engine) {
        if (null == mainClass) {
            mainClass = engine;
        } else {
            System.out.println("mainClass can not be changed!");
        }
    }

    public BoundingBox getBounds() {
        return bounds;
    }
}