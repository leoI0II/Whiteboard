package org.Controller;

import java.util.Objects;

import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.Stroke;
import org.model.base.context.BrushContext;
import org.model.interfaces.Controller;

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
     * Sets the brush context for this PenController, which determines the thickness and color of the strokes drawn.
     * @param context The BrushContext to be used for drawing. Must not be null.
     */
    public void setContext(final BrushContext context) {
        this.context = Objects.requireNonNull(context);
    }

    /**
     * Sets the viewport for this PenController, which is used to convert screen coordinates to world coordinates.
     * @param viewport The Viewport to be used for coordinate transformations. Must not be null.
     */
    public void setViewport(final Viewport viewport) {
        this.viewport = Objects.requireNonNull(viewport);
    }

    /**
     * Sets the stroke pool for this PenController, which is where new strokes are added when drawn.
     * @param strokePool The DrewPool to which new strokes will be added. Must not be null.
     */
    public void setStrokePool(final DrewPool strokePool) {
        this.strokePool = Objects.requireNonNull(strokePool);
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
        // 1. Crea una copia del contesto attuale per il nuovo tratto.
        BrushContext strokeContext = new BrushContext(context);

        // 2. Applica la trasformazione dello spessore solo sulla copia.
        double adjustedThickness = strokeContext.getThickness() / viewport.getZoom();
        strokeContext.setThickness(adjustedThickness);

        // 3. Passa la copia indipendente al costruttore dello Stroke.
        currentStroke = new Stroke(strokeContext);
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
            currentStroke.addPoint(viewport.screenToWorld(x, y));
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
