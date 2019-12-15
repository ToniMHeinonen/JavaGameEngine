package io.github.tonimheinonen.engine.tools;

/**
 * Holds object's collision area.
 */
public class BoundingBox {
    private double x, y, width, height;
    private double xOffset, yOffset, widthOffset, heightOffset;
    private double origXOffset, origYOffset, origWidthOffset, origHeightOffset;

    /**
     * Default constructor for class.
     */
    public BoundingBox() {}

    /**
     * Sets collision coordinate and size.
     * @param x coordinate
     * @param y coordinate
     * @param width desired width
     * @param height desired height
     */
    public void setBox(double x, double y, double width, double height) {
        this.x = x + xOffset;
        this.y = y + yOffset;
        this.width = width + widthOffset;
        this.height = height + heightOffset;
    }

    /**
     * Sets offset for bounds values.
     * @param x coordinate offset
     * @param y coordinate offset
     * @param width of the bounds
     * @param height of the bounds
     */
    public void setOffset(double x, double y, double width, double height) {
        this.xOffset = x;
        this.yOffset = y;
        this.widthOffset = width - this.width;
        this.heightOffset = height - this.height;

        // Initialize original offset values for resizing
        this.origXOffset = this.xOffset;
        this.origYOffset = this.yOffset;
        this.origWidthOffset = this.widthOffset;
        this.origHeightOffset = this.heightOffset;
    }

    /**
     * Resizes bounds according to percent.
     * @param percentX percent value for x and width
     * @param percentY percent value for y and height
     */
    public void resizeOffset(double percentX, double percentY) { 
        this.xOffset = this.origXOffset * percentX;
        this.yOffset = this.origYOffset * percentY;
        this.widthOffset = this.origWidthOffset * percentX;
        this.heightOffset = this.origHeightOffset * percentY;
    }

    /**
     * Resizes bounds according to percent.
     * @param percent value for whole bounds
     */
    public void resizeOffset(double percent) {
        this.xOffset = this.origXOffset * percent;
        this.yOffset = this.origYOffset * percent;
        this.widthOffset = this.origWidthOffset * percent;
        this.heightOffset = this.origHeightOffset * percent;
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

    /**
     * Check if provided coordinate is inside bounds.
     * @param x coordinate
     * @param y coordinate
     * @return true if coordinate is inside bounds
     */
    public boolean contains(double x, double y) {
        return x >= this.x && x <= this.x + this.width &&
            y >= this.y && y <= this.y + this.height;
    }

    /**
     * Returns x coordinate of the bounds.
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns y coordinate of the bounds.
     * @return y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns width of the bounds.
     * @return width of the bounds
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns height of the bounds.
     * @return height of the bounds
     */
    public double getHeight() {
        return height;
    }
}