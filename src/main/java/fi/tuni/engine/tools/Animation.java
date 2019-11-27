package fi.tuni.engine.tools;

import javafx.scene.image.Image;

public class Animation {

    //public final ImageView imageView; //Image view that will display our sprite
    public final Image image;

    public final int totalFrames; //Total number of frames in the sequence
    private float fps; //frames per second I.E. 24

    public final int cols; //Number of columns on the sprite sheet
    public final int rows; //Number of rows on the sprite sheet

    public final int frameWidth; //Width of an individual frame
    public final int frameHeight; //Height of an individual frame

    public Animation(Image image, int columns, int rows, int totalFrames, int frameWidth, int frameHeight, float framesPerSecond) {
        this.image = image;

        cols = columns;
        this.rows = rows;
        this.totalFrames = totalFrames;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        fps = framesPerSecond;
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public float getFps() {
        return fps;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }
}