package io.github.tonimheinonen.engine.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public abstract class Draw {
    
    private static GraphicsContext gc;

    /**
     * Draws image to provided coordinate and size.
     * @param img image to draw
     * @param x coordinate
     * @param y coordinate
     * @param width desired width
     * @param height desired height
     */
    public static void image(Image img, double x, double y,
    double width, double height) {
        gc.drawImage(img, x, y, width, height);
    }

    /**
     * Draws image to provided coordinate.
     * @param img image to draw
     * @param x coordinate
     * @param y coordinate
     */
    public static void image(Image img, double x, double y) {
        gc.drawImage(img, x, y, img.getWidth(), img.getHeight());
    }
    
    /**
     * Draws animated image to provided coordinate and size.
     * @param img image to draw
     * @param x coordinate
     * @param y coordinate
     * @param width desired width
     * @param height desired height
     */
    public static void animatedImage(AnimatedImage img, double x, double y,
                 double width, double height) {
        img.render(gc, x, y, width, height);
    }

    /**
     * Draws image to provided coordinate.
     * @param img image to draw
     * @param x coordinate
     * @param y coordinate
     */
    public static void animatedImage(AnimatedImage img, double x, double y) {
        img.render(gc, x, y, img.getFrameWidth(), img.getFrameHeight());
    }

    /**
     * Draws text with current color.
     * @param text to draw
     * @param x coordinate
     * @param y coordinate
     */
    public static void text(String text, double x, double y) {
        gc.fillText(text, x, y);
    }

    /**
     * Sets color for drawn objects.
     * @param color to be used for drawing
     */
    public static void setColor(Paint color) {
        gc.setFill(color);
    }

    /**
     * Sets font for drawn text.
     * @param font name of the font (e. "Verdana")
     * @param size size of the font (e. 12)
     */
    public static void setFont(String font, int size) {
        gc.setFont(new Font(font, size));
    }

    /**
     * Sets graphicsContext to be drawn on.
     * @param graphicsContext to be drawn on
     */
    public static void setGraphicsContext(GraphicsContext graphicsContext) {
        gc = graphicsContext;
    }
}