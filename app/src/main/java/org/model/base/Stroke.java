package org.model.base;

import java.util.ArrayList;
import java.util.List;

import org.model.interfaces.Drawable;

public class Stroke implements Drawable {
    final private List<Point> points;
    private double thickness;
    private ARGBColor color;

    public Stroke(final double thickness, final ARGBColor color) {
        this.points = new ArrayList<>();
        this.thickness = thickness;
        this.color = color;
    }

    public Stroke(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public ARGBColor getColor() {
        return color;
    }

    public void setColor(ARGBColor color) {
        this.color = color;
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Render'");
    }

}
