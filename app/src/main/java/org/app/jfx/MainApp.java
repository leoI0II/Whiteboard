package org.app.jfx;

import org.Controller.ToolsController;
import org.Controller.ToolsControllerBuilder;
import org.model.DrewPool;
import org.model.Viewport;
import org.view.jfx.BoardRenderer;
import org.view.jfx.ToolBarBuilder;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCodeCombination;

public class MainApp extends javafx.application.Application {

    private BoardRenderer renderer;
    private Canvas canvas;
    private Viewport viewport;
    private DrewPool drewPool;
    private ToolsController mainToolsetController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.viewport = new Viewport(0, 0, 1.0);
        this.drewPool = new DrewPool();

        this.mainToolsetController = ToolsControllerBuilder.buildStandardToolset(drewPool, viewport);

        this.renderer = new BoardRenderer(null); // GraphicsContext will be set later
        renderer.setViewport(viewport);
        
        this.canvas = new Canvas(800, 600);
        Pane rootPane = new Pane(canvas);
        bindCanvasSize(rootPane);
        setupMouseEvents();
        setupGestures();
        
        HBox toolBar = ToolBarBuilder.buildToolBar(mainToolsetController, drewPool, this::redrawCanvas);
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(rootPane);
        mainLayout.setTop(toolBar);
        Scene scene = new Scene(mainLayout, 800, 600);

        setupKeyboardShortcuts(scene);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Whiteboard App");
        primaryStage.show();
    }

        private void bindCanvasSize(Pane rootPane) {
        canvas.widthProperty().bind(rootPane.widthProperty());
        canvas.heightProperty().bind(rootPane.heightProperty());
        canvas.widthProperty().addListener(observable -> redrawCanvas());
        canvas.heightProperty().addListener(observable -> redrawCanvas());
    }

    private void setupMouseEvents() {
        canvas.setOnMousePressed(event -> {
            mainToolsetController.handleMousePressed(event.getX(), event.getY());
            redrawCanvas();
        });

        canvas.setOnMouseDragged(event -> {
            mainToolsetController.handleMouseDragged(event.getX(), event.getY());
            redrawCanvas();
        });

        canvas.setOnMouseReleased(event -> {
            mainToolsetController.handleMouseReleased(event.getX(), event.getY());
            redrawCanvas();
        });
    }

    private void setupGestures() {
        canvas.setOnZoom(event-> {
            System.out.println("Zoom event, factor " + event.getZoomFactor());
            viewport.setZoom(viewport.getZoom() * event.getZoomFactor());
            redrawCanvas();
        });

        canvas.setOnScroll(event -> {
            if (event.isControlDown() || event.isShortcutDown()) {
                // --- MODO ZOOM (Ctrl + Scorrimento a due dita) ---
                
                // Calcoliamo la variazione di zoom in base alla velocità/lunghezza dello scroll
                double zoomChange = event.getDeltaY() * 0.005; 
                double zoomFactor = 1.0 + zoomChange;
                
                double oldZoom = viewport.getZoom();
                double newZoom = oldZoom * zoomFactor;
                
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
                
                // Переводим пиксели экрана в координаты мира с учетом текущего масштаба
                double deltaXWorld = event.getDeltaX() / viewport.getZoom();
                double deltaYWorld = event.getDeltaY() / viewport.getZoom();
                
                // Вычитаем уже "мировую" дельту
                double newX = viewport.getOffsetX() - deltaXWorld;
                double newY = viewport.getOffsetY() - deltaYWorld;
                viewport.setOffset(newX, newY);
            }
            
            redrawCanvas();
            event.consume();
        });
    }

    private void setupKeyboardShortcuts(Scene scene) {
        KeyCombination undoKey = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
        KeyCombination redoKey = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);

        scene.getAccelerators().put(undoKey, () -> {
            drewPool.undo();
            redrawCanvas();
        }); 

        scene.getAccelerators().put(redoKey, () -> {
            drewPool.redo();
            redrawCanvas();
        });
    }

    private void redrawCanvas() {
        var gc = canvas.getGraphicsContext2D();
        renderer.setGraphicsContext(gc);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        renderer.render(drewPool);
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}
