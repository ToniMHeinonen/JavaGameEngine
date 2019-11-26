package fi.tuni.engine;

import fi.tuni.engine.tools.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.util.Duration;

public abstract class GEngine extends Application {

    private int width = 1280;
    private int height = 720;
    private Stage stage;
    private Canvas canvas;
    private GraphicsContext gc;
    private ArrayList<GObject> objects = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) throws Exception {
        System.out.println("Author: Toni Heinonen");
        this.stage = theStage;
        stage.setTitle("GEngine");
        stage.centerOnScreen();

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        this.canvas = canvas;
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.gc = gc;

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        // Object animations are shown on this
        ImageView imageView = new ImageView();
        root.getChildren().add(imageView);

        /*ImageViewSprite anim = new ImageViewSprite(imageView, new Image("images/playerAnim.png"), 4, 1, 4, 320, 320, 60);
        anim.start();*/

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
    }

    public abstract void createEvent();

    public abstract void stepEvent();

    public abstract void drawEvent();

    /*************************
        SYSTEM
    **************************/
    /**
     * Controls game loop for main engine and it's objects.
     */
    private void controlGameLoop() {
        // Clear the canvas
        gc.clearRect(0, 0, width, height);
                    
        // Run main game object's step and draw events
        stepEvent();
        drawEvent(); 
        
        // Loop through object's step and draw events
        for (GObject o : objects) {
            o.stepEvent();
            o.drawEvent();
            o.resetReferences();
        }

        destroyFlagged();

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
     */
    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
        canvas.setWidth(width);
        canvas.setHeight(height);
    }

    /*************************
        OBJECTS
     **************************/
    /**
     * Creates object from provided class type.
     * @param x horizontal position
     * @param y vertical position
     * @param type object to create
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
        objects.add(obj);
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

    /*************************
        GETTERS & SETTERS
    **************************/

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public ArrayList<GObject> getObjects() {
        return objects;
    }
}