package org.model.base;

public class BrushContext {
    
    private double thickness;
    private ARGBColor color;

    private static final double DEFAULT_THICKNESS = 10;
    private static final ARGBColor DEFAULT_COLOR = ARGBColor.BLACK;

    /**
     * Constructs a BrushContext with the specified thickness and color.
     *
     * @param thickness The thickness of the brush.
     * @param color The color of the brush.
     */
    public BrushContext(double thickness, ARGBColor color) {
        this.thickness = thickness;
        this.color = color;
    }

    public BrushContext() {
        this(DEFAULT_THICKNESS, DEFAULT_COLOR);
    }

    /**
     * Gets the thickness of the brush.
     *
     * @return The brush thickness.
     */
    public double getThickness() {
        return thickness;
    }

    /**
     * Sets the thickness of the brush.
     *
     * @param thickness The new thickness value.
     */
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    /**
     * Gets the color of the brush.
     *
     * @return The ARGBColor of the brush.
     */
    public ARGBColor getColor() {
        return color;
    }

    /**
     * Sets the color of the brush.
     *
     * @param color The new ARGBColor for the brush.
     */
    public void setColor(ARGBColor color) {
        this.color = color;
    }

}
