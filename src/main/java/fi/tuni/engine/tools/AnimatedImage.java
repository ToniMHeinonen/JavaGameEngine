package fi.tuni.engine.tools;

import javafx.scene.image.Image;

public class AnimatedImage extends Animator {
    
    public AnimatedImage(Image image, int columns, int rows, int frameWidth,
        int frameHeight, int totalFrames, int framesPerSecond) {
		this.image = image;

		this.cols = columns;
        this.rows = rows;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.totalFrames = totalFrames;
		this.fps = framesPerSecond;
    }
}