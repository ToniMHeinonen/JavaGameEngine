package fi.tuni.engine;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public abstract class GEngine extends Application {

    private boolean isRunning = true;
    private int fps = 60;
    private int width = 1280;
    private int height = 720;
    private Stage stage;

    @Override
    public void start(Stage theStage) throws Exception {
        this.stage = theStage;
        stage.setTitle("GEngine");
        stage.centerOnScreen();

        createEvent();

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );
        
        Canvas canvas = new Canvas( 512, 512 );
        root.getChildren().add( canvas );
        
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );
        
        final long timeStart = System.currentTimeMillis();
        
        KeyFrame kf = new KeyFrame(
            Duration.seconds(0.017),                // 60 FPS
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent ae)
                {
                    stepEvent();
                    drawEvent(); 
                    
                    // Clear the canvas
                    gc.clearRect(0, 0, width, height);
                    
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

    public void run() {
        System.out.println("run");
        int test = 500;
        while (isRunning) {
            long time = System.currentTimeMillis();
            System.out.println("Here");
            test--;

            if (test < 0)
                isRunning = false;
               
            stepEvent();
            drawEvent();
            
            //  delay for each frame  -   time it took for one frame
            time = (1000 / fps) - (System.currentTimeMillis() - time);
            
            if (time > 0)
            {
                    try
                    {
                            Thread.sleep(time);
                    }
                    catch(Exception e){}
            }
        }

        System.exit(0);
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
    }

    public void closeProgram() {
        System.exit(0);
    }
}