package org.model.interfaces;

import org.model.utils.BoundingBox;

/**
 * The Drawable interface defines a contract for objects that can be rendered on a canvas.
 * Any class that implements this interface must provide a 'render' method, which contains
 * the logic for drawing the object.
 */
public interface Drawable {

    /**
     * Returns the bounding box of the drawable object, which is used for various purposes such as hit testing and erasing.
     * @return The bounding box of the drawable object.
     */
    public BoundingBox getBoundingBox();
}
