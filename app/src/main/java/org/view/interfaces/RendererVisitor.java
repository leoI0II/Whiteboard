package org.view.interfaces;

import org.model.base.Stroke;

/**
 * The RendererVisitor interface defines a visitor pattern for rendering different drawable objects.
 * It contains a visit method for each type of drawable object that can be rendered.
 */
public interface RendererVisitor {
    /**
     * Visits a Stroke object and renders it on the canvas.
     * @param stroke The Stroke object to be rendered.
     */
    public void visit(Stroke stroke);

    // public void visit(Text text);        // future implementation for rendering text
}
