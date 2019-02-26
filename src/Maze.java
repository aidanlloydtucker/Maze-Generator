/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * The maze representation
 */
public class Maze {
    private final Random random = new Random();
    // The internal representation of the maze
    private MazeCell[][] mazeMatrix;


    // Dimensions of maze
    private int width;
    private int height;

    // Start and end points of maze
    private int startX;
    private int startY;
    private int endX = 0;
    private int endY = 0;

    /**
     * Creates a maze with width and height dimensions
     *
     * @param w width
     * @param h height
     */
    public Maze(int w, int h) {
        this.width = w;
        this.height = h;

        updateStartEndPoints();

        clear();
    }

    private void updateStartEndPoints() {
        this.setStart(this.width - 1, this.height - 1);
        this.setEnd(0, 0);
    }

    public void setStart(int x, int y) {
        this.startX = x;
        this.startY = y;
    }

    public void setEnd(int x, int y) {
        this.endX = x;
        this.endY = y;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    /**
     * Changes dimensions of maze
     *
     * @param w width
     * @param h height
     */
    public void changeDimensions(int w, int h) {
        this.width = w;
        this.height = h;

        updateStartEndPoints();

        clear();
    }

    /**
     * Clears maze by creating new cells with all walls
     */
    public void clear() {
        MazeCell[][] genMatrix = new MazeCell[height][width];
        for (int i = 0; i < genMatrix.length; i++) {
            for (int j = 0; j < genMatrix[i].length; j++) {
                genMatrix[i][j] = new MazeCell(j, i);
            }
        }

        this.mazeMatrix = genMatrix;
    }

    /**
     * Generates the maze using a modified Prim's algorithm
     */
    public void generate() {
        clear();
        // Get random cell to start
        int randCellX = random.nextInt(width);
        int randCellY = random.nextInt(height);
        MazeCell randCell = this.mazeMatrix[randCellY][randCellX];

        // Generate a list of adjacent cells
        RandomList<MazeCell> adjList = new RandomList<>();

        adjList.add(randCell);

        // While we have stuff in the adjacency list
        while (!adjList.isEmpty()) {
            // Get a random cell and remove it
            MazeCell cell = adjList.popRandom();

            // TODO: THIS IS HOW I FIXED IT
            // If this cell has already been visited, ignore it
            if (cell.isVisited()) {
                continue;
            }

            // Visit the cell
            cell.visit();

            // Get its neighboring/adjacent cells
            ArrayList<MazeCell> neighbors = getNeighbors(cell);

            // Randomly go through the list of neighbors until you find one that is already visited
            Iterator<MazeCell> iter = new RandomListIterator<>(neighbors);
            while (iter.hasNext()) {
                MazeCell adjCell = iter.next();

                // Then, remove the wall between the current cell and its already visited neighbor
                if (adjCell.isVisited()) {
                    removeWall(cell, adjCell);
                    break;
                }
            }

            // Add the neighboring cells that haven't been visited to the adjacency list
            for (MazeCell adjCell : neighbors) {
                if (!adjCell.isVisited()) {
                    adjList.add(adjCell);
                }
            }
        }
    }

    /**
     * Remove the wall between two maze cells
     *
     * @param a the maze cell that a wall will be removed from
     * @param b the adjacent maze cell with the wall connecting
     */
    private void removeWall(@NotNull MazeCell a, @NotNull MazeCell b) {
        int abDirection = a.cellDirection(b);
        a.openWall(abDirection);
        b.openWall(WallConstants.invertDirection(abDirection));
    }

    /**
     * Get the neighbors of a cell
     * @param cell the maze cell
     * @return neighbors of that cell, ordered by [W,N,E,S]
     */
    public ArrayList<MazeCell> getNeighbors(MazeCell cell) {
        ArrayList<MazeCell> cells = new ArrayList<>();
        if (cell.getX() > 0) {
            cells.add(mazeMatrix[cell.getY()][cell.getX()-1]);
        }
        if (cell.getY() > 0) {
            cells.add(mazeMatrix[cell.getY()-1][cell.getX()]);
        }
        if (cell.getX() < width-1) {
            cells.add(mazeMatrix[cell.getY()][cell.getX()+1]);
        }
        if (cell.getY() < height-1) {
            cells.add(mazeMatrix[cell.getY()+1][cell.getX()]);
        }

        return cells;
    }

    /**
     * The height of the maze.
     * @return the height of the maze
     */
    public int getHeight() {
        return height;
    }

    /**
     * The width of the maze.
     * @return the width of the maze
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the maze cell at the specified coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @return the maze cell at (x,y)
     */
    public MazeCell getCell(int x, int y) {
        return this.mazeMatrix[y][x];
    }

    /**
     * Converts to a string. Each cell is represented as a 3x3 square
     * @return the string representing the maze
     */
    public String toString() {
        StringBuilder str = new StringBuilder();

        // For all in the y direction
        for (int i = 0; i < mazeMatrix.length; i++) {
            // Because cells are represented as 3x3 squares, we need to have 3 lines
            StringBuilder str1 = new StringBuilder();
            StringBuilder str2 = new StringBuilder();
            StringBuilder str3 = new StringBuilder();

            // For each cell in this row, get the 3x3 representation and append each respective line to the lines
            for (int j = 0; j < mazeMatrix[i].length; j++) {
                String[] cellRep = mazeMatrix[i][j].toCellRep();
                str1.append(cellRep[0]);
                str2.append(cellRep[1]);
                str3.append(cellRep[2]);
            }

            // Add these 3 lines to the main string
            str1.append("\n");
            str2.append("\n");
            str3.append("\n");
            str.append(str1);
            str.append(str2);
            str.append(str3);
        }
        return str.toString();
    }
}
