package fi.tuni.engine;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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
        
        final long timeStart = System.currentTimeMillis();

        createEvent();
        
        KeyFrame kf = new KeyFrame(
            Duration.seconds(0.017),                // 60 FPS
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent ae) {
                    // Clear the canvas
                    gc.clearRect(0, 0, width, height);
                    
                    stepEvent();
                    drawEvent(); 
                    
                    for (GObject o : objects) {
                        o.stepEvent();
                        o.drawEvent();
                    }
                    
                    // background image clears canvas
                    /*gc.drawImage( space, 0, 0 );
                    gc.drawImage( earth, x, y );
                    gc.drawImage( sun, 196, 196 );*/
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
        type.setX(x);
        type.setY(y);
        objects.add(type);
        type.setGc(gc);
        type.createEvent();
    }

    public void closeProgram() {
        System.exit(0);
    }
}