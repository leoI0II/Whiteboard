package org.app.jfx;

import org.Controller.ContextSetting;
import org.Controller.Tools;
import org.Controller.ToolsController;
import org.Controller.ToolsControllerBuilder;
import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.Eraser;
import org.view.jfx.BoardRenderer;
import org.view.jfx.ToolBarBuilder;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;

public class WhiteboardSession {

    private final Canvas mainCanvas;
    private final Canvas cursorCanvas;
    private final DrewPool drewPool;
    private final Viewport viewport;
    private final Eraser eraser;
    private final ToolsController toolsController;
    private final BoardRenderer renderer;
    private Runnable cursorRendererRunnable = () -> {};

    private static final Runnable NO_CURSOR_RENDERER = () -> {};

    public WhiteboardSession(Canvas mainCanvas, Canvas cursorCanvas) {
        this.mainCanvas = mainCanvas;
        this.cursorCanvas = cursorCanvas;

        ContextSetting contextSetting = new ContextSetting();
        this.viewport = new Viewport(0, 0, 1.0);
        this.drewPool = new DrewPool();
        this.eraser = new Eraser(contextSetting.getEraserContext());
        this.toolsController = ToolsControllerBuilder.buildStandardToolset(drewPool, eraser, viewport, contextSetting);

        this.renderer = new BoardRenderer(null);
        this.renderer.setViewport(viewport);
        this.renderer.setContextSetting(contextSetting);
    }

    public void setup(Scene scene) {
        setupObservers();
        setupMouseEvents();
        setupGestures();
        setupKeyboardShortcuts(scene);
        mainCanvas.widthProperty().addListener(obs -> redrawCanvas());
        mainCanvas.heightProperty().addListener(obs -> redrawCanvas());
    }

    public HBox buildToolBar() {
        return ToolBarBuilder.buildToolBar(toolsController, drewPool, this::redrawCanvas);
    }

    private void setupObservers() {
        toolsController.addToolChangedObserver(tool -> {
            if (tool == Tools.ERASER) {
                mainCanvas.setCursor(Cursor.NONE);
                cursorRendererRunnable = () -> renderer.render(eraser);
            }
            else if (tool == Tools.HAND) {
                mainCanvas.setCursor(Cursor.OPEN_HAND);
                cursorRendererRunnable = NO_CURSOR_RENDERER;
            } else {
                mainCanvas.setCursor(Cursor.DEFAULT);
                cursorRendererRunnable = NO_CURSOR_RENDERER;
            }
        });
    }

    private void setupMouseEvents() {
        mainCanvas.setOnMouseMoved(event -> {
            toolsController.handleMouseMoved(event.getX(), event.getY());
            redrawCursorCanvas();
        });
        mainCanvas.setOnMousePressed(event -> {
            if (toolsController.getActiveTool() == Tools.HAND) {
                mainCanvas.setCursor(Cursor.CLOSED_HAND);
            }

            toolsController.handleMousePressed(event.getX(), event.getY());
            redrawCanvas();
        });
        mainCanvas.setOnMouseDragged(event -> {
            if (toolsController.getActiveTool() == Tools.HAND) {
                mainCanvas.setCursor(Cursor.CLOSED_HAND);
            }
            toolsController.handleMouseDragged(event.getX(), event.getY());
            redrawCanvas();
            redrawCursorCanvas();
        });
        mainCanvas.setOnMouseReleased(event -> {
            if (toolsController.getActiveTool() == Tools.HAND) {
                mainCanvas.setCursor(Cursor.OPEN_HAND);
            }

            toolsController.handleMouseReleased(event.getX(), event.getY());
            redrawCanvas();
        });
    }

    private void setupGestures() {
        mainCanvas.setOnZoom(event -> {
            viewport.setZoom(viewport.getZoom() * event.getZoomFactor());
            redrawCanvas();
        });

        mainCanvas.setOnScroll(event -> {
            if (event.isControlDown() || event.isShortcutDown()) {
                double oldZoom = viewport.getZoom();
                double newZoom = oldZoom * (1.0 + event.getDeltaY() * 0.005);
                double mouseX = event.getX();
                double mouseY = event.getY();
                double worldX = (mouseX / oldZoom) + viewport.getOffsetX();
                double worldY = (mouseY / oldZoom) + viewport.getOffsetY();
                viewport.setZoom(newZoom);
                viewport.setOffset(worldX - mouseX / newZoom, worldY - mouseY / newZoom);
            } else {
                viewport.setOffset(
                    viewport.getOffsetX() - event.getDeltaX() / viewport.getZoom(),
                    viewport.getOffsetY() - event.getDeltaY() / viewport.getZoom()
                );
            }
            redrawCanvas();
            event.consume();
        });
    }

    private void setupKeyboardShortcuts(Scene scene) {
        scene.getAccelerators().put(
            new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN),
            () -> { drewPool.undo(); redrawCanvas(); }
        );
        scene.getAccelerators().put(
            new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN),
            () -> { drewPool.redo(); redrawCanvas(); }
        );
    }

    private void redrawCanvas() {
        var gc = mainCanvas.getGraphicsContext2D();
        renderer.setGraphicsContext(gc);
        gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        renderer.render(drewPool);
    }

    private void redrawCursorCanvas() {
        var gc = cursorCanvas.getGraphicsContext2D();
        renderer.setGraphicsContext(gc);
        gc.clearRect(0, 0, cursorCanvas.getWidth(), cursorCanvas.getHeight());
        cursorRendererRunnable.run();
    }
}
