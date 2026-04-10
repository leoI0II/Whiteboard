package org.app;

import java.security.Key;

import org.Controller.PenController;
import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.context.BrushContext;
import org.view.JfxBoardRenderer;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MainApp extends javafx.application.Application {

    private JfxBoardRenderer renderer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Viewport viewport = new Viewport(0, 0, 1.0);
        DrewPool drewPool = new DrewPool();
        PenController penController = new PenController(drewPool, viewport);
        BrushContext brushContext = new BrushContext();
        renderer = new JfxBoardRenderer(null); // GraphicsContext will be set later
        renderer.setViewport(viewport);
        penController.setContext(brushContext);
        penController.setViewport(viewport);

        Canvas canvas = new Canvas(800, 600);
        Pane rootPane = new Pane(canvas);
        // Привязываем ширину и высоту холста к размерам окна
        canvas.widthProperty().bind(rootPane.widthProperty());
        canvas.heightProperty().bind(rootPane.heightProperty());

        // Окно растянулось -> холст растянулся -> надо перерисовать картинку, чтобы она не стерлась!
        canvas.widthProperty().addListener(observable -> redrawCanvas(canvas, drewPool, viewport));
        canvas.heightProperty().addListener(observable -> redrawCanvas(canvas, drewPool, viewport));

        canvas.setOnMousePressed(event -> {
            penController.handleMousePressed(event.getX(), event.getY());
            redrawCanvas(canvas, drewPool, viewport);
        });

        canvas.setOnMouseDragged(event -> {
            penController.handleMouseDragged(event.getX(), event.getY());
            redrawCanvas(canvas, drewPool, viewport);
        });

        canvas.setOnMouseReleased(event -> {
            penController.handleMouseReleased(event.getX(), event.getY());
            redrawCanvas(canvas, drewPool, viewport);
        });

        canvas.setOnScroll(event -> {
            if (event.isShortcutDown()) {
                // --- РЕЖИМ ЗУМА (Щипок тачпадом или Ctrl + Колесико) ---
                // getDeltaY() обычно возвращает положительное число при приближении и отрицательное при отдалении
                double zoomFactor = event.getDeltaY() > 0 ? 1.05 : 0.95; 
                
                double currentZoom = viewport.getZoom();
                viewport.setZoom(currentZoom * zoomFactor);
                
                System.out.println("Zoom event! Текущий зум: " + viewport.getZoom());
            } else {
                System.out.println("Scroll event! DeltaX: " + event.getDeltaX() + ", DeltaY: " + event.getDeltaY());
                // --- РЕЖИМ СМЕЩЕНИЯ (Два пальца по тачпаду или просто Колесико) ---
                // Обрати внимание: при смещении камеры мы вычитаем дельту, чтобы доска двигалась "вместе с пальцами"
                double newX = viewport.getOffsetX() - event.getDeltaX();
                double newY = viewport.getOffsetY() - event.getDeltaY();
                viewport.setOffset(newX, newY);
            }
            
            redrawCanvas(canvas, drewPool, viewport);
            
            // Поглощаем событие, чтобы оно не улетело дальше по цепочке интерфейса
            event.consume(); 
        });

        canvas.setOnZoom(event -> {
            // event.getZoomFactor() возвращает множитель (например, 1.2 для приближения)
            // Здесь ты будешь менять viewport.setZoom(...)
            var zoomFactor = event.getZoomFactor();
            viewport.setZoom(viewport.getZoom() * zoomFactor);
            System.out.println("Zoom factor: " + zoomFactor + ", New viewport zoom: " + viewport.getZoom());
            redrawCanvas(canvas, drewPool, viewport);
        });

        final KeyCombination undoKeyComb = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
        final KeyCombination redoKeyComb = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);
        
        Scene scene = new Scene(rootPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Whiteboard App");
        
        scene.getAccelerators().put(undoKeyComb, () -> {
            System.out.println("Undo triggered!");
            drewPool.undo();
            // Здесь ты должен вызвать метод undo() у своего контроллера, который управляет историей действий
            // Например: historyManager.undo();
            redrawCanvas(canvas, drewPool, viewport);
        }); 
        scene.getAccelerators().put(redoKeyComb, () -> {
            System.out.println("Redo triggered!");
            drewPool.redo();
            // Здесь ты должен вызвать метод redo() у своего контроллера, который управляет историей действий
            // Например: historyManager.redo();
            redrawCanvas(canvas, drewPool, viewport);
        });
        
        primaryStage.show();
    }

    private void redrawCanvas(Canvas canvas, DrewPool pool, Viewport viewport) {
        var gc = canvas.getGraphicsContext2D();
        renderer.setGraphicsContext(gc);
        // Очищаем экран каждый кадр
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Здесь должен быть цикл, который рисует все объекты из pool...
        // Но как именно их рисовать?
        renderer.render(pool);
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}
