package fi.tuni.engine.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class AnimatedImage {
	
	protected final Image image;
	public final int totalFrames; //Total number of frames in the sequence
    private float fps; //frames per second I.E. 24

    public final int cols; //Number of columns on the sprite sheet
    public final int rows; //Number of rows on the sprite sheet

    public final int frameWidth; //Width of an individual frame
    public final int frameHeight; //Height of an individual frame

	private int currentCol = 0;
    private int currentRow = 0;

    private long lastFrame = 0;
	
	public AnimatedImage(Image image, int columns, int rows, int frameWidth, int frameHeight, int totalFrames, int framesPerSecond) {
		this.image = image;

		cols = columns;
        this.rows = rows;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.totalFrames = totalFrames;
		fps = framesPerSecond;
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

    public void startOver() {
        this.currentRow = 0;
        this.currentCol = 0;
        this.lastFrame = System.nanoTime();
    }

    public double getFrameWidth() {
        return frameWidth;
    }

    public double getFrameHeight() {
        return frameHeight;
	}

	public float getFps() {
		return fps;
	}

	public void setFps(float fps) {
		this.fps = fps;
	}
}