/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MazeRunner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set the window title
        primaryStage.setTitle("Maze Runner");

        // Create the root of the window as a border pane
        BorderPane root = new BorderPane();

        // Make the maze and maze solver
        Maze maze = new Maze(30, 30);
        maze.generate();

        MazeSolver mazeSolver = new MazeSolver(maze);

        // Create the canvas for the maze
        MazeCanvas canvas = new MazeCanvas(maze, mazeSolver);

        // Create a pane to wrap the canvas
        Pane wrapperPane = new Pane();
        root.setCenter(wrapperPane);

        // Add the canvas to the wrapper pane and
        // then bind the canvas dimensions to the wrapper
        wrapperPane.getChildren().add(canvas);
        canvas.widthProperty().bind(wrapperPane.widthProperty());
        canvas.heightProperty().bind(wrapperPane.heightProperty());

        /* Create W and H Selectors */

        Spinner<Integer> widthSpinner = new Spinner<>(1, Integer.MAX_VALUE, maze.getWidth());
        widthSpinner.setEditable(true);
        Spinner<Integer> heightSpinner = new Spinner<>(1, Integer.MAX_VALUE, maze.getHeight());
        heightSpinner.setEditable(true);

        /* Button Creation */

        // When someone clicks the generation button, if the dimensions should be changed, change them;
        // generate a new maze; clear the solver, as we have a new maze; and then redraw the canvas
        Button generateButton = new Button("Generate");
        generateButton.setOnAction(event -> {
            if (maze.getWidth() != widthSpinner.getValue() || maze.getHeight() != heightSpinner.getValue()) {
                maze.changeDimensions(widthSpinner.getValue(), heightSpinner.getValue());
            }
            maze.generate();
            mazeSolver.clear();
            canvas.draw();
        });

        // When someone clicks the clear button, if the dimensions should be changed, change them;
        // clear the maze; clear the solver, as we have no maze; and then redraw the canvas
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(event -> {
            if (maze.getWidth() != widthSpinner.getValue() || maze.getHeight() != heightSpinner.getValue()) {
                maze.changeDimensions(widthSpinner.getValue(), heightSpinner.getValue());
            }
            maze.clear();
            mazeSolver.clear();
            canvas.draw();
        });

        // When someone clicks the solve button, solve the maze and then redraw the canvas
        Button solveButton = new Button("Solve");
        solveButton.setOnAction(event -> {
            mazeSolver.solve();
            canvas.draw();
        });

        /* Control Toolbar */

        // Create a bottom toolbar with controls
        ToolBar toolBar = new ToolBar(
                widthSpinner,
                heightSpinner,
                generateButton,
                clearButton,
                new Separator(),
                solveButton
        );

        // Add the toolbar to the root
        root.setBottom(toolBar);

        // Create the stage and scene and display the window
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }
}
