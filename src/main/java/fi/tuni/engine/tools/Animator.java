package fi.tuni.engine.tools;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Animator extends AnimationTimer {

    private ImageView imageView; //Image view that will display our sprite
    private Animation currentAnim;

    private int totalFrames; //Total number of frames in the sequence
    private float fps; //frames per second I.E. 24

    private int cols; //Number of columns on the sprite sheet
    private int rows; //Number of rows on the sprite sheet

    private int frameWidth; //Width of an individual frame
    private int frameHeight; //Height of an individual frame

    private int currentCol = 0;
    private int currentRow = 0;

    private long lastFrame = 0;

    public Animator(ImageView view) {
        this.imageView = view;
    }

    public void playAnimation(Animation anim) {
        currentAnim = anim;
        cols = anim.cols;
        this.rows = anim.rows;
        this.totalFrames = anim.totalFrames;
        this.frameWidth = anim.frameWidth;
        this.frameHeight = anim.frameHeight;
        fps = anim.getFps();

        imageView.setImage(anim.image);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));

        lastFrame = System.nanoTime();

        this.start();
    }

    @Override
    public void handle(long now) {
        fps = currentAnim.getFps();
        
        int frameJump = (int) Math.floor((now - lastFrame) / (1000000000 / fps)); //Determine how many frames we need to advance to maintain frame rate independence

        //Do a bunch of math to determine where the viewport needs to be positioned on the sprite sheet
        if (frameJump >= 1) {
            lastFrame = now;
            int addRows = (int) Math.floor((float) frameJump / (float) cols);
            int frameAdd = frameJump - (addRows * cols);

            if (currentCol + frameAdd >= cols) {
                currentRow += addRows + 1;
                currentCol = frameAdd - (cols - currentCol);
            } else {
                currentRow += addRows;
                currentCol += frameAdd;
            }
            currentRow = (currentRow >= rows) ? currentRow - ((int) Math.floor((float) currentRow / rows) * rows) : currentRow;

            //The last row may or may not contain the full number of columns
            if ((currentRow * cols) + currentCol >= totalFrames) {
                currentRow = 0;
                currentCol = Math.abs(currentCol - (totalFrames - (int) (Math.floor((float) totalFrames / cols) * cols)));
            }

            imageView.setViewport(new Rectangle2D(currentCol * frameWidth, currentRow * frameHeight, frameWidth, frameHeight));
        }
    }
}