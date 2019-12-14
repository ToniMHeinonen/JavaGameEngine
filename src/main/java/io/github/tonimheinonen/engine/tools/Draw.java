package io.github.tonimheinonen.engine.tools;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public abstract class Draw {
    
    private static GraphicsContext gc;
    private static double textSize = 20;
    private static String textFont = "Verdana";

    /**
     * Initialize default drawing values.
     */
    static {
        /*gc.setFont(new Font(textFont, textSize));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.WHITE);*/
    }

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

     */
    public static void setTextFont(String font) {
        gc.setFont(new Font(font, textSize));
    }

    /**
     * Sets size for drawn text.
     * @param size size of the font (e. 12)
     */
    public static void setTextSize(double size) {
        gc.setFont(new Font(textFont, size));
    }

    /**
     * Sets horizontal alignment of text.
     * @param align value to set (HA_LEFT / HA_CENTER / HA_RIGHT)
     */
    public static void setHorizontalAlign(TextAlignment align) {
        gc.setTextAlign(align);
    }

    /**
     * Sets vertical alignment of text.
     * @param align value to set (VA_TOP / VA_CENTER / VA_BOTTOM)
     */
    public static void setVerticalAlign(VPos align) {
        gc.setTextBaseline(align);
    }

    /**
     * Sets graphicsContext to be drawn on.
     * @param graphicsContext to be drawn on
     */
    public static void setGraphicsContext(GraphicsContext graphicsContext) {
        gc = graphicsContext;
    }
}