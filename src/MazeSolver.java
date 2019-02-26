/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class MazeSolver {
    private final Maze maze;
    private int[][] visitedCells;
    private LinkedList<MazeCell> solution;

    public MazeSolver(Maze maze) {
        this.maze = maze;
        clear();
    }

    public void clear() {
        this.solution = null;
        this.visitedCells = new int[maze.getHeight()][maze.getWidth()];
        for (int i = 0; i < visitedCells.length; i++) {
            visitedCells[i] = new int[maze.getWidth()];
        }
    }

    // Return an array of cells (and directions)
    public void solve(int startX, int startY, int endX, int endY) {
        clear();

        LinkedList<MazeCell> s = new LinkedList<>();
        recursiveSolve(startX, startY, endX, endY, s);

        this.solution = s;
    }

    public LinkedList<MazeCell> getSolution() {
        return solution;
    }

    public boolean solved() {
        return solution != null && !solution.isEmpty();
    }

    private boolean recursiveSolve(int x, int y, int endX, int endY, LinkedList<MazeCell> s) {
        if (x == endX && y == endY) {
            s.addFirst(maze.getCell(x, y));
            this.visitedCells[y][x] = 2;
            return true;
        }

        if (this.visitedCells[y][x] > 0) {
            return false;
        }

        this.visitedCells[y][x] = 1;
        MazeCell cell = maze.getCell(x, y);

        ArrayList<MazeCell> neighbors = maze.getNeighbors(cell);

        boolean foundEnd = false;
        for (MazeCell neighbor : neighbors) {
            if (cell.hasPassage(neighbor) && visitedCells[neighbor.getY()][neighbor.getX()] == 0) {
                boolean recFound = recursiveSolve(neighbor.getX(), neighbor.getY(), endX, endY, s);
                if (recFound) {
                    foundEnd = true;
                    this.visitedCells[y][x] = 2;
                    s.addFirst(cell);
                    break;
                }
            }
        }

        return foundEnd;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                str.append(visitedCells[y][x]);
                str.append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}
