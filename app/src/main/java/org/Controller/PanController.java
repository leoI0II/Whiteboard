package org.Controller;

import org.model.Viewport;
import org.model.interfaces.Controller;

public class PanController extends Controller {
    
    private Viewport viewport;

    public PanController(final Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    protected void onMousePressed(double x, double y) {}

    @Override
    protected void onMouseDragged(double x, double y) {
        var deltaX = x - lastX;
        var deltaY = y - lastY;
        var offsetX = viewport.getOffsetX();
        var offsetY = viewport.getOffsetY();
        offsetX = offsetX - (deltaX / viewport.getZoom());
        offsetY = offsetY - (deltaY / viewport.getZoom());
        viewport.setOffset(offsetX, offsetY);
    }

    @Override
    protected void onMouseReleased(double x, double y) {}

    @Override
    protected void onMouseMoved(double x, double y) {
        
    }

    

}
