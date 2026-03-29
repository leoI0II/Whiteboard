package org.Controller;

import org.model.DrewPool;
import org.model.Viewport;
import org.model.interfaces.Controller;
import org.model.interfaces.Drawable;

public class PenController extends Controller {
    
    private Drawable currentStroke;
    private DrewPool strokePool;
    private Viewport viewport;

    public PenController(final DrewPool strokePool, final Viewport viewport) {
        currentStroke = null;
        this.strokePool = strokePool;
        this.viewport = viewport;
    }

    @Override
    protected void onMousePressed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMousePressed'");
    }

    @Override
    protected void onMouseDragged() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMouseDragged'");
    }

    @Override
    protected void onMouseReleased() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMouseReleased'");
    }

    
    
}
