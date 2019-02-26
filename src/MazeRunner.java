/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.LinkedList;

public class MazeRunner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Maze Runner");

        BorderPane root = new BorderPane();

        Maze maze = new Maze(30, 30);
        maze.generate();

        MazeSolver mazeSolver = new MazeSolver(maze);

        MazeCanvas canvas = new MazeCanvas(maze, mazeSolver);

        Pane wrapperPane = new Pane();
        root.setCenter(wrapperPane);

        wrapperPane.getChildren().add(canvas);
        canvas.widthProperty().bind(wrapperPane.widthProperty());
        canvas.heightProperty().bind(wrapperPane.heightProperty());

        Spinner<Integer> widthSpinner = new Spinner<>(1, Integer.MAX_VALUE, maze.getWidth());
        widthSpinner.setEditable(true);
        widthSpinner.setPromptText("Width");
        Spinner<Integer> heightSpinner = new Spinner<>(1, Integer.MAX_VALUE, maze.getHeight());
        heightSpinner.setEditable(true);
        heightSpinner.setPromptText("Height");

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(event -> {
            if (maze.getWidth() != widthSpinner.getValue() || maze.getHeight() != heightSpinner.getValue()) {
                maze.changeDimensions(widthSpinner.getValue(), heightSpinner.getValue());
            }
            maze.generate();
            mazeSolver.clear();
            canvas.draw();
        });

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(event -> {
            if (maze.getWidth() != widthSpinner.getValue() || maze.getHeight() != heightSpinner.getValue()) {
                maze.changeDimensions(widthSpinner.getValue(), heightSpinner.getValue());
            }
            maze.clear();
            mazeSolver.clear();
            canvas.draw();
        });

        Button solveButton = new Button("Solve");
        solveButton.setOnAction(event -> {
            mazeSolver.solve(maze.getWidth() - 1, maze.getHeight() - 1, 0, 0);
            canvas.draw();
        });

        ToolBar toolBar = new ToolBar(
                widthSpinner,
                heightSpinner,
                generateButton,
                clearButton,
                new Separator(),
                solveButton
        );

        root.setBottom(toolBar);

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    class MazeCanvas extends Canvas {
        private final Maze maze;
        private MazeSolver solver = null;

        public MazeCanvas(Maze maze) {
            this.maze = maze;

            // Redraw canvas when size changes.
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
        }

        public MazeCanvas(Maze maze, MazeSolver solver) {
            this.maze = maze;
            this.solver = solver;

            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
        }

        private void draw() {
            double width = getWidth();
            double height = getHeight();

            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, width, height);

            int mazeWidth = maze.getWidth();
            int mazeHeight = maze.getHeight();

            double cellWidth = width/mazeWidth;
            double cellHeight = height/mazeHeight;

            // Start position
            gc.setFill(Color.GREEN);
            gc.fillRect((mazeWidth-1)*cellWidth + 1, (mazeHeight-1)*cellHeight + 1, cellWidth - 1, cellHeight - 1);

            // End position
            gc.setFill(Color.RED);
            gc.fillRect(0, 0, cellWidth - 1, cellHeight - 1);

            gc.setStroke(Color.BLACK);
            for (int x = 0; x < mazeWidth; x++) {
                for (int y = 0; y < mazeHeight; y++) {
                    //gc.setFill(Color.rgb(255,255,255));
                    //gc.fillRect(x*cellWidth, y*cellHeight, cellWidth, cellHeight);

                    MazeCell cell = this.maze.getCell(x,y);

                    // North
                    if (cell.hasWall(WallConstants.WALL_NORTH)) {
                        gc.strokeLine(x*cellWidth, y*cellHeight, x*cellWidth+cellWidth, y*cellHeight);
                    }

                    // South
                    if (cell.hasWall(WallConstants.WALL_SOUTH)) {
                        gc.strokeLine(x*cellWidth, y*cellHeight + cellHeight, x*cellWidth+cellWidth, y*cellHeight + cellHeight);
                    }

                    // East
                    if (cell.hasWall(WallConstants.WALL_EAST)) {
                        gc.strokeLine(x*cellWidth + cellWidth, y*cellHeight, x*cellWidth + cellWidth, y*cellHeight + cellHeight);
                    }

                    // West
                    if (cell.hasWall(WallConstants.WALL_WEST)) {
                        gc.strokeLine(x*cellWidth, y*cellHeight, x*cellWidth, y*cellHeight + cellHeight);
                    }
                }
            }

            if (solver != null && solver.solved()) {
                gc.setStroke(Color.PURPLE);

                LinkedList<MazeCell> solution = solver.getSolution();
                MazeCell prevCell = null;
                for (MazeCell cell : solution) {
                    if (prevCell == null) {
                        prevCell = cell;
                        continue;
                    }

                    double currMidpointX = (cell.getX()*cellWidth + cell.getX()*cellWidth + cellWidth)/2;
                    double currMidpointY = (cell.getY()*cellHeight + cell.getY()*cellHeight + cellHeight)/2;

                    double prevMidpointX = (prevCell.getX()*cellWidth + prevCell.getX()*cellWidth + cellWidth)/2;
                    double prevMidpointY = (prevCell.getY()*cellHeight + prevCell.getY()*cellHeight + cellHeight)/2;

                    gc.strokeLine(prevMidpointX, prevMidpointY, currMidpointX, currMidpointY);

                    prevCell = cell;
                }
            }
        }

        @Override
        public boolean isResizable() {
            return true;
        }

        @Override
        public double prefWidth(double height) {
            return getWidth();
        }

        @Override
        public double prefHeight(double width) {
            return getHeight();
        }

    }
}
