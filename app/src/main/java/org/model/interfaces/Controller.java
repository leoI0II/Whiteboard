package org.model.interfaces;

/**
 * The Controller class is an abstract base class for handling user input from mouse events.
 * It provides a template for processing mouse presses, drags, and releases, allowing subclasses
 * to implement specific behaviors for these actions.
 */
public abstract class Controller {
    
    protected boolean isPressed;
    protected double lastX;
    protected double lastY;

    /**
     * Handles the initial mouse press event.
     * This method sets the pressed state to true and calls the onMousePressed method to be implemented by subclasses.
     *
     * @param x The x-coordinate of the mouse press.
     * @param y The y-coordinate of the mouse press.
     */
    public void handleMousePressed(double x, double y) {
        isPressed = true;
        lastX = x;
        lastY = y;
        onMousePressed(x, y);
    }

    /**
     * Handles the mouse drag event.
     * If the mouse is currently pressed, this method calls the onMouseDragged method.
     *
     * @param x The x-coordinate of the mouse drag.
     * @param y The y-coordinate of the mouse drag.
     */
    public void handleMouseDragged(double x, double y) {
        if (isPressed)
            onMouseDragged(x, y);
        lastX = x;
        lastY = y;
    }

    /**
     * Handles the mouse release event.
     * This method sets the pressed state to false and calls the onMouseReleased method.
     *
     * @param x The x-coordinate of the mouse release.
     * @param y The y-coordinate of the mouse release.
     */
    public void handleMouseReleased(double x, double y) {
        isPressed = false;
        onMouseReleased(x, y);
        lastX = x;
        lastY = y;
    }

    /**
     * Checks if the mouse is currently pressed.
     *
     * @return true if the mouse is pressed, false otherwise.
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Abstract method to be implemented by subclasses to handle the mouse pressed event.
     *
     * @param x The x-coordinate of the mouse press.
     * @param y The y-coordinate of the mouse press.
     */
    protected abstract void onMousePressed(double x, double y);

    /**
     * Abstract method to be implemented by subclasses to handle the mouse dragged event.
     *
     * @param x The x-coordinate of the mouse drag.
     * @param y The y-coordinate of the mouse drag.
     */
    protected abstract void onMouseDragged(double x, double y);

    /**
     * Abstract method to be implemented by subclasses to handle the mouse released event.
     *
     * @param x The x-coordinate of the mouse release.
     * @param y The y-coordinate of the mouse release.
     */
    protected abstract void onMouseReleased(double x, double y);

}
