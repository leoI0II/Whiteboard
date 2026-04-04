package org.model;

import org.model.base.Point;

/**
 * The Viewport class represents the state of the virtual camera, including its position (offset) and zoom level.
 * It provides methods to get and set these properties, which are essential for converting between
 * screen coordinates and world coordinates.
 */
public class Viewport {
    
    private double offsetX;
    private double offsetY;
    private double zoom;

    /**
     * Constructs a Viewport with the specified offset and zoom level.
     *
     * @param offsetX The initial horizontal offset.
     * @param offsetY The initial vertical offset.
     * @param zoom The initial zoom level.
     */
    public Viewport(double offsetX, double offsetY, double zoom) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.zoom = zoom;
    }

    /**
     * Converts a screen X coordinate to a world X coordinate.
     *
     * @param x The screen X coordinate.
     * @return The world X coordinate.
     */
    public double screenToWorldX(double x) {
        return x / getZoom() + getOffsetX();
    }

    /**
     * Converts a screen Y coordinate to a world Y coordinate.
     *
     * @param y The screen Y coordinate.
     * @return The world Y coordinate.
     */
    public double screenToWorldY(double y) {
        return y / getZoom() + getOffsetY();
    }

    /**
     * Converts screen coordinates to world coordinates.
     *
     * @param x The screen X coordinate.
     * @param y The screen Y coordinate.
     * @return A Point object representing the corresponding world coordinates.
     */
    public Point screenToWorld(double x, double y) {
        return new Point(screenToWorldX(x), screenToWorldY(y));
    }

    /**
     * Converts a world X coordinate to a screen X coordinate.
     * @param x The world X coordinate.
     * @return The screen X coordinate.
     */
    public double worldToScreenX(double x) {
        return (x - getOffsetX()) * getZoom();
    }

    /**
     * Converts a world Y coordinate to a screen Y coordinate.
     * @param y The world Y coordinate.
     * @return The screen Y coordinate.
     */
    public double worldToScreenY(double y) {
        return (y - getOffsetY()) * getZoom();
    }

    /**
     * Converts world coordinates to screen coordinates.
     *
     * @param x The world X coordinate.
     * @param y The world Y coordinate.
     * @return A Point object representing the corresponding screen coordinates.
     */
    public Point worldToScreen(double x, double y) {
        return new Point(worldToScreenX(x), worldToScreenY(y));
    }

    /**
     * Gets the thickness adjusted for the current zoom level.
     * @param thickness The original thickness.
     * @return The adjusted thickness.
     */
    public double getZoomedThickness(double thickness) {
        return thickness / zoom;
    }

    /**
     * Constructs a Viewport with default offset (0, 0) and zoom level (1).
     */
    public Viewport() {
        this(0, 0, 1);
    }

    /**
     * Gets the horizontal offset of the viewport.
     *
     * @return The horizontal offset.
     */
    public double getOffsetX() {
        return offsetX;
    }

    /**
     * Sets the horizontal offset of the viewport.
     *
     * @param offsetX The new horizontal offset.
     */
    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    /**
     * Gets the vertical offset of the viewport.
     *
     * @return The vertical offset.
     */
    public double getOffsetY() {
        return offsetY;
    }

    /**
     * Sets the vertical offset of the viewport.
     *
     * @param offsetY The new vertical offset.
     */
    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    /**
     * Sets the offset of the viewport.

     * @param x
     * @param y
     */
    public void setOffset(double x, double y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    /**
     * Gets the current offset of the viewport.
     *
     * @return Point representing the current offset.
     */
    public Point getOffset() {
        return new Point(offsetX, offsetY);
    }

    /**
     * Gets the zoom level of the viewport.
     *
     * @return The zoom level.
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Sets the zoom level of the viewport.
     *
     * @param zoom The new zoom level.
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

}
