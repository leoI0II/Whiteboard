package org.model.interfaces;

public abstract class Controller {
    
    private boolean isPressed;

    public void handleMousePressed() {
        isPressed = true;
        onMousePressed();
    }

    public void handleMouseDragged() {
        if (isPressed)
            onMouseDragged();
    }

    public void handleMouseReleased() {
        isPressed = false;
        onMouseReleased();
    }

    public boolean isPressed() {
        return isPressed;
    }

    protected abstract void onMousePressed();
    protected abstract void onMouseDragged();
    protected abstract void onMouseReleased();

}
