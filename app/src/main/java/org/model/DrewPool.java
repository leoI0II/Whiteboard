package org.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.model.interfaces.Drawable;

public class DrewPool {
    private final Stack<Drawable> drewObjects;
    private final Stack<Drawable> temporaryRemovedDrawables;

    public DrewPool(Stack<Drawable> objs) {
        this.drewObjects = objs;
        this.temporaryRemovedDrawables = new Stack<>();
    }

    public DrewPool() {
        this(new Stack<>());
    }

    public List<Drawable> getDrewObjects() {
        return drewObjects;
    }

    public void addObject(Drawable obj) {
        this.drewObjects.add(obj);
    }

    public void clear() {
        this.drewObjects.clear();
    }

    public void removeObject(final Drawable obj) {
        drewObjects.remove(obj);
    }

    public void undo() {
        if (!drewObjects.isEmpty()) {
            final Drawable lastStroke = drewObjects.get(drewObjects.size() - 1);
            temporaryRemovedDrawables.add(lastStroke);
            removeObject(lastStroke);
        }
    }

    public void redo() {
        if (!temporaryRemovedDrawables.isEmpty()) {
            final Drawable lastRemovedStroke = temporaryRemovedDrawables.get(temporaryRemovedDrawables.size() - 1);
            addObject(lastRemovedStroke);
            temporaryRemovedDrawables.remove(lastRemovedStroke);
        }
    }

}
