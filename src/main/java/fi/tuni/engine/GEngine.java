package fi.tuni.engine;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.animation.Timeline;

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
    private ArrayList<String> input = new ArrayList<>();
    private ArrayList<String> releasedInput = new ArrayList<>();

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
 
                    // only add once... prevent duplicates
                    if (!input.contains(code))
                        addInput(code);
                }
            });
 
        theScene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    input.remove(code);
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

    public void setWindowTitle(String title) {
        stage.setTitle(title);
    }

    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
        canvas.setWidth(width);
        canvas.setHeight(height);
    }

    public void createObject(int x, int y, GObject type) {
        type.setMainClass(this);
        type.setX(x);
        type.setY(y);
        objects.add(type);
        type.setGraphicsContext(gc);
        type.createEvent();
    }

    /*************************
        INPUT
    **************************/

    public boolean isKeyPressed(String key) {
        // Change to uppercase to allow lowercase code
        key = key.toUpperCase();
        
        if (input.contains(key))
            return true;

        return false;
    }

    public boolean isKeyReleased(String key) {
        // Change to uppercase to allow lowercase code
        key = key.toUpperCase();

        if (releasedInput.contains(key))
            return true;

        return false;
    }

    private void addInput(String code) {
        // Remove word DIGIT from numbers
        if (code.startsWith("DIGIT"))
            code = code.substring(5, code.length());

        input.add(code);
    }

    private void resetInput() {
        if (!releasedInput.isEmpty())
            releasedInput.clear();
    }

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
}