package org.Controller;

public enum Tools {
    PEN, 
    ERASER,
    SHAPE,
    SELECTION,
    TEXT,
    COLOR_PICKER,
    ZOOM,
    HAND;

    public static Tools DEFAULT_TOOL = PEN;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
