package org.Controller;

import java.util.Objects;

import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.ARGBColor;
import org.model.base.BrushContext;
import org.model.base.Point;
import org.model.base.Stroke;
import org.model.interfaces.Controller;
import org.model.interfaces.Drawable;

/**
 * The PenController class is responsible for handling user input to draw strokes on the canvas.
 * It translates mouse events into drawing actions, creating and modifying strokes based on the user's interaction.
 */
public class PenController extends Controller {
    
    private Stroke currentStroke;
    private DrewPool strokePool;
    private Viewport viewport;
    private BrushContext context;

    /**
     * Constructs a PenController with the specified stroke pool and viewport.
     *
     * @param strokePool The pool to which drawn strokes are added. Must not be null.
     * @param viewport The viewport used for coordinate transformations. Must not be null.
     */
    public PenController(final DrewPool strokePool, final Viewport viewport) {
        currentStroke = null;
        this.strokePool = Objects.requireNonNull(strokePool);
        this.viewport = Objects.requireNonNull(viewport);
    }

    /**
     * Converts screen coordinates to world coordinates based on the current viewport settings.
     *
     * @param x The x-coordinate on the screen.
     * @param y The y-coordinate on the screen.
     * @return A Point object representing the corresponding world coordinates.
     */
    private Point getWorldPoint(double x, double y) {
        x = (x / viewport.getZoom()) + viewport.getOffsetX();
        y = (y / viewport.getZoom()) + viewport.getOffsetY();
        return new Point(x, y);
    }

    /**
     * Handles the mouse pressed event to begin a new stroke.
     * A new stroke is created with a default thickness and color, and added to the stroke pool.
     *
     * @param x The x-coordinate of the mouse press.
     * @param y The y-coordinate of the mouse press.
     */
    @Override
    protected void onMousePressed(double x, double y) {
        double thickness = context.getThickness();
        ARGBColor color = context.getColor();
        thickness = thickness / viewport.getZoom();
        currentStroke = new Stroke(thickness, color);
        strokePool.addObject(currentStroke);
        
        onMouseDragged(x, y);
    }

    /**
     * Handles the mouse dragged event to add points to the current stroke.
     * If the mouse is pressed, the current screen coordinates are converted to world coordinates and added to the stroke.
     *
     * @param x The x-coordinate of the mouse drag.
     * @param y The y-coordinate of the mouse drag.
     */
    @Override
    protected void onMouseDragged(double x, double y) {
        if (isPressed()) {
            currentStroke.addPoint(getWorldPoint(x, y));
        }
    }

    /**
     * Handles the mouse released event to finalize the current stroke.
     * The reference to the current stroke is cleared.
     *
     * @param x The x-coordinate of the mouse release.
     * @param y The y-coordinate of the mouse release.
     */
    @Override
    protected void onMouseReleased(double x, double y) {
        currentStroke = null;
    }

    
    
}
