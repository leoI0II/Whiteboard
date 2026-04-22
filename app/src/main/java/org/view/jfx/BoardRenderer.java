package org.view.jfx;

import java.util.List;

import org.Controller.ContextSetting;
import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.Eraser;
import org.model.base.Stroke;
import org.model.interfaces.Drawable;
import org.model.interfaces.Selectable;
import org.model.utils.RectangleBBox;
import org.view.interfaces.RendererVisitor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class BoardRenderer implements RendererVisitor {

    private GraphicsContext gc;
    private Viewport viewport;
    private ContextSetting contextSetting;

    public BoardRenderer() {
        this.gc = null;
        this.viewport = null;
        this.contextSetting = null;
    }

    public BoardRenderer(GraphicsContext gc) {
        this.gc = gc;
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public void setContextSetting(ContextSetting contextSetting) {
        this.contextSetting = contextSetting;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public void render(DrewPool drewPool) {
        for (Drawable item : drewPool.getDrewObjects()) {
            item.acceptRenderer(this);
        }
    }

    public void render(Eraser eraser) {
        eraser.acceptRenderer(this);
    }

    public void renderSelectionBox(final RectangleBBox selectionBox) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        gc.setLineDashes(5);
        var x = viewport.worldToScreenX(selectionBox.getX());
        var y = viewport.worldToScreenY(selectionBox.getY());
        var w = selectionBox.getWidth() * viewport.getZoom();
        var h = selectionBox.getHeight() * viewport.getZoom();
        gc.strokeRect(x, y, w, h);
        gc.setLineDashes(0); // Сбросить стиль линий
    }

    public void renderSelectedItemHighlight(List<Selectable> selectedItems) {
        for (var item : selectedItems) {
            var bbox = item.getBoundingBox();
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(2);
            var x = viewport.worldToScreenX(bbox.getTopLeftX());
            var y = viewport.worldToScreenY(bbox.getTopLeftY());
            var w = viewport.worldToScreenX(bbox.getBottomRightX()) - x;
            var h = viewport.worldToScreenY(bbox.getBottomRightY()) - y;
            gc.strokeRect(x, y, w, h);
        }
    }

    @Override
    public void visit(Stroke stroke) {
        var points = stroke.getPoints();
        if (points.isEmpty()) return; // Защита от пустых линий

        // 1. Настройка "Кисти"
        var c = stroke.getColor();
        // Конвертируем твой ARGBColor (0-255) в JavaFX Color (где прозрачность от 0.0 до 1.0)
        gc.setStroke(Color.rgb(c.red(), c.green(), c.blue(), c.alpha() / 255.0));
        gc.setLineWidth(stroke.getThickness() * viewport.getZoom()); // Учитываем масштабирование
        
        // Делаем края линий круглыми и сглаженными (иначе при быстром рисовании будут острые углы)
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        // 2. Рисование пути (Path)
        gc.beginPath(); // Ставим кисть на бумагу
        
        // Перемещаемся в первую точку не оставляя следа
        gc.moveTo(viewport.worldToScreenX(points.get(0).x()), 
                    viewport.worldToScreenY(points.get(0).y()));
        
        // Проводим линии по всем остальным точкам
        for (int i = 1; i < points.size(); i++) {
            gc.lineTo(viewport.worldToScreenX(points.get(i).x()), 
                        viewport.worldToScreenY(points.get(i).y()));
        }
        
        // Физически заливаем краской пройденный путь
        gc.stroke();
    }
    
    @Override
    public void visit(Eraser eraser) {
        double screenX = viewport.worldToScreenX(eraser.getPointX());
        double screenY = viewport.worldToScreenY(eraser.getPointY());
        double screenRadius = eraser.getContext().getRadius() * viewport.getZoom();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeOval(screenX - screenRadius, screenY - screenRadius, 2 * screenRadius, 2 * screenRadius);
    }
}
