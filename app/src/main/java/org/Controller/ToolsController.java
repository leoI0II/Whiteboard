package org.Controller;

import java.util.HashMap;

import org.model.interfaces.Controller;

public class ToolsController extends Controller {
    
    private HashMap<Tools, Controller> controllers;
    private Controller activeController;

    public ToolsController() {
        controllers = new HashMap<>();
        activeController = null;
    }

    public void addController(Tools tool, Controller controller) {
        controllers.put(tool, controller);
    }

    public void setActiveTool(Tools tool) {
        activeController = controllers.get(tool);
    }

    public Controller getActiveController() {
        return activeController;
    }

    public Controller getController(Tools tool) {
        return controllers.get(tool);
    }

    @Override
    protected void onMousePressed(double x, double y) {
        if (activeController != null) {
            activeController.handleMousePressed(x, y);
        } else {
            throw new UnsupportedOperationException("No active tool selected");
        }
    }

    @Override
    protected void onMouseDragged(double x, double y) {
        if (activeController != null) {
            activeController.handleMouseDragged(x, y);
        } else {
            throw new UnsupportedOperationException("No active tool selected");
        }
    }

    @Override
    protected void onMouseReleased(double x, double y) {
        if (activeController != null) {
            activeController.handleMouseReleased(x, y);
        } else {
            throw new UnsupportedOperationException("No active tool selected");
        }
    }

}
