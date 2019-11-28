package fi.tuni.engine;

import java.util.ArrayList;

import fi.tuni.engine.tools.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class GObject implements Global {
    
    private double x, y, width, height, origWidth, origHeight;
    private GraphicsContext gc;
    private AnimatedImage currentAnimation;
    private Image self;
    private float spriteSpeed = -99;
    private boolean drawAnimation;
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
     * @param imagePath image to create
     */
    public Image spriteCreate(String imagePath) {
        self = new Image(imagePath);
        return self;
    }

    public AnimatedImage spriteCreate(String imagePath, int columns, int rows,
        int totalFrames, int frameWidth, int frameHeight, int frameSpeed) {
        Image img = new Image(imagePath);
        AnimatedImage anim = new AnimatedImage(img, columns, rows, frameWidth,
             frameHeight, totalFrames, frameSpeed);

        return anim;
    }

    public void spriteSet(Image sprite) {
        // If using normal sprite already and it's this one, skip code
        if (!drawAnimation && self == sprite)
            return;

        drawAnimation = false;
        width = sprite.getWidth();
        height = sprite.getHeight();
        setOriginalSize();
        updatePositionAndSize();

        self = sprite;
    }

    public void spriteSet(AnimatedImage sprite) {
        // If animation running and it's this one, skip code
        if (drawAnimation && this.currentAnimation == sprite)
            return;
        
        drawAnimation = true;

        if (this.spriteSpeed == -99)
            this.spriteSpeed = sprite.getFps();
        else
            sprite.setFps(this.spriteSpeed);

        width = sprite.getFrameWidth();
        height = sprite.getFrameHeight();
        setOriginalSize();
        updatePositionAndSize();

        this.currentAnimation = sprite;
        this.currentAnimation.startOver();
    }

    public void spriteSpeed(float fps) {
        this.spriteSpeed = fps;
        this.currentAnimation.setFps(fps);
    }

    /**
     * Resizes the sprite in use.
     * @param width to change
     * @param height to change
     */
    public void spriteResize(double width, double height) {
        this.width = width;
        this.height = height;
        updatePositionAndSize();
    }

    /**
     * Resizes the sprite in use.
     * @param percent 1 = 100%
     */
    public void spriteResize(double percent) {
        this.width = origWidth * percent;
        this.height = origHeight * percent;
        updatePositionAndSize();
    }

    /**
     * Draws the sprite in it's x and y coordinate.
     */
    public void drawSelf() {
        if (drawAnimation)
            currentAnimation.render(gc, x, y, width, height);
        else
            gc.drawImage(self, x, y, width, height);
    }

    private void setOriginalSize() {
        origWidth = width;
        origHeight = height;
    }

    /*************************
        COLLISIONS
    **************************/
    /**
     * Move bounds to new location.
     */
    private void updatePositionAndSize() {
        bounds.setBox(x, y, width, height);
    }

    /**
     * Draws collision bounds on screen.
     * @param alpha to use for drawing
     * @param color to use for drawing
     */
    public void drawBounds(double alpha, Color color) {
        gc.setGlobalAlpha(alpha);
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
        // Reset values
        gc.setGlobalAlpha(1);
        gc.setFill(cBlack);
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
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        updatePositionAndSize();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        updatePositionAndSize();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        updatePositionAndSize();
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

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public AnimatedImage getCurrentAnimation() {
        return currentAnimation;
    }
}