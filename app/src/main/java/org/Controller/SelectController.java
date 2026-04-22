package org.Controller;

import java.util.ArrayList;
import java.util.List;

import org.model.DrewPool;
import org.model.Viewport;
import org.model.interfaces.Controller;
import org.model.interfaces.Selectable;
import org.model.utils.RectangleBBox;

public class SelectController extends Controller {

    private DrewPool itemPool;
    private Viewport viewport;
    private List<Selectable> selectedItems = new ArrayList<>();
    private List<Selectable> contextItems = new ArrayList<>();
    double selectStartX, selectStartY;
    double lastMouseX, lastMouseY;
    private RectangleBBox selectionBox;

    public SelectController(final DrewPool itemPool, final Viewport viewport) {
        this.itemPool = itemPool;
        this.viewport = viewport;
    }

    private void updateSelectionBox() {
        double x1 = Math.min(selectStartX, lastMouseX);
        double y1 = Math.min(selectStartY, lastMouseY);
        double x2 = Math.max(selectStartX, lastMouseX);
        double y2 = Math.max(selectStartY, lastMouseY);
        selectionBox.set(x1, y1, x2 - x1, y2 - y1);
    }

    public RectangleBBox getSelectionBox() {
        return selectionBox;
    }
    
    @Override
    protected void onMousePressed(double x, double y) {
        selectStartX = viewport.screenToWorldX(x);
        selectStartY = viewport.screenToWorldY(y);
        contextItems.clear();
        selectedItems.clear();
        selectionBox = new RectangleBBox();
        for (var item : itemPool.getDrewObjects()) {
            if (item instanceof Selectable) {
                contextItems.add((Selectable)item);
            }
        }
        onMouseDragged(x, y);
    }

    private boolean checkIntersection(Selectable item) {
        return item.intersects(selectionBox);
    }

    @Override
    protected void onMouseDragged(double x, double y) {
        lastMouseX = viewport.screenToWorldX(x);
        lastMouseY = viewport.screenToWorldY(y);

        updateSelectionBox();

        for (var item : contextItems) {
            if (checkIntersection(item)) {
                if (!selectedItems.contains(item)) selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }
        }
    }

    @Override
    protected void onMouseReleased(double x, double y) {
        // for (var item : contextItems) {
        //     if (selectionBox.intersects(item.getBoundingBox())) {
        //         if (!selectedItems.contains(item)) {
        //             selectedItems.add(item);
        //         }
        //     }
        // }
        
        selectionBox = null;
    }

    public List<Selectable> getSelectedItems() {
        return selectedItems;
    }

    public void clearSelection() {
        selectedItems.clear();
    }

    @Override
    protected void onMouseMoved(double x, double y) {
         
    }
    
}
