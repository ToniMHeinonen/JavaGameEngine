package fi.tuni.engine.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Animator {
    private AnimatedImage currentAnimation;
    private Image image;
	private int totalFrames; //Total number of frames in the sequence
    private float fps; //frames per second I.E. 24

    private int cols; //Number of columns on the sprite sheet
    private int rows; //Number of rows on the sprite sheet

    private int frameWidth; //Width of an individual frame
    private int frameHeight; //Height of an individual frame

	private int currentCol = 0;
    private int currentRow = 0;

    private long lastFrame = 0;

    public void setAnimation(AnimatedImage animatedImage) {
        this.currentAnimation = animatedImage;
        this.image = animatedImage.image;

		cols = animatedImage.cols;
        this.rows = animatedImage.rows;
		this.frameWidth = animatedImage.frameWidth;
		this.frameHeight = animatedImage.frameHeight;
		this.totalFrames = animatedImage.totalFrames;
        fps = animatedImage.fps;
        
        this.currentRow = 0;
        this.currentCol = 0;
        this.lastFrame = System.nanoTime();
    }

    public void render(GraphicsContext gc, double x, double y, double width, double height) {
		long now = System.nanoTime();
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
        }
		
		gc.drawImage(image, currentCol * frameWidth,
			currentRow * frameHeight, frameWidth, frameHeight, x, y, width, height);
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public AnimatedImage getCurrentAnimation() {
        return currentAnimation;
    }
}