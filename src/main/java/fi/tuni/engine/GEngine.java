package fi.tuni.engine;

import fi.tuni.engine.tools.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.animation.Timeline;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public abstract class GEngine extends Application {

    private int width = 1280;
    private int height = 720;
    private Stage stage;
    private Canvas canvas;
    private GraphicsContext gc;
    private ArrayList<GObject> objects = new ArrayList<>();
    private ArrayList<String> pressedInput = new ArrayList<>();
    private ArrayList<String> releasedInput = new ArrayList<>();
    private ArrayList<String> holdInput = new ArrayList<>();

    @Override
    public void start(Stage theStage) throws Exception {
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

        // Handle input
        theScene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();

                    code = modifyInputCode(code);
 
                    // only add once... prevent duplicates
                    if (!holdInput.contains(code)) {
                        holdInput.add(code);
                        pressedInput.add(code);
                    }
                }
            });
 
        theScene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    code = modifyInputCode(code);

                    pressedInput.remove(code);
                    holdInput.remove(code);
                    releasedInput.add(code);
                }
            });

        createEvent();
        
        KeyFrame kf = new KeyFrame(
            Duration.seconds(0.017),                // 60 FPS
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent ae) {
                    // Clear the canvas
                    gc.clearRect(0, 0, width, height);
                    
                    // Run main game object's step and draw events
                    stepEvent();
                    drawEvent(); 
                    
                    // Loop through object's step and draw events
                    for (GObject o : objects) {
                        o.stepEvent();
                        o.drawEvent();
                    }

                    // Reset released input after each frame
                    resetInput();
                }
            });
        
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();

        stage.show();
    }

    public abstract void createEvent();

    public abstract void stepEvent();

    public abstract void drawEvent();

    /*************************
        WINDOW
    **************************/

    public void setWindowTitle(String title) {
        stage.setTitle(title);
    }

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
     public <T extends GObject> T createObject(int x, int y, Class<T> type) {
        // Check if provided type is derived from GObject
        if (type.getSuperclass().equals(GObject.class)) {
            try {
                GObject obj = (GObject) type.getDeclaredConstructor().newInstance(); // FIX THIS
                obj = initObject(x, y, obj);
                return (T) obj;     // Cast to provided class
            }
            catch (InstantiationException e) {} 
            catch (IllegalAccessException e) {}
            catch (NoSuchMethodException e) {}
            catch (InvocationTargetException e) {}
        } else {
            throw new IllegalArgumentException("Object must extend GObject");
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends GObject> T createObject(int x, int y, GObject type) {
        return (T) initObject(x, y, type);
    }

    private GObject initObject(int x, int y, GObject obj) {
        obj.setMainClass(this);
        obj.setX(x);
        obj.setY(y);
        objects.add(obj);
        obj.setGraphicsContext(gc);
        obj.createEvent();
        return obj;
    }

    /*************************
        INPUT
    **************************/

    public boolean isKeyPressed(String key) {
        return checkInput(pressedInput, key);
    }

    public boolean isKeyPressedHold(String key) {
        return checkInput(holdInput, key);
    }

    public boolean isKeyReleased(String key) {
        return checkInput(releasedInput, key);
    }

    private boolean checkInput(ArrayList<String> input, String key) {
        // Change to uppercase to allow lowercase code
        key = key.toUpperCase();

        if (input.contains(key))
            return true;

        return false;
    }

    /**
     * Pressed input and released input will be cleared after each frame.
     */
    private void resetInput() {
        if (!releasedInput.isEmpty())
            releasedInput.clear();

        if (!pressedInput.isEmpty())
            pressedInput.clear();
    }

    private String modifyInputCode(String code) {
        // Remove word DIGIT from numbers
        if (code.startsWith("DIGIT"))
            code = code.substring(5, code.length());

        return code;
    }

    /*************************
        SYSTEM
    **************************/

    public void closeProgram() {
        System.exit(0);
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