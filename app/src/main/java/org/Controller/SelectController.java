package org.Controller;

import java.util.ArrayList;
import java.util.List;

import org.model.DrewPool;
import org.model.Viewport;
import org.model.interfaces.Controller;
import org.model.interfaces.Drawable;
import org.model.interfaces.Selectable;

public class SelectController extends Controller {

    private DrewPool itemPool;
    private Viewport viewport;
    private List<Drawable> selectedItems = new ArrayList<>();
    private List<Drawable> contextItems = new ArrayList<>();
    double selectStartX, selectStartY;
    double lastMouseX, lastMouseY;

    public SelectController(final DrewPool itemPool, final Viewport viewport) {
        this.itemPool = itemPool;
        this.viewport = viewport;
    }
    
    @Override
    protected void onMousePressed(double x, double y) {
        selectStartX = viewport.screenToWorldX(x);
        selectStartY = viewport.screenToWorldY(y);
        contextItems.clear();
        selectedItems.clear();
        for (var item : itemPool.getDrewObjects()) {
            if (item instanceof Selectable) {
                contextItems.add(item);
            }
        }
        onMouseDragged(x, y);
    }

    @Override
    protected void onMouseDragged(double x, double y) {
        lastMouseX = viewport.screenToWorldX(x);
        lastMouseY = viewport.screenToWorldY(y);

        for (var item : contextItems) {
            if (item.getBoundingBox().contains(lastMouseX, lastMouseY)) {
                if (!selectedItems.contains(item)) {
                    selectedItems.add(item);
                }
            }
        }
    }

    @Override
    protected void onMouseReleased(double x, double y) {

    }

    public List<Drawable> getSelectedItems() {
        return selectedItems;
    }

    public void clearSelection() {
        selectedItems.clear();
    }

    @Override
    protected void onMouseMoved(double x, double y) {
         
    }
    
}
