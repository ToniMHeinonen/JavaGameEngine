package fi.tuni.engine;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GObject {
    
    private int x, y;
    private double width, height;
    private GraphicsContext gc;
    private Image sprite;
    private GEngine mainClass;

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
}