package fi.tuni.engine.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Animator {
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

    private boolean loop;
    private boolean animationEnd;

    public void setAnimation(AnimatedImage animatedImage, boolean loop) {
        this.image = animatedImage.image;

		cols = animatedImage.cols;
        this.rows = animatedImage.rows;
		this.frameWidth = animatedImage.frameWidth;
		this.frameHeight = animatedImage.frameHeight;
		this.totalFrames = animatedImage.totalFrames;
        fps = animatedImage.fps;

        this.loop = loop;
        animationEnd = false;

        // If new sprite has less frames than where last sprite
        // left off, start from beginning
        if (currentCol + currentRow > totalFrames-2) {
            startFromBeginning();
        }
    }

    public void render(GraphicsContext gc, double x, double y, double width, double height) {
        animationEnd = false;
        long now = System.nanoTime();

        //Determine how many frames we need to advance to maintain frame rate independence
		int frameJump = (int) Math.floor((now - lastFrame) / (1000000000 / fps));

        //Do a bunch of math to determine where the viewport needs to be positioned on the sprite sheet
        if (frameJump >= 1) {
            System.out.println(currentCol);
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

            if (currentRow == 0 && currentCol == 0) {
                animationEnd = true;
                if (!loop) {
                    setCurrentFrame(totalFrames-1);
                    setFps(0);
                }
            }
        }
		
		gc.drawImage(image, currentCol * frameWidth,
			currentRow * frameHeight, frameWidth, frameHeight, x, y, width, height);
    }

    public void startFromBeginning() {
        this.currentRow = 0;
        this.currentCol = 0;
        this.lastFrame = System.nanoTime();
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public void setCurrentFrame(int index) {
        // Reset current values
        startFromBeginning();
        
        // Index starts at 0, totalFrames starts at 1
        // If given index goes over, start from 0
        if (index > totalFrames-1)
            return;

        while (index > 0) {
            currentCol++;
            index--;

            if (currentCol > cols-1) {
                currentCol = 0;
                currentRow++;
            }
        }
    }

    public boolean animationEnded() {
        return animationEnd;
    }
}