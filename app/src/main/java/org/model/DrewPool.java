package org.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.model.interfaces.Drawable;
import org.model.interfaces.observers.HistoryObserver;

/**
 * The DrewPool class manages a collection of drawable objects.
 * It provides functionality to add, remove, and clear objects, as well as undo and redo capabilities
 * for managing the history of drawn objects.
 */
public class DrewPool {
    private final Stack<Drawable> drewObjects;
    private final Stack<Drawable> temporaryRemovedDrawables;
    private final List<HistoryObserver> historyObservers = new ArrayList<>();

    /**
     * Constructs a DrewPool with a pre-existing stack of drawable objects.
     *
     * @param objs The initial stack of drawable objects.
     */
    public DrewPool(Stack<Drawable> objs) {
        this.drewObjects = objs;
        this.temporaryRemovedDrawables = new Stack<>();
    }

    /**
     * Constructs an empty DrewPool.
     */
    public DrewPool() {
        this(new Stack<>());
    }

    public void addObserver(HistoryObserver observer) {
        // Implementation for adding an observer to listen for history changes
        historyObservers.add(observer);
    }

    private void notifyHistoryObservers() {
        boolean canUndo = !drewObjects.isEmpty();
        boolean canRedo = !temporaryRemovedDrawables.isEmpty();
        for (var observer : historyObservers) {
            observer.onHistoryChanged(canUndo, canRedo);
        }
    }

    /**
     * Retrieves the list of currently drawn objects.
     *
     * @return A list of drawable objects.
     */
    public List<Drawable> getDrewObjects() {
        return drewObjects;
    }

    /**
     * Retrieves the list of temporarily removed drawable objects, which can be used for redo operations.
     *
     * @return A list of temporarily removed drawable objects.
     */
    public List<Drawable> getTemporaryRemovedDrawables() {
        return temporaryRemovedDrawables;
    }

    /**
     * Adds a new drawable object to the pool.
     *
     * @param obj The drawable object to add.
     */
    public void addObject(Drawable obj) {
        this.drewObjects.push(obj);
        temporaryRemovedDrawables.clear();
        notifyHistoryObservers();
    }

    /**
     * Clears all drawable objects from the pool.
     */
    public void clear() {
        temporaryRemovedDrawables.addAll(drewObjects);
        this.drewObjects.clear();
        notifyHistoryObservers();
    }

    /**
     * Removes a specific drawable object from the pool.
     *
     * @param obj The drawable object to remove.
     */
    public void removeObject(final Drawable obj) {
        temporaryRemovedDrawables.push(obj);
        drewObjects.remove(obj);
        notifyHistoryObservers();
    }

    /**
     * Undoes the last drawing action by moving the most recent object to a temporary removal stack.
     */
    public void undo() {
        if (!drewObjects.isEmpty()) {
            final Drawable lastStroke = drewObjects.pop();
            temporaryRemovedDrawables.push(lastStroke);
            notifyHistoryObservers();
        }
    }

    /**
     * Redoes the last undone action by restoring the most recently removed object from the temporary stack.
     */
    public void redo() {
        if (!temporaryRemovedDrawables.isEmpty()) {
            final Drawable lastRemovedStroke = temporaryRemovedDrawables.pop();
            this.drewObjects.push(lastRemovedStroke);
            notifyHistoryObservers();
        }
    }

}
