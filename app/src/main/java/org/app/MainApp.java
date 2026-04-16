package org.app;

import org.Controller.ToolsControllerBuilder;
import org.model.DrewPool;
import org.model.Viewport;
import org.view.JfxBoardRenderer;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCodeCombination;

public class MainApp extends javafx.application.Application {

    private JfxBoardRenderer renderer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Viewport viewport = new Viewport(0, 0, 1.0);
        DrewPool drewPool = new DrewPool();

        var mainToolsetController = ToolsControllerBuilder.buildStandardToolset(drewPool, viewport);

        renderer = new JfxBoardRenderer(null); // GraphicsContext will be set later
        renderer.setViewport(viewport);
        
        Canvas canvas = new Canvas(800, 600);
        Pane rootPane = new Pane(canvas);
        // Привязываем ширину и высоту холста к размерам окна
        canvas.widthProperty().bind(rootPane.widthProperty());
        canvas.heightProperty().bind(rootPane.heightProperty());

        // Окно растянулось -> холст растянулся -> надо перерисовать картинку, чтобы она не стерлась!
        canvas.widthProperty().addListener(observable -> redrawCanvas(canvas, drewPool, viewport));
        canvas.heightProperty().addListener(observable -> redrawCanvas(canvas, drewPool, viewport));

        canvas.setOnMousePressed(event -> {
            mainToolsetController.handleMousePressed(event.getX(), event.getY());
            redrawCanvas(canvas, drewPool, viewport);
        });

        canvas.setOnMouseDragged(event -> {
            mainToolsetController.handleMouseDragged(event.getX(), event.getY());
            redrawCanvas(canvas, drewPool, viewport);
        });

        canvas.setOnMouseReleased(event -> {
            mainToolsetController.handleMouseReleased(event.getX(), event.getY());
            redrawCanvas(canvas, drewPool, viewport);
        });

        canvas.setOnZoom(event-> {
            System.out.println("Zoom event, factor " + event.getZoomFactor());
            viewport.setZoom(viewport.getZoom() * event.getZoomFactor());
            redrawCanvas(canvas, drewPool, viewport);
        });

        canvas.setOnScroll(event -> {
            if (event.isControlDown() || event.isShortcutDown()) {
                // --- MODO ZOOM (Ctrl + Scorrimento a due dita) ---
                
                // Calcoliamo la variazione di zoom in base alla velocità/lunghezza dello scroll
                double zoomChange = event.getDeltaY() * 0.005; 
                double zoomFactor = 1.0 + zoomChange;
                
                double oldZoom = viewport.getZoom();
                double newZoom = oldZoom * zoomFactor;

                // Limiti di sicurezza (evita zoom infiniti o negativi)
                // if (newZoom < 0.1) newZoom = 0.1;
                // if (newZoom > 10.0) newZoom = 10.0;

                double mouseX = event.getX();
                double mouseY = event.getY();

                // 1. Troviamo le coordinate del "Mondo" sotto il cursore ORA
                double worldX = (mouseX / oldZoom) + viewport.getOffsetX();
                double worldY = (mouseY / oldZoom) + viewport.getOffsetY();

                // 2. Applichiamo il nuovo zoom
                viewport.setZoom(newZoom);

                // 3. Ricalcoliamo l'offset per mantenere il mondo incollato al cursore
                double newOffsetX = worldX - (mouseX / newZoom);
                double newOffsetY = worldY - (mouseY / newZoom);

                viewport.setOffset(newOffsetX, newOffsetY);
                System.out.println("Zoom al cursore! Nuovo zoom: " + newZoom);

            } else {
                // --- MODO SPOSTAMENTO (Scorrimento a due dita normale) ---
                
                double newX = viewport.getOffsetX() - event.getDeltaX();
                double newY = viewport.getOffsetY() - event.getDeltaY();
                viewport.setOffset(newX, newY);
            }
            
            redrawCanvas(canvas, drewPool, viewport);
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
