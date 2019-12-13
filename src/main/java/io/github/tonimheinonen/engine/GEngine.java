package io.github.tonimheinonen.engine;

import io.github.tonimheinonen.engine.tools.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.Timeline;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * Main class of the software, holds main variables and launches the game.
 */
public abstract class GEngine extends Application implements Global {

    private int windowWidth = 1280;
    private int windowHeight = 720;
    private Group root;
    private Stage stage;
    private Image backgroundImage;
    private Canvas canvas;
    private GraphicsContext gc;
    private ArrayList<GObject> objects = new ArrayList<>();
    private ArrayList<GObject> objectsToAdd = new ArrayList<>();

    /**
     * Runs at the start of the program.
     * @param theStage main stage
     */
    @Override
    public void start(Stage theStage) throws Exception {
        try {
            System.out.println("Author: Toni Heinonen");
            this.stage = theStage;
            stage.setTitle("GEngine");
            stage.centerOnScreen();
            
            // Create main group
            Group root = new Group();
            this.root = root;

            // Set root on scene
            Scene theScene = new Scene(root);
            theStage.setScene(theScene);
            
            // Create canvas for drawing objects
            Canvas canvas = new Canvas(windowWidth, windowHeight);
            root.getChildren().add(canvas);
            this.canvas = canvas;
            
            // Get gc for drawing objects
            GraphicsContext gc = canvas.getGraphicsContext2D();
            this.gc = gc;
            Draw.setGraphicsContext(gc);

            // Set gameloop timeline
            Timeline gameLoop = new Timeline();
            gameLoop.setCycleCount(Timeline.INDEFINITE);

            // Handle input
            theScene.setOnKeyPressed(Input::handlePressed);
            theScene.setOnKeyReleased(Input::handleReleased);

            // Run main game create event before going to game loop
            createEvent();
            
            // Control game loop and it's speed
            KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),                // 60 FPS
                (e)->this.controlGameLoop());
            
            gameLoop.getKeyFrames().add( kf );
            gameLoop.play();

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    /**
     * Runs at the start of the program before other events.
     */
    public abstract void createEvent();

    /**
     * Runs every frame.
     */
    public abstract void stepEvent();

    /**
     * Runs every frame after stepEvent().
     */
    public abstract void drawEvent();

    /*************************
        SYSTEM
    **************************/
    /**
     * Controls game loop for main engine and it's objects.
     */
    private void controlGameLoop() {
        // Clear the canvas
        gc.clearRect(0, 0, windowWidth, windowHeight);

        // Draw background image
        drawBackgroundImage();
                    
        // Run main game object's step and draw events
        stepEvent();
        drawEvent(); 
        
        // Loop through object's events
        for (GObject o : objects)
            o.stepEvent();

        for (GObject o : objects)
            o.applyForces();

        for (GObject o : objects)
            o.drawEvent();

        destroyFlagged();
        addObjects();

        // Reset released input after each frame
        Input.resetInput();
    }

    /**
     * Closes program.
     */
    public void closeProgram() {
        stop();
    }

    /**
     * Stops javafx and all the other threads.
     */
    @Override
    public void stop() {
        // Close all threads
        System.exit(0);
    }

    /*************************
        WINDOW
    **************************/

    /**
     * Sets the title for window.
     * @param title new title to set
     */
    public void setWindowTitle(String title) {
        stage.setTitle(title);
    }

    /**
     * Sets the width and height for the window.
     * @param width width of the window
     * @param height height of the window
     */
    public void setWindowSize(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
        canvas.setWidth(width);
        canvas.setHeight(height);
    }

    /*************************
        BACKGROUND
    **************************/
    /**
     * Sets background for the window.
     * @param path location of the image file
     */
    public void setBackgroundImage(String path) {
        backgroundImage = new Image(path);
    }

    /**
     * Draws background image on screen.
     * 
     * Pretty useless method, but gamemaker has it too.
     */
    private void drawBackgroundImage() {
        if (backgroundImage != null)
            Draw.image(backgroundImage, 0, 0);
    }

    /*************************
        OBJECTS
     **************************/
    /**
     * Creates object from provided class type.
     * @param x horizontal position
     * @param y vertical position
     * @param type object to create
     * @param <T> type
     * @return instanced object
     */
    @SuppressWarnings("unchecked")
     public <T extends GObject> T createInstance(int x, int y, Class<T> type) {
        try {
            // Create new instance from provided class
            GObject obj = (GObject) type.getDeclaredConstructor().newInstance();
            obj = initInstance(x, y, obj);
            return (T) obj;     // Cast to provided class
        }
        catch (InstantiationException e) {} 
        catch (IllegalAccessException e) {}
        catch (NoSuchMethodException e) {
            System.out.println("You need to declare" +
            " default constructor for the class, or remove other" +
            " constructors!");
            throw new IllegalArgumentException("Default constructor missing!");
        }
        catch (InvocationTargetException e) {}

        return null;
    }

    /**
     * Creates provided instance.
     * @param <T> object which extends GObject
     * @param x coordinate for instance
     * @param y coordinate for instance
     * @param type instance to initialize
     * @return initialized instance
     */
    @SuppressWarnings("unchecked")
    public <T extends GObject> T createInstance(int x, int y, GObject type) {
        return (T) initInstance(x, y, type);
    }

    /**
     * Initializes newly created instance.
     * @param x coordinate for instance
     * @param y coordinate for instance
     * @param obj created instance
     * @return created instance
     */
    private GObject initInstance(int x, int y, GObject obj) {
        obj.setMainClass(this);
        obj.setX(x);
        obj.setY(y);
        objectsToAdd.add(obj);
        obj.setGraphicsContext(gc);
        obj.createEvent();
        return obj;
    }

    /**
     * Marks only one instance for destruction.
     * @param obj instance to destroy
     */
    public void destroyInstance(GObject obj) {
        obj.setDestroyThis(true);
    }

    /**
     * Marks all instances derived from type for destruction.
     * @param type class type to destroy
     */
    public void destroyInstance(Class<? extends GObject> type) {
        for (GObject o : objects) {
            if (o.getClass().getName().equals(type.getName())) {
                o.setDestroyThis(true);
            }
        }
    }

    /**
     * Destroys all instances which are marked for destruction.
     */
    private void destroyFlagged() {
        // Copy objects array
        ArrayList<GObject> temp = new ArrayList<>(objects);
        
        // Remove all marked for destroy
        for (GObject o : objects) {
            // Remove from temp to not disrupt this for loop
            if (o.isDestroyThis()) {
                temp.remove(o);
            } 
        }
        objects = temp;
    }

    private void addObjects() {
        for (GObject o : objectsToAdd) {
            objects.add(o);
        }

        objectsToAdd.clear();
    }

    /*************************
        BUTTONS
    **************************/
    /**
     * Creates button from provided class type.
     * @param text text that button has
     * @param x horizontal position
     * @param y vertical position
     * @param type object to create
     * @param <T> type
     * @return instanced object
     */
    @SuppressWarnings("unchecked")
     public <T extends GButton> T createButton(String text, int x, int y, Class<T> type) {
        try {
            // Create new instance from provided class
            GButton btn = (GButton) type.getDeclaredConstructor().newInstance();
            btn = initButton(text, x, y, btn);
            return (T) btn;     // Cast to provided class
        }
        catch (InstantiationException e) {} 
        catch (IllegalAccessException e) {}
        catch (NoSuchMethodException e) {
            System.out.println("You need to declare" +
            " default constructor for the class, or remove other" +
            " constructors!");
            throw new IllegalArgumentException("Default constructor missing!");
        }
        catch (InvocationTargetException e) {}

        return null;
    }

    /**
     * Creates provided instance.
     * @param text text that button has
     * @param <T> object which extends GObject
     * @param x coordinate for instance
     * @param y coordinate for instance
     * @param type instance to initialize
     * @return initialized instance
     */
    @SuppressWarnings("unchecked")
    public <T extends GButton> T createButton(String text, int x, int y, GButton type) {
        return (T) initButton(text, x, y, type);
    }

    /**
     * Initializes newly created instance.
     * @param text text that button has
     * @param x coordinate for instance
     * @param y coordinate for instance
     * @param btn created instance
     * @return created instance
     */
    private GButton initButton(String text, int x, int y, GButton btn) {
        btn.setMainClass(this);
        btn.setText(text);
        btn.setTranslateX(x);
        btn.setTranslateY(y);
        btn.createEvent();
        btn.setOnMousePressed(e->btn.onPressed());
        btn.setOnMouseReleased(e->btn.onReleased());
        btn.setOnMouseDragged(e->btn.onMouseDragged());
        btn.setOnMouseEntered(e->btn.onMouseEntered());
        btn.setOnMouseExited(e->btn.onMouseExited());

        // Without this, input stops working. No clue why.
        btn.setFocusTraversable(false);
        
        root.getChildren().add(btn);
        return btn;
    }

    /*************************
        GETTERS & SETTERS
    **************************/
    /**
     * Returns width of the window.
     * @return window width
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * Returns height of the window.
     * @return window height
     */
    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * Returns current stage.
     * @return current stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets current stage.
     * @param stage stage to change to
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns current canvas.
     * @return current canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Sets current canvas.
     * @param canvas canvas to set to
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Returns all the instances in game.
     * @return list of objects
     */
    public ArrayList<GObject> getObjects() {
        return objects;
    }
}