package org.app;

import org.Controller.PenController;
import org.model.DrewPool;
import org.model.Viewport;
import org.model.base.context.BrushContext;
import org.view.JfxBoardRenderer;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends javafx.application.Application {

    private JfxBoardRenderer renderer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Viewport viewport = new Viewport(0, 0, 1.0);
        DrewPool drewPool = new DrewPool();
        PenController penController = new PenController(drewPool, viewport);
        BrushContext brushContext = new BrushContext();
        renderer = new JfxBoardRenderer(null); // GraphicsContext will be set later
        penController.setContext(brushContext);
        penController.setViewport(viewport);

        Canvas canvas = new Canvas(800, 600);
        Pane rootPane = new Pane(canvas);
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

        Scene scene = new Scene(rootPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Whiteboard App");
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
