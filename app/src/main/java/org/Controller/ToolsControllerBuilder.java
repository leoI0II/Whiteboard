package org.Controller;

import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.context.BrushContext;
import org.model.base.context.EraserContext;

public class ToolsControllerBuilder {
    
    public static ToolsController buildStandardToolset(DrewPool drewPool, Viewport viewport) {
        ToolsController toolsController = new ToolsController();
        PenController penController = new PenController(drewPool, viewport);;
        penController.setContext( new BrushContext());
        toolsController.addController(Tools.PEN, penController);
        toolsController.addController(Tools.ERASER, new EraserController(new EraserContext(), drewPool, viewport));
        toolsController.setActiveTool(Tools.DEFAULT_TOOL);
        return toolsController;
    }
}
