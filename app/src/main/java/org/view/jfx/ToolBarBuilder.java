package org.view.jfx;

import java.util.HashMap;

import org.Controller.Tools;
import org.Controller.ToolsController;
import org.model.DrewPool;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class ToolBarBuilder {
    
    public static HBox buildToolBar(ToolsController mainController, DrewPool pool, Runnable requestRedraw) {
        HashMap<ToggleButton, Tools> toggleToolMap = new HashMap<>();

        ToggleButton penButton = new ToggleButton("Pen");
        toggleToolMap.put(penButton, Tools.PEN);
        ToggleButton eraserButton = new ToggleButton("Eraser");
        toggleToolMap.put(eraserButton, Tools.ERASER);
        ToggleButton selectButton = new ToggleButton("Select");
        toggleToolMap.put(selectButton, Tools.SELECTION);
        
        Button undoButton = new Button("Undo");
        undoButton.setDisable(true);
        undoButton.setOnAction(e -> {
            System.out.println("Undo button clicked!");
            pool.undo();
            requestRedraw.run();
        });
        Button redoButton = new Button("Redo");
        redoButton.setDisable(true);
        redoButton.setOnAction(e -> {
            System.out.println("Redo button clicked!");
            pool.redo();
            requestRedraw.run();
        });
        pool.addObserver((canUndo, canRedo) -> {
            undoButton.setDisable(!canUndo);
            redoButton.setDisable(!canRedo);
        });
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            System.out.println("Clear button clicked!");
            pool.clear();
            requestRedraw.run();
        });

        ToggleGroup toolToggleGroup = new ToggleGroup();
        penButton.setToggleGroup(toolToggleGroup);
        eraserButton.setToggleGroup(toolToggleGroup);
        selectButton.setToggleGroup(toolToggleGroup);
        penButton.setSelected(true); // Выбираем перо по умолчанию
        HBox toolBar = new HBox(10);
        toolBar.setPadding(new Insets(10));
        toolBar.getChildren()
            .addAll(penButton,
                    eraserButton,
                    selectButton,
                    new Separator(),
                    undoButton,
                    redoButton,
                    clearButton
            );
        toolToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                Tools selectedTool = toggleToolMap.get(newToggle);
                mainController.setActiveTool(selectedTool);
                System.out.println("Selected tool: " + selectedTool);
            }
        });
        return toolBar;
    }
}
