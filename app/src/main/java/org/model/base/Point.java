package org.model.base;

/**
 * The Point record represents a two-dimensional point with x and y coordinates.
 * As a record, it is an immutable data structure, ensuring that point objects cannot be modified after creation.
 * It provides methods for basic mathematical operations.
 *
 * @param x The x-coordinate of the point.
 * @param y The y-coordinate of the point.
 */
public record Point(double x, double y) {
    
    /**
     * Multiplies the coordinates of this point by a given factor.
     *
     * @param factor The factor to multiply by.
     * @return A new Point with the scaled coordinates.
     */
    public Point multiply(double factor) {
        return new Point(x * factor, y * factor);
    }

    /**
     * Divides the coordinates of this point by a given factor.
     *
     * @param factor The factor to divide by.
     * @return A new Point with the divided coordinates.
     */
    public Point divide(double factor) {
        return new Point(x / factor, y / factor);
    }

    /**
     * Adds the coordinates of another point to this point.
     *
     * @param other The other Point to add.
     * @return A new Point representing the sum of the two points.
     */
    public Point add(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    /**
     * Subtracts the coordinates of another point from this point.
     *
     * @param other The other Point to subtract.
     * @return A new Point representing the difference between the two points.
     */
    public Point subtract(Point other) {
        return new Point(x - other.x, y - other.y);
    
    }
}
