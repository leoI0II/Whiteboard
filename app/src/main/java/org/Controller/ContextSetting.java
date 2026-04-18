package org.Controller;

import org.model.base.context.BrushContext;
import org.model.base.context.EraserContext;

public class ContextSetting {
    private BrushContext brushContext = new BrushContext();
    private EraserContext eraserContext = new EraserContext();

    public BrushContext getBrushContext() {
        return brushContext;
    }

    public EraserContext getEraserContext() {
        return eraserContext;
    }
}
