package fi.tuni.engine.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class AnimatedImage
{
	protected final Image source;
	protected final double frameWidth;
	protected final double frameHeight;
	protected final int frameCount;
	protected int frameRepeat;
	protected int currentFrame;
	
	public AnimatedImage(Image source, double frameWidth, double frameHeight, int frameCount, int frameRepeat) {
		this.source = source;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameCount = frameCount;
		this.frameRepeat = frameRepeat;
		currentFrame = 0;
	}
	
	public void render(GraphicsContext gc, double x, double y, double width, double height) {
		currentFrame++;
		if(currentFrame/frameRepeat >= frameCount) {
			currentFrame = 0;
		}
		gc.drawImage(source, (int)(currentFrame/frameRepeat)*frameWidth,
			0, frameWidth, frameHeight, x, y, width, height);
    }

    public double getFrameWidth() {
        return frameWidth;
    }

    public double getFrameHeight() {
        return frameHeight;
    }
}