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
    double selectStartX, selectStartY;

    public SelectController(final DrewPool itemPool, final Viewport viewport) {
        this.itemPool = itemPool;
        this.viewport = viewport;
    }
    
    @Override
    protected void onMousePressed(double x, double y) {
        selectStartX = x;
        selectStartY = y;
        selectedItems.clear();
        for (var item : itemPool.getDrewObjects()) {
            if (item instanceof Selectable) {
                selectedItems.add(item);
            }
        }
        onMouseDragged(x, y);
    }

    @Override
    protected void onMouseDragged(double x, double y) {

    }

    @Override
    protected void onMouseReleased(double x, double y) {

    }
    
}
