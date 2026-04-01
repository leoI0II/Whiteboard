package org.model.utils;

import org.model.base.Point;

/**
 * Represents an axis aligned bounding box.
 */
public class BoundingBox {
    private double topLeftX;
    private double topLeftY;
    private double bottomRightX;
    private double bottomRightY;

    /**
     * Constructs a BoundingBox with the given coordinates.
     *
     * @param tlX The x-coordinate of the top-left corner.
     * @param tlY The y-coordinate of the top-left corner.
     * @param brX The x-coordinate of the bottom-right corner.
     * @param brY The y-coordinate of the bottom-right corner.
     */
    public BoundingBox(double tlX, double tlY, double brX, double brY) {
        this.topLeftX = tlX;
        this.topLeftY = tlY;
        this.bottomRightX = brX;
        this.bottomRightY = brY;
    }

    /**
     * Constructs a BoundingBox with default coordinates.
     */
    public BoundingBox() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs a BoundingBox from two Point objects.
     *
     * @param tl The top-left corner Point.
     * @param br The bottom-right corner Point.
     */
    public BoundingBox(final Point tl, final Point br) {
        this(tl.x(), tl.y(), br.x(), br.y());
    }

    /**
     * @return The x-coordinate of the top-left corner.
     */
    public double getTopLeftX() {
        return topLeftX;
    }

    /**
     * @return The y-coordinate of the top-left corner.
     */
    public double getTopLeftY() {
        return topLeftY;
    }

    /**
     * @return A Point representing the top-left corner.
     */
    public Point getTopLeft() {
        return new Point(topLeftX, topLeftY);
    }

    /**
     * Sets the x-coordinate of the top-left corner.
     *
     * @param x The new x-coordinate.
     */
    public void setTopLeftX(double x) {
        this.topLeftX = x;
    }

    /**
     * Sets the y-coordinate of the top-left corner.
     *
     * @param y The new y-coordinate.
     */
    public void setTopLeftY(double y) {
        this.topLeftY = y;
    }

    /**
     * Sets the coordinates of the top-left corner.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void setTopLeft(double x, double y) {
        this.topLeftX = x;
        this.topLeftY = y;
    }

    /**
     * Sets the coordinates of the top-left corner.
     *
     * @param p The new top-left corner Point.
     */
    public void setTopLeft(final Point p) {
        setTopLeft(p.x(), p.y());
    }

    /**
     * @return The x-coordinate of the bottom-right corner.
     */
    public double getBottomRightX() {
        return bottomRightX;
    }
    
    /**
     * @return The y-coordinate of the bottom-right corner.
     */
    public double getBottomRightY() {
        return bottomRightY;
    }
    
    /**
     * @return A Point representing the bottom-right corner.
     */
    public Point getBottomRight() {
        return new Point(bottomRightX, bottomRightY);
    }

    /**
     * Sets the x-coordinate of the bottom-right corner.
     *
     * @param x The new x-coordinate.
     */
    public void setBottomRightX(double x) {
        this.bottomRightX = x;
    }

    /**
     * Sets the y-coordinate of the bottom-right corner.
     *
     * @param y The new y-coordinate.
     */
    public void setBottomRightY(double y) {
        this.bottomRightY = y;
    }

    /**
     * Sets the coordinates of the bottom-right corner.
     *
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void setBottomRight(double x, double y) {
        this.bottomRightX = x;
        this.bottomRightY = y;
    }

    /**
     * Sets the coordinates of the bottom-right corner.
     *
     * @param p The new bottom-right corner Point.
     */
    public void setBottomRight(final Point p) {
        setBottomRight(p.x(), p.y());
    }
    
}