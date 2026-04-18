package org.Controller;

import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.Eraser;

public class ToolsControllerBuilder {
    
    public static ToolsController buildStandardToolset(DrewPool drewPool, Eraser eraser, Viewport viewport, ContextSetting contextSetting) {
        ToolsController toolsController = new ToolsController();
        PenController penController = new PenController(drewPool, viewport);
        penController.setContext(contextSetting.getBrushContext());
        EraserController eraserController = new EraserController(eraser, drewPool, viewport);
        PanController panController = new PanController(viewport);
        SelectController selectController = new SelectController(drewPool, viewport);
        toolsController.addController(Tools.PEN, penController);
        toolsController.addController(Tools.ERASER, eraserController);
        toolsController.addController(Tools.HAND, panController);
        toolsController.addController(Tools.SELECTION, selectController);
        toolsController.setActiveTool(Tools.DEFAULT_TOOL);

        toolsController.addToolChangedObserver((tool) -> {
            System.out.println("Active tool changed to: " + tool);

            if (toolsController.getActiveController() != selectController) {
                selectController.clearSelection();
            }
        });

        return toolsController;
    }
}
