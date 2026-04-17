package org.model.interfaces;

public interface HistoryObserver {
    void onHistoryChanged(boolean canUndo, boolean canRedo);
}
