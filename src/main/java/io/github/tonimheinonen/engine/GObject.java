package io.github.tonimheinonen.engine;

import java.util.ArrayList;

import io.github.tonimheinonen.engine.tools.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Holds all the information every object will need in game.
 */
public abstract class GObject implements Global {
    
    private double x, y, width, height, origWidth, origHeight;
    private GraphicsContext gc;
    private Animator animator = new Animator();
    private Image self;
    private AnimatedImage curAnim;
    private float spriteSpd = -99;
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
     * @return created image
     */
    public Image spriteCreate(String imagePath) {
        self = new Image(imagePath);
        return self;
    }

    /**
     * Create animated image.
     * @param imagePath path under resources where image is located
     * @param columns how many columns image has
     * @param rows how many rows image has
     * @param totalFrames how many frames image has
     * @param frameWidth width of 1 frame
     * @param frameHeight height of 1 frame
     * @param frameSpeed how many frames to show per second
     * @return created animated image
     */
    public AnimatedImage spriteCreate(String imagePath, int columns, int rows,
        int totalFrames, int frameWidth, int frameHeight, int frameSpeed) {
        Image img = new Image(imagePath);
        AnimatedImage anim = new AnimatedImage(img, columns, rows, frameWidth,
             frameHeight, totalFrames, frameSpeed);

        return anim;
    }

    /**
     * Set object's sprite to normal image.
     * @param sprite image to set
     */
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

    /**
     * Sets object's sprite to an animated image.
     * @param sprite animated image to set
     * @param startFromBeginning start animation from beginning
     */
    public void spriteSet(AnimatedImage sprite, boolean startFromBeginning) {
        // If animation running and it's this one, skip code
        if (drawAnimation && curAnim == sprite)
            return;
        
        drawAnimation = true;
        curAnim = sprite;

        // If spriteSpd has not been altered, use given sprite's fps
        if (this.spriteSpd == -99)
            this.spriteSpd = sprite.getFps();

        width = sprite.getFrameWidth();
        height = sprite.getFrameHeight();
        setOriginalSize();
        updatePositionAndSize();

        animator.setAnimation(sprite);
        animator.setFps(this.spriteSpd);

        if (startFromBeginning)
            animator.startFromBeginning();
    }

    /**
     * Sets how many frames sprite shows in a second.
     * @param fps speed to set
     * @param addToCurrent whether to add to current speed
     */
    public void spriteSpeed(float fps, boolean addToCurrent) {
        if (addToCurrent)
            this.spriteSpd += fps;
        else
            this.spriteSpd = fps;

        this.animator.setFps(this.spriteSpd);
    }

    /**
     * Sets animation to provided index.
     * @param index to set, starts from 0
     */
    public void spriteIndex(int index) {
        this.animator.setCurrentFrame(index);
    }

    /**
     * Sets whether to loop animation or not.
     * @param loop whether to loop or not
     */
    public void spriteLoop(boolean loop) {
        this.animator.setLooping(loop);
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
     * Returns if animation has just shown last frame.
     * @return if animation has just ended
     */
    public boolean spriteAnimationEnded() {
        return animator.animationEnded();
    }

    /**
     * Draws the sprite in it's x and y coordinate.
     * 
     * Uses object's width and height for drawing.
     */
    public void drawSelf() {
        if (drawAnimation)
            animator.render(gc, x, y, width, height);
        else
            gc.drawImage(self, x, y, width, height);
    }

    /**
     * Sets original size for sprite.
     * 
     * Can be used after spriteResize() to set to current width and height.
     * Using this will make spriteResize(percent) 100% to be current size.
     */
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
    // Instances
    /**
     * Creates object from provided class type.
     * @param x horizontal position
     * @param y vertical position
     * @param type object to create
     * @return instanced object
     */
    public <T extends GObject> T createInstance(int x, int y, Class<T> type) {
        return global().createInstance(x, y, type);
    }

    /**
     * Creates provided instance.
     * @param <T> object which extends GObject
     * @param x coordinate for instance
     * @param y coordinate for instance
     * @param type instance to initialize
     * @return initialized instance
     */
    public <T extends GObject> T createInstance(int x, int y, GObject type) {
        return global().createInstance(x, y, type);
    }

    /**
     * Marks only one instance for destruction.
     * @param obj instance to destroy
     */
    public void destroyInstance(GObject obj) {
        global().destroyInstance(obj);
    }

    /**
     * Marks all instances derived from type for destruction.
     * @param type class type to destroy
     */
    public void destroyInstance(Class<? extends GObject> type) {
        global().destroyInstance(type);
    }

    // Drawing
    /**
     * Draws image to provided coordinate and size.
     * @param img image to draw
     * @param x coordinate
     * @param y coordinate
     * @param width desired width
     * @param height desired height
     */
    public void drawImage(Image img, double x, double y,
    double width, double height) {
        global().drawImage(img, x, y, width, height);
    }

    /**
     * Draws image to provided coordinate.
     * @param img image to draw
     * @param x coordinate
     * @param y coordinate
     */
    public void drawImage(Image img, double x, double y) {
        global().drawImage(img, x, y);
    }

    /**
     * Draws animated image to provided coordinate and size.
     * @param img image to draw
     * @param x coordinate
     * @param y coordinate
     * @param width desired width
     * @param height desired height
     */
    public void drawAnimatedImage(AnimatedImage img, double x, double y,
                 double width, double height) {
        global().drawAnimatedImage(img, x, y, width, height);
    }

    /**
     * Draws image to provided coordinate.
     * @param img image to draw
     * @param x coordinate
     * @param y coordinate
     */
    public void drawAnimatedImage(AnimatedImage img, double x, double y) {
        global().drawAnimatedImage(img, x, y);
    }

    /*************************
        GETTERS & SETTERS
    **************************/
    /**
     * Sets position of object.
     * @param x coordinate
     * @param y coordinate
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        updatePositionAndSize();
    }

    /**
     * Returns x coordinate.
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets x position of object.
     * @param x coordinate
     */
    public void setX(double x) {
        this.x = x;
        updatePositionAndSize();
    }

    /**
     * Returns y coordinate.
     * @return y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets y position of object.
     * @param y coordinate
     */
    public void setY(double y) {
        this.y = y;
        updatePositionAndSize();
    }

    /**
     * Returns canvas's graphicscontext where object draws on.
     * @return graphics context
     */
    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    /**
     * Sets canvas's graphicscontext where object draws on.
     * @param gc graphics context
     */
    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Returns reference to main class, which extends GEngine.
     * @return main class
     */
    public GEngine global() {
        return mainClass;
    }

    /**
     * Sets main class when object is created.
     * 
     * Main class can not be changed.
     * @param engine main class
     */
    public void setMainClass(GEngine engine) {
        if (null == mainClass) {
            mainClass = engine;
        } else {
            System.out.println("mainClass can not be changed!");
        }
    }

    /**
     * Returns object's collision bounds.
     * @return bounding box of collision bounds.
     */
    public BoundingBox getBounds() {
        return bounds;
    }

    /**
     * Returns if object is marked for destruction.
     * @return if destroyThis is true
     */
    public boolean isDestroyThis() {
        return destroyThis;
    }

    /**
     * Marks object for destruction.
     * 
     * Main class uses this, if you want to destroy object, call
     * instanceDestroy() to destroy it properly.
     * @param destroyThis marks if this instance should be destroyed
     */
    public void setDestroyThis(boolean destroyThis) {
        this.destroyThis = destroyThis;
    }

    /**
     * Returns list of instances which collision happened with.
     * @return list of instances 
     */
    public ArrayList<GObject> getCollidedObjects() {
        return collidedObjects;
    }

    /**
     * Returns other instance which collision happened with.
     * 
     * Use this if there is specific instance which you checked
     * collision with.
     * @return reference to other instance
     */
    public GObject getOther() {
        return other;
    }

    /**
     * Returns width of the object.
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns height of the object.
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns current sprite's animation speed.
     * @return current sprite's animation speed
     */
    public float getSpriteSpd() {
        return spriteSpd;
    }

    /**
     * Returns current animation which is running.
     * @return current animation
     */
    public AnimatedImage getCurrentAnimation() {
        return curAnim;
    }
}