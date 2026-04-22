package org.model.interfaces;

import org.model.utils.BoundingBox;
import org.model.utils.RectangleBBox;

public interface Selectable {

    BoundingBox getBoundingBox();
    boolean intersects(RectangleBBox selectionBox);
    void move(double deltaX, double deltaY);

}
