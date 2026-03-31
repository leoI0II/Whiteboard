package org.model.interfaces;

/**
 * The Drawable interface defines a contract for objects that can be rendered on a canvas.
 * Any class that implements this interface must provide a 'render' method, which contains
 * the logic for drawing the object.
 */
public interface Drawable {
    
    /**
     * Renders the object on a canvas.
     * The specific implementation of this method will determine how the object is visually represented.
     */
    public void render();

}
