package fi.tuni.engine;
/*

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class GEngine extends Application {

    private boolean isRunning = true;
    private int fps = 60;
    private int width = 1280;
    private int height = 720;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("GEngine");
        stage.centerOnScreen();

        createEvent();

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);

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
}*/