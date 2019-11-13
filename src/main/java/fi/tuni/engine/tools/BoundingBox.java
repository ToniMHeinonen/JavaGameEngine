package fi.tuni.engine.tools;

public class BoundingBox {
    private double x, y, width, height;

    public BoundingBox() {}

    public void setBox(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public BoundingBox getBounds() {
        return this;
    }

    public boolean intersects(BoundingBox b) {
        return b.width > 0 && b.height > 0 && width > 0 && height > 0
            && b.x < x + width && b.x + b.width > x
            && b.y < y + height && b.y + b.height > y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}