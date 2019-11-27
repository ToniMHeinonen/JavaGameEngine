package fi.tuni.engine;

import java.util.ArrayList;

import fi.tuni.engine.tools.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GObject {
    
    private double x, y, width, height, origWidth, origHeight;
    private GraphicsContext gc;
    private ImageView view = new ImageView();
    private Animator animator = new Animator(view);
    private Image self;
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

    public Animation spriteCreate(String imagePath, int columns, int rows,
        int totalFrames, int frameWidth, int frameHeight, float framesPerSecond) {
        Image img = new Image(imagePath);
        Animation anim = new Animation(img, columns, rows,
        totalFrames, frameWidth, frameHeight, framesPerSecond);

        return anim;
    }

    public void spriteSet(Image sprite) {
        self = sprite;
        view.setImage(self);
    }

    public void spriteSet(Animation sprite) {
        width = sprite.getFrameWidth();
        height = sprite.getFrameHeight();
        setOriginalSize();
        updatePositionAndSize();

        playAnimation(sprite);
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
        gc.drawImage(self, x, y, width, height);
    }

    private void setOriginalSize() {
        origWidth = width;
        origHeight = height;
    }

    /*************************
        ANIMATIONS
    **************************/
    public void playAnimation(Animation sprite) {
        animator.playAnimation(sprite);
    }

    public void stopAnimation() {
        animator.stop();
    }

    /*************************
        COLLISIONS
    **************************/
    /**
     * Move bounds to new location.
     */
    private void updatePositionAndSize() {
        bounds.setBox(x, y, width, height);
        view.setX(x);
        view.setY(y);
        view.setFitWidth(width);
        view.setFitHeight(height);
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

    public ImageView getView() {
        return view;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}