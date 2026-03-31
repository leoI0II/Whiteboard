package org.model;

/**
 * The Viewport class represents the state of the virtual camera, including its position (offset) and zoom level.
 * It provides methods to get and set these properties, which are essential for converting between
 * screen coordinates and world coordinates.
 */
public class Viewport {
    
    private long offsetX;
    private long offsetY;
    private double zoom;

    /**
     * Constructs a Viewport with the specified offset and zoom level.
     *
     * @param offsetX The initial horizontal offset.
     * @param offsetY The initial vertical offset.
     * @param zoom The initial zoom level.
     */
    public Viewport(long offsetX, long offsetY, double zoom) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.zoom = zoom;
    }

    /**
     * Gets the horizontal offset of the viewport.
     *
     * @return The horizontal offset.
     */
    public long getOffsetX() {
        return offsetX;
    }

    /**
     * Sets the horizontal offset of the viewport.
     *
     * @param offsetX The new horizontal offset.
     */
    public void setOffsetX(long offsetX) {
        this.offsetX = offsetX;
    }

    /**
     * Gets the vertical offset of the viewport.
     *
     * @return The vertical offset.
     */
    public long getOffsetY() {
        return offsetY;
    }

    /**
     * Sets the vertical offset of the viewport.
     *
     * @param offsetY The new vertical offset.
     */
    public void setOffsetY(long offsetY) {
        this.offsetY = offsetY;
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
