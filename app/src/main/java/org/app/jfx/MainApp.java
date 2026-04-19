package org.app.jfx;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas mainCanvas = new Canvas(800, 600);
        Canvas cursorCanvas = new Canvas(800, 600);
        cursorCanvas.setMouseTransparent(true);
        Pane rootPane = new Pane(mainCanvas, cursorCanvas);
        mainCanvas.widthProperty().bind(rootPane.widthProperty());
        mainCanvas.heightProperty().bind(rootPane.heightProperty());
        cursorCanvas.widthProperty().bind(rootPane.widthProperty());
        cursorCanvas.heightProperty().bind(rootPane.heightProperty());

        WhiteboardSession session = new WhiteboardSession(mainCanvas, cursorCanvas);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(rootPane);
        mainLayout.setTop(session.buildToolBar());
        Scene scene = new Scene(mainLayout, 800, 600);
        session.setup(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Whiteboard App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
