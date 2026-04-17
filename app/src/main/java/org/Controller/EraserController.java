package org.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.context.EraserContext;
import org.model.interfaces.Controller;
import org.model.interfaces.Drawable;
import org.model.interfaces.Erasable;

public class EraserController extends Controller {

    private EraserContext context;
    private DrewPool itemPool;
    private List<Drawable> contextItems;
    private Viewport viewport;

    public EraserController(final EraserContext context, final DrewPool itemPool, final Viewport viewport) {
        this.context = Objects.requireNonNull(context);
        this.itemPool = Objects.requireNonNull(itemPool);
        this.contextItems = new ArrayList<>();
        this.viewport = viewport;
    }

    public void setContext(final EraserContext context) {
        this.context = Objects.requireNonNull(context);
    }

    public void setItemPool(final DrewPool itemPool) {
        this.itemPool = Objects.requireNonNull(itemPool);
    }

    public void setViewport(final Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    protected void onMousePressed(double x, double y) {
        contextItems.clear();
        for (var item : itemPool.getDrewObjects()) {
            if (item instanceof Erasable) {
                contextItems.add(item);
            }
        }
        onMouseDragged(x, y);
    }

    @Override
    protected void onMouseDragged(double x, double y) {
        if (!isPressed()) return;
        
        double worldX = viewport.screenToWorldX(x);
        double worldY = viewport.screenToWorldY(y);
        var eraserBox = context.getBoundingBox(worldX, worldY);

        List<Drawable> itemsToRemove = new ArrayList<>();
        for (var item : contextItems) {
            Erasable erasableItem = (Erasable) item;
            if (eraserBox.intersects(item.getBoundingBox())) {
                if (checkExactCollision(erasableItem, worldX, worldY, context.getRadius())) {
                    itemsToRemove.add(item);
                }
            }
        }

        for (var item : itemsToRemove) {
            itemPool.removeObject(item);
            contextItems.remove(item);
        }
    }

    private boolean checkExactCollision(final Erasable item, final double worldX, final double worldY, final double radius) {
        return item.intersectsEraser(worldX, worldY, radius);
    }

    @Override
    protected void onMouseReleased(double x, double y) {
        contextItems.clear();
    }

    
}
