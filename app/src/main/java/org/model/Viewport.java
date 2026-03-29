package org.model;

public class Viewport {
    
    private long offsetX;
    private long offsetY;
    private double zoom;

    public Viewport(long offsetX, long offsetY, double zoom) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.zoom = zoom;
    }

    public long getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(long offsetX) {
        this.offsetX = offsetX;
    }

    public long getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(long offsetY) {
        this.offsetY = offsetY;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }



}
