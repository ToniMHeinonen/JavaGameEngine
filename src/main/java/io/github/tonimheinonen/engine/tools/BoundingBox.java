package io.github.tonimheinonen.engine.tools;

/**
 * Holds object's collision area.
 */
public class BoundingBox {
    private double x, y, width, height;

    public BoundingBox() {}

    /**
     * Sets collision coordinate and size.
     * @param x coordinate
     * @param y coordinate
     * @param width desired width
     * @param height desired height
     */
    public void setBox(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns collision box.
     * @return collision box
     */
    public BoundingBox getBounds() {
        return this;
    }

    /**
     * Checks if this box collides with provided box.
     * @param b box to check
     * @return if collision happens
     */
    public boolean intersects(BoundingBox b) {
        return b.width > 0 && b.height > 0 && width > 0 && height > 0
            && b.x < x + width && b.x + b.width > x
            && b.y < y + height && b.y + b.height > y;
    }
}