package org.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.model.interfaces.Controller;
import org.model.interfaces.observers.ToolChangedObserver;

public class ToolsController extends Controller {
    
    private HashMap<Tools, Controller> controllers;
    private Controller activeController;
    private List<ToolChangedObserver> toolChangedObservers = new ArrayList<>();
    private Tools activeTool;

    public ToolsController() {
        controllers = new HashMap<>();
        activeController = null;
    }

    public void addController(Tools tool, Controller controller) {
        controllers.put(tool, controller);
    }

    public void setActiveTool(Tools tool) {
        activeController = controllers.get(tool);
        activeTool = tool;

        notifyToolChangedObservers(tool);
    }

    public Controller getActiveController() {
        return activeController;
    }

    public Tools getActiveTool() {
        return activeTool;
    }

    public Controller getController(Tools tool) {
        return controllers.get(tool);
    }

    public void addToolChangedObserver(ToolChangedObserver observer) {
        toolChangedObservers.add(observer);
    }

    private void notifyToolChangedObservers(Tools newTool) {
        for (var observer : toolChangedObservers) {
            observer.onToolChanged(newTool);
        }
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
