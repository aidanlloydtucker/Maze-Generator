/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;

class MazeCanvas extends Canvas {
    private final Maze maze;
    private MazeSolver solver = null;

    public MazeCanvas(Maze maze) {
        this.maze = maze;

        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public MazeCanvas(Maze maze, MazeSolver solver) {
        this.maze = maze;
        this.solver = solver;

        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    protected void draw() {
        double width = getWidth();
        double height = getHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        int mazeWidth = maze.getWidth();
        int mazeHeight = maze.getHeight();

        double cellWidth = width / mazeWidth;
        double cellHeight = height / mazeHeight;

        drawStartEnd(gc, cellWidth, cellHeight);

        drawMaze(gc, cellWidth, cellHeight);

        if (solver != null && solver.solved()) {
            drawSolution(gc, cellWidth, cellHeight);
        }
    }

    private void drawStartEnd(GraphicsContext gc, double cellWidth, double cellHeight) {
        // Start position
        gc.setFill(Color.GREEN);
        gc.fillRect(maze.getStartX() * cellWidth + 1, maze.getStartY() * cellHeight + 1, cellWidth - 1, cellHeight - 1);

        // End position
        gc.setFill(Color.RED);
        gc.fillRect(maze.getEndX(), maze.getEndY(), cellWidth - 1, cellHeight - 1);
    }

    private void drawMaze(GraphicsContext gc, double cellWidth, double cellHeight) {
        int mazeWidth = maze.getWidth();
        int mazeHeight = maze.getHeight();

        gc.setStroke(Color.BLACK);
        for (int x = 0; x < mazeWidth; x++) {
            for (int y = 0; y < mazeHeight; y++) {

                MazeCell cell = this.maze.getCell(x, y);

                // North
                if (cell.hasWall(WallConstants.WALL_NORTH)) {
                    gc.strokeLine(x * cellWidth, y * cellHeight, x * cellWidth + cellWidth, y * cellHeight);
                }

                // South
                if (cell.hasWall(WallConstants.WALL_SOUTH)) {
                    gc.strokeLine(x * cellWidth, y * cellHeight + cellHeight, x * cellWidth + cellWidth, y * cellHeight + cellHeight);
                }

                // East
                if (cell.hasWall(WallConstants.WALL_EAST)) {
                    gc.strokeLine(x * cellWidth + cellWidth, y * cellHeight, x * cellWidth + cellWidth, y * cellHeight + cellHeight);
                }

                // West
                if (cell.hasWall(WallConstants.WALL_WEST)) {
                    gc.strokeLine(x * cellWidth, y * cellHeight, x * cellWidth, y * cellHeight + cellHeight);
                }
            }
        }
    }

    private void drawSolution(GraphicsContext gc, double cellWidth, double cellHeight) {
        gc.setStroke(Color.PURPLE);

        LinkedList<MazeCell> solution = solver.getSolution();
        MazeCell prevCell = null;
        for (MazeCell cell : solution) {
            if (prevCell == null) {
                prevCell = cell;
                continue;
            }

            double currMidpointX = getCellMidpointX(cell, cellWidth);
            double currMidpointY = getCellMidpointY(cell, cellHeight);

            double prevMidpointX = getCellMidpointX(prevCell, cellWidth);
            double prevMidpointY = getCellMidpointY(prevCell, cellHeight);

            gc.strokeLine(prevMidpointX, prevMidpointY, currMidpointX, currMidpointY);

            prevCell = cell;
        }
    }

    private double getCellMidpointX(MazeCell cell, double cellWidth) {
        return (cell.getX() * cellWidth + cell.getX() * cellWidth + cellWidth) / 2;
    }

    private double getCellMidpointY(MazeCell cell, double cellHeight) {
        return (cell.getY() * cellHeight + cell.getY() * cellHeight + cellHeight) / 2;
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
