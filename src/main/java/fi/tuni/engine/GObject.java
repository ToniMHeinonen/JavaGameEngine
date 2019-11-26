package fi.tuni.engine;

import java.util.ArrayList;

import fi.tuni.engine.tools.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GObject {
    
    private double x, y, width, height;
    private GraphicsContext gc;
    private Image sprite;
    private GEngine mainClass;
    private BoundingBox bounds = new BoundingBox();
    private ArrayList<GObject> collidedObjects = new ArrayList<>();
    private GObject other;
    private boolean destroyThis;

    public abstract void createEvent();

    public abstract void stepEvent();

    public abstract void drawEvent();

    /*************************
        SPRITE
    **************************/
    /**
     * Creates new sprite.
     * @param path image to create
     */
    public void spriteCreate(String path) {
        sprite = new Image(path);
        width = sprite.getWidth();
        height = sprite.getHeight();
        updateBounds();
    }

    public void spriteCreate() {
        
    }

    /**
     * Resizes the sprite in use.
     * @param width
     * @param height
     */
    public void spriteResize(double width, double height) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    /**
     * Draws the sprite in it's x and y coordinate.
     */
    public void spriteDraw() {
        gc.drawImage(sprite, x, y, width, height);
    }

    /*************************
        COLLISIONS
    **************************/
    /**
     * Move bounds to new location.
     */
    private void updateBounds() {
        bounds.setBox(x, y, width, height);
    }

    /**
     * Draws collision bounds on screen.
     * @param alpha to use for drawing
     */
    public void drawBounds(double alpha) {
        gc.setGlobalAlpha(alpha);
        gc.fillRect(x, y, width, height);
        gc.setGlobalAlpha(1);
    }

    /**
     * Checks if object collides with any provided class instance.
     * @param other class to check for collisions
     * @return if collision happens
     */
    public boolean collidesWith(Class<? extends GObject> other) {
        // If current object is the only instance, don't check
        if (global().getObjects().size() <= 1)
            return false;
        
        boolean collision = false;

        collidedObjects.clear();    // Empty previous collisions
        
        // Loop through all the objects in game
        for (GObject o : global().getObjects()) {
            // Ignore collision with self
            if (o.equals(this))
                continue;

            // If collision does not happen, skip iteration
            if (!bounds.intersects(o.getBounds()))
                continue;

            // Check if objects class equals with parameter class
            if (o.getClass().getName().equals(other.getName())) {
                collidedObjects.add(o);
                collision = true;
            }
        }
        
        return collision;
    }

    /**
     * Checks collision with provided instance.
     * @param other instance to check
     * @return if collision happens
     */
    public boolean collidesWith(GObject other) {
        if (other == null)
            return false;
        
        if (bounds.intersects(other.getBounds())) {
            this.other = other;
            return true;
        }

        return false;
    }

    /**
     * Resets references in case they got destroyed.
     */
    public void resetReferences() {
        other = null;               // Clear collided object
        collidedObjects.clear();    // Clear collided objects
    }

    /*************************
        ENGINE METHODS
    **************************/
    public <T extends GObject> T createInstance(int x, int y, Class<T> type) {
        return global().createInstance(x, y, type);
    }

    public <T extends GObject> T createInstance(int x, int y, GObject type) {
        return global().createInstance(x, y, type);
    }

    public void destroyInstance(GObject obj) {
        global().destroyInstance(obj);
    }

    public <T extends GObject> void destroyInstance(Class<T> type) {
        global().destroyInstance(type);
    }

    /*************************
        GETTERS & SETTERS
    **************************/
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        updateBounds();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        updateBounds();
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

    public boolean isDestroyThis() {
        return destroyThis;
    }

    public void setDestroyThis(boolean destroyThis) {
        this.destroyThis = destroyThis;
    }

    public ArrayList<GObject> getCollidedObjects() {
        return collidedObjects;
    }

    public GObject getOther() {
        return other;
    }
}