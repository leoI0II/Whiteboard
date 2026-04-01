package org.model.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.model.base.context.BrushContext;
import org.model.interfaces.Drawable;
import org.model.interfaces.Erasable;
import org.model.utils.BoundingBox;

/**
 * The Stroke class represents a single continuous line drawn by the user.
 * It implements the Drawable interface, allowing it to be rendered on a canvas. A stroke consists of a list of points,
 * a thickness, and a color.
 */
public class Stroke implements Drawable, Erasable {
    final private List<Point> points;
    private BrushContext context;
    private BoundingBox boundingBox;

    /**
     * Constructs a new Stroke with a specified thickness and color.
     * The list of points is initialized as an empty ArrayList.
     *
     * @param thickness The thickness of the stroke.
     * @param color The color of the stroke.
     */
    public Stroke(final double thickness, final ARGBColor color) {
        this.points = new ArrayList<>();
        this.context = new BrushContext(thickness, color);
        boundingBox = new BoundingBox();
    }

    public Stroke(BrushContext context) {
        this.points = new ArrayList<>();
        this.context = Objects.requireNonNull(context);
        boundingBox = new BoundingBox();
    }

    /**
     * Constructs a new Stroke from a pre-existing list of points.
     *
     * @param points The list of points that make up the stroke.
     */
    public Stroke(List<Point> points) {
        this.points = points;
        this.context = new BrushContext(); // Use default brush context
        boundingBox = new BoundingBox();
    }

    private void updateBoundingBox(final Point point) {
        // check top left x
        if (point.x() < boundingBox.getTopLeftX()) {
            boundingBox.setTopLeftX(point.x());
        }
        // check top left y
        if (point.x() > boundingBox.getBottomRightX()) {
            boundingBox.setBottomRightX(point.x());
        }
        //check bottom right x
        if (point.y() < boundingBox.getTopLeftY()) {
            boundingBox.setTopLeftY(point.y());
        }
        // check bottom right y
        if (point.y() > boundingBox.getBottomRightY()) {
            boundingBox.setBottomRightY(point.y());
        }
    }

    /**
     * Adds a new point to the stroke's path.
     *
     * @param point
     */
    public void addPoint(final Point point) {
        points.add(point);
        updateBoundingBox(point);
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
     * Gets the bounding box that encompasses all points in the stroke.
     * @return The bounding box of the stroke.
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Gets the BrushContext associated with this stroke, which contains properties like thickness and color.
     * @return The BrushContext of the stroke.
     */
    public BrushContext getContext() {
        return context;
    }

    /**
     * Gets the thickness of the stroke.
     *
     * @return The stroke thickness.
     */
    public double getThickness() {
        return context.getThickness();
    }

    /**
     * Sets the thickness of the stroke.
     *
     * @param thickness The new thickness value.
     */
    public void setThickness(float thickness) {
        context.setThickness(thickness);
    }

    /**
     * Gets the color of the stroke.
     *
     * @return The ARGBColor of the stroke.
     */
    public ARGBColor getColor() {
        return context.getColor();
    }

    /**
     * Sets the color of the stroke.
     *
     * @param color The new ARGBColor for the stroke.
     */
    public void setColor(ARGBColor color) {
        context.setColor(color);
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
