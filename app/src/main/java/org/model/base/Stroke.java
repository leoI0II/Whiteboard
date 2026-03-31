package org.model.base;

import java.util.ArrayList;
import java.util.List;

import org.model.interfaces.Drawable;

/**
 * The Stroke class represents a single continuous line drawn by the user.
 * It implements the Drawable interface, allowing it to be rendered on a canvas. A stroke consists of a list of points,
 * a thickness, and a color.
 */
public class Stroke implements Drawable {
    final private List<Point> points;
    private double thickness;
    private ARGBColor color;

    /**
     * Constructs a new Stroke with a specified thickness and color.
     * The list of points is initialized as an empty ArrayList.
     *
     * @param thickness The thickness of the stroke.
     * @param color The color of the stroke.
     */
    public Stroke(final double thickness, final ARGBColor color) {
        this.points = new ArrayList<>();
        this.thickness = thickness;
        this.color = color;
    }

    /**
     * Constructs a new Stroke from a pre-existing list of points.
     *
     * @param points The list of points that make up the stroke.
     */
    public Stroke(List<Point> points) {
        this.points = points;
    }

    /**
     * Gets the list of points that define the stroke's path.
     *
     * @return The list of points.
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Gets the thickness of the stroke.
     *
     * @return The stroke thickness.
     */
    public double getThickness() {
        return thickness;
    }

    /**
     * Sets the thickness of the stroke.
     *
     * @param thickness The new thickness value.
     */
    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    /**
     * Gets the color of the stroke.
     *
     * @return The ARGBColor of the stroke.
     */
    public ARGBColor getColor() {
        return color;
    }

    /**
     * Sets the color of the stroke.
     *
     * @param color The new ARGBColor for the stroke.
     */
    public void setColor(ARGBColor color) {
        this.color = color;
    }

    /**
     * Renders the stroke. This method is currently not implemented.
     *
     * @throws UnsupportedOperationException always.
     */
    @Override
    public void render() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Render'");
    }

}
