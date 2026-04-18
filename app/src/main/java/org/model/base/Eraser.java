package org.model.base;

import org.model.base.context.EraserContext;
import org.model.interfaces.Drawable;
import org.model.utils.BoundingBox;
import org.view.interfaces.RendererVisitor;

public class Eraser implements Drawable {
    
    private double pointX, pointY;
    private EraserContext context;

    public Eraser(EraserContext context) {
        this.context = context;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return context.getBoundingBox(pointX, pointY);
    }

    public EraserContext getContext() {
        return context;
    }

    public double getPointX() {
        return pointX;
    }

    public double getPointY() {
        return pointY;
    }

    @Override
    public void acceptRenderer(RendererVisitor visitor) {
        // Eraser is not rendered, so this method can be left empty or throw an exception if desired.
        visitor.visit(this);
    }
    
}
