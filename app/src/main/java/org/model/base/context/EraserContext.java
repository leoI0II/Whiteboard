package org.model.base.context;

import org.model.utils.BoundingBox;

/**
 * Represents the context for an eraser tool, including properties like its radius.
 * This class provides methods to manage the eraser's size and to determine its bounding box at a given point.
 */
public class EraserContext {
    
    private double radius;

    private static final double DEFAULT_RADIUS = 5;

    /**
     * Constructs a new EraserContext with a specified radius.
     *
     * @param radius The radius of the eraser.
     */
    public EraserContext(final double radius) {
        this.radius = radius;
    }

    /**
     * Constructs a new EraserContext with the default radius.
     */
    public EraserContext() {
        this(DEFAULT_RADIUS);
    }

    /**
     * Sets the radius of the eraser.
     *
     * @param radius The new radius for the eraser.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Gets the current radius of the eraser.
     *
     * @return The radius of the eraser.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Calculates the bounding box for the eraser at a given center point.
     * The bounding box is a square centered at (x, y) with sides of length 2 * radius.
     *
     * @param x The x-coordinate of the center of the eraser.
     * @param y The y-coordinate of the center of the eraser.
     * @return A {@link BoundingBox} representing the area covered by the eraser.
     */
    public BoundingBox getBoundingBox(final double x, final double y) {
        double _x = x - radius;
        double _y = y - radius;
        double __x = x + radius;
        double __y = y + radius;
        return new BoundingBox(_x, _y, __x, __y);
    }

}
