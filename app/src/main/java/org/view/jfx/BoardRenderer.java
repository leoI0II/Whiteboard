package org.view.jfx;

import org.Controller.ContextSetting;
import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.Eraser;
import org.model.base.Stroke;
import org.model.interfaces.Drawable;
import org.view.interfaces.RendererVisitor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
        // Implementation for rendering eraser strokes
        // This could be a visual representation of the eraser's path, or it could be left empty if erasing is handled by removing strokes from the DrewPool
        // For example, you could draw a semi-transparent circle at the eraser's position to indicate where the eraser is active:

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10);
        var radius = eraser.getContext().getRadius();
        var x = eraser.getPointX();
        var y = eraser.getPointY();
        gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }
}
