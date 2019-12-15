package io.github.tonimheinonen.engine;

import java.util.ArrayList;

import io.github.tonimheinonen.engine.tools.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
    private double originX = -99;
    private double originY = -99;
    private boolean drawAnimation;
    private GEngine mainClass;
    private BoundingBox bounds = new BoundingBox();
    private ArrayList<GObject> collidedObjects = new ArrayList<>();
    private GObject other;
    private boolean destroyThis;
    private double direction, speed, maxSpeed, friction;
    private double gravity, gravityCurForce;
    private boolean maxSpeedInitialized;
    private int depth;
    
    /** 
     * Runs when the instance is created.
     */
    public abstract void createEvent();
    
    /** 
     * Runs every frame.
     */
    public abstract void stepEvent();
    
    /** 
     * Runs every frame, use for drawing on screen.
     */
    public abstract void drawEvent();

    /*************************
        CONTROL OBJECT
    **************************/

    /**
     * Applies forces for object to control speed and direction.
     */
    public void applyForces() {
        // Apply friction
        if (speed > 0)
            speed -= friction;

        // Check speed
        if (speed < 0)
            speed = 0;
        else if (speed > maxSpeed && maxSpeedInitialized)
            speed = maxSpeed;

        // Apply gravity ADD LATER
        /*if (gravity != 0) {
            if (speed != 0) {
                
                moveObject(gravityCurForce, 90);
            } else {
                gravityCurForce = 0;
            }
        }*/
        
        moveObject(speed, direction);
    }

    /**
     * Moves object to the given direction using given speed.
     * @param speed in pixels
     * @param direction in degrees
     */
    private void moveObject(double speed, double direction) {
        setX(x + speed * Math.cos( Math.toRadians(direction)));
        setY(y + speed * Math.sin( Math.toRadians(direction)));
    }

    /**
     * Wraps player from one side of the screen to another.
     * @param horizontal whether to wrap horizontally
     * @param vertical whether to wrap vertically
     * @param xOffset how far off the screen before wrapping
     * @param yOffset how far off the screen before wrapping
     */
    public void wrap(boolean horizontal, boolean vertical,
                    double xOffset, double yOffset) {
        int width = global().getWindowWidth();
        int height = global().getWindowHeight();
        
        if (horizontal) {
            if (x < 0 - xOffset)
                setX(width + (xOffset-1));
            else if (x > width + xOffset)
                setX(-xOffset+1);
        }

        if (vertical) {
            if (y < 0 - yOffset)
                setY(height + (yOffset-1));
            else if (y > height + yOffset)
                setY(-yOffset+1);
        }
    }

    /*************************
        SPRITE
    **************************/
    /**
     * Creates new sprite.
     * @param imagePath image to create
     * @return created image
     */
    public Image spriteCreate(String imagePath) {
        return FileManager.getImage(imagePath);
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
        Image img = FileManager.getImage(imagePath);
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
        if (width == 0) {
            width = sprite.getWidth();
            height = sprite.getHeight();
        }
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

        if (width == 0) {
            width = sprite.getFrameWidth();
            height = sprite.getFrameHeight();
        }
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
            animator.render(gc, x - originX, y - originY, width, height);
        else
            gc.drawImage(self, x - originX, y - originY, width, height);
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

    /**
     * Centers the origin of the object.
     */
    private void centerOrigin() {
        if (width > 0) {
            originX = width / 2;
            originY = height / 2;
        }
    }

    /*************************
        COLLISIONS
    **************************/
    /**
     * Move bounds to new location.
     */
    private void updatePositionAndSize() {
        centerOrigin();
        bounds.setBox(x - originX, y - originY, width, height);
    }

    public void setBounds(double xOffset, double yOffset,
                double width, double height) {
        
    }

    /**
     * Draws collision bounds on screen.
     * @param alpha to use for drawing
     * @param color to use for drawing
     */
    public void drawBounds(double alpha, Color color) {
        Paint curColor = gc.getFill();
        double curAlpha = gc.getGlobalAlpha();
        gc.setGlobalAlpha(alpha);
        gc.setFill(color);
        gc.fillRect(x - originX, y - originY, width, height);
        // Reset values
        gc.setGlobalAlpha(curAlpha);
        gc.setFill(curColor);
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
        MOUSE
    **************************/
    /** 
     * Returns if mouse is over current instance.
     * @return true if mouse is over current instance
     */
    public boolean mouseOver() {
        return bounds.contains(Input.getMousePosX(), Input.getMousePosY());
    }
    
    /** 
     * Returns if mouse was pressed over current instance.
     * @return true if mouse was pressed over current instance
     */
    public boolean mousePressed() {
        return bounds.contains(Input.getMousePressedX(), Input.getMousePressedY());
    }
    
    /** 
     * Returns if mouse was released over current instance.
     * @return true if mouse was released over current instance
     */
    public boolean mouseReleased() {
        return bounds.contains(Input.getMouseReleasedX(), Input.getMouseReleasedY());
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

    /**
     * Returns current direction.
     */
    public double getDirection() {
        return direction;
    }

    /**
     * Sets direction for the object to move at.
     * 
     * Direction value should be between 0 - 359.
     * 0 = Right / 90 = Down / 180 = Left / 270 = Up.
     * @param direction in degrees
     */
    public void setDirection(double direction) {
        this.direction = direction;
    }

    /**
     * Returns current speed.
     * @return
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the object to move
     * @param speed in pixels
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Returns maximun speed allowed.
     * 
     * If max speed has not been set, it won't be used.
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets maximum speed for the object.
     * @param maxSpeed in pixels
     */
    public void setMaxSpeed(double maxSpeed) {
        maxSpeedInitialized = true;
        this.maxSpeed = maxSpeed;
    }

    /**
     * Gets the friction of the object.
     */
    public double getFriction() {
        return friction;
    }

    /**
     * Sets the friction for the object to decrease speed.
     * @param friction in pixels
     */
    public void setFriction(double friction) {
        this.friction = friction;
    }

    /**
     * Returns drawing depth of the object.
     * @return
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Sets drawing depth of the object.
     * 
     * Lower number draws on top of higher number.
     * @param depth wanted depth (-2 draws on top of -1)
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }
}