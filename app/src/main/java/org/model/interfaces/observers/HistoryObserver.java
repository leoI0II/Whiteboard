package org.model.interfaces.observers;

public interface HistoryObserver {
    void onHistoryChanged(boolean canUndo, boolean canRedo);
}
