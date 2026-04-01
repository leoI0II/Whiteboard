package org.model.utils;

import org.model.base.Point;

public class RectangleBBox {
    private double x;
    private double y;
    private double width;
    private double height;

    /**
     * Constructs a RectangleBBox with the specified position and dimensions.
     *
     * @param x      The x-coordinate of the top-left corner.
     * @param y      The y-coordinate of the top-left corner.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public RectangleBBox(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a RectangleBBox with default position and dimensions.
     */
    public RectangleBBox() {
        this(0, 0, 0, 0);
    }

    /**
     * Construct an axis aligned Bounding Box from the original rectangle.
     * 
     * @return BoundingBox.
     */
    public BoundingBox toBoundingBox() {
        return new BoundingBox(x, y, x + width, y + height);
    }

    /**
     * Checks if this rectangle intersects with a BoundingBox.
     *
     * @param bbox The BoundingBox to check for intersection.
     * @return true if the rectangles overlap, false otherwise.
     */
    public boolean intersects(BoundingBox bbox) {
        return !(bbox.getBottomRightX() < x ||
                bbox.getTopLeftX() > x + width ||
                bbox.getBottomRightY() < y ||
                bbox.getTopLeftY() > y + height);
    }

    /**
     * Checks if this rectangle intersects with another RectangleBBox.
     *
     * @param bbox The RectangleBBox to check for intersection.
     * @return true if the rectangles overlap, false otherwise.
     */
    public boolean intersects(final RectangleBBox bbox) {
        return intersects(bbox.toBoundingBox());
    }

    /**
     * Checks if a point is contained within this rectangle.
     *
     * @param px The x-coordinate of the point.
     * @param py The y-coordinate of the point.
     * @return true if the point is inside or on the boundary.
     */
    public boolean contains(double px, double py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    /**
     * Checks if a point is contained within this rectangle.
     *
     * @param p The Point to check for containment.
     * @return true if the point is inside or on the boundary.
     */
    public boolean contains(final Point p) {
        return contains(p.x(), p.y());
    }
    
    /**
     * Gets the x-coordinate of the top-left corner.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the top-left corner.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the width of the rectangle.
     *
     * @return The width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the rectangle.
     *
     * @return The height.
     */
    public double getHeight() {
        return height;
    }

}
