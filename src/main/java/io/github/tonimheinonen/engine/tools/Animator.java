package io.github.tonimheinonen.engine.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Animates and draws given animated image.
 */
public class Animator {
    Image image;
	int totalFrames; //Total number of frames in the sequence
    float fps; //frames per second I.E. 24

    int cols; //Number of columns on the sprite sheet
    int rows; //Number of rows on the sprite sheet

    int frameWidth; //Width of an individual frame
    int frameHeight; //Height of an individual frame

	int currentCol = 0;
    int currentRow = 0;

    long lastFrame = 0;

    boolean loop = true;
    boolean animationEnd;

    /**
     * Retrieves values from animatedImage to animate it correctly.
     * @param animatedImage image to animate
     */
    public void setAnimation(AnimatedImage animatedImage) {
        this.image = animatedImage.image;

		this.cols = animatedImage.cols;
        this.rows = animatedImage.rows;
		this.frameWidth = animatedImage.frameWidth;
		this.frameHeight = animatedImage.frameHeight;
		this.totalFrames = animatedImage.totalFrames;
        this.fps = animatedImage.fps;

        this.animationEnd = false;

        /* If new sprite has less frames than where last sprite
           left off, start from beginning */
        if (currentCol + currentRow > totalFrames-1) {
            startFromBeginning();
        }
    }

    /**
     * Draws image on screen and animates it.
     * @param gc GraphicsContext to which it will be drawn
     * @param x coordinate
     * @param y coordinate
     * @param width of frame
     * @param height of frame
     */
    public void render(GraphicsContext gc, double x, double y, double width, double height) {
        animationEnd = false;
        long now = System.nanoTime();

        //Determine how many frames we need to advance to maintain frame rate independence
		int frameJump = (int) Math.floor((now - lastFrame) / (1000000000 / fps));

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

    /**
     * Starts animation from beginning.
     */
    public void startFromBeginning() {
        this.currentRow = 0;
        this.currentCol = 0;
        this.lastFrame = System.nanoTime();
    }

    /**
     * Sets animation speed.
     * @param fps speed to set
     */
    public void setFps(float fps) {
        this.fps = fps;
    }

    /**
     * Sets if animation loops.
     * @param loop whether to loop
     */
    public void setLooping(boolean loop) {
        this.loop = loop;
    }

    /**
     * Selects frame from image.
     * 
     * If you want to draw only certain part of sprite sheet,
     * set desired frame and set fps to 0.
     * @param index set animation to this frame
     */
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

    /**
     * Returns if animation has just ended.
     * @return if animation ended
     */
    public boolean animationEnded() {
        return animationEnd;
    }

    /**
     * Returns current image.
     * @return current image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns total frame count
     * @return total frame count
     */
    public int getTotalFrames() {
        return totalFrames;
    }

    /**
     * Returns current fps.
     * @return current fps
     */
    public float getFps() {
        return fps;
    }

    /**
     * Returns current columns count.
     * @return current columns count
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns current rows count.
     * @return current rows count
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns current image's frame width.
     * @return current image's frame width
     */
    public int getFrameWidth() {
        return frameWidth;
    }

    /**
     * Returns current image's frame height.
     * @return current image's frame height
     */
    public int getFrameHeight() {
        return frameHeight;
    }

    /**
     * Returns true if current image is looping.
     * @return whether image is looping or not
     */
    public boolean isLoop() {
        return loop;
    }
}