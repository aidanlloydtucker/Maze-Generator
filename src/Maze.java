import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Maze {
    private MazeCell[][] mazeMatrix;
    private int height;
    private int width;

    public Maze(int w, int h) {
        this.width = w;
        this.height = h;

        clear();
    }

    public void changeDimensions(int w, int h) {
        this.width = w;
        this.height = h;
        clear();
    }

    public void clear() {
        MazeCell[][] genMatrix = new MazeCell[height][width];
        for (int i = 0; i < genMatrix.length; i++) {
            for (int j = 0; j < genMatrix[i].length; j++) {
                genMatrix[i][j] = new MazeCell(j, i);
            }
        }

        this.mazeMatrix = genMatrix;
    }

    public void generate() {
        clear();

        Random rand = new Random();

        // Get random cell to start
        int randCellX = rand.nextInt(width);
        int randCellY = rand.nextInt(height);

        // Visit this random cell
        MazeCell randCell = this.mazeMatrix[randCellY][randCellX];

        // Generate a list of adjacent cells
        RandomList<MazeCell> adjList = new RandomList<>();

        adjList.add(randCell);

        // While we have stuff in the adjacency list
        while (!adjList.isEmpty()) {
            // Get a random cell and remove it
            MazeCell cell = adjList.popRandom();

            // TODO: THIS IS HOW I FIXED IT
            if (cell.isVisited()) {
                continue;
            }

            cell.visit();

            // Get its adjacent cells
            ArrayList<MazeCell> adjCells = getNeighbors(cell);

            Iterator<MazeCell> iter = new RandomListIterator<>(adjCells);
            while (iter.hasNext()) {
                MazeCell adjCell = iter.next();
                if (adjCell.isVisited()) {
                    removeWall(cell, adjCell);
                    break;
                }
            }

            // Add neighboring cells
            for (MazeCell adjCell : adjCells) {
                if (!adjCell.isVisited()) {
                    adjList.add(adjCell);
                }
            }

        }
    }

    private void removeWall(MazeCell cell1, MazeCell cell2) {
        int c1to2Dir = cell1.cellDirection(cell2);
        cell1.openWall(c1to2Dir);
        cell2.openWall(MazeCell.invertDirection(c1to2Dir));
    }

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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public MazeCell getCell(int x, int y) {
        return this.mazeMatrix[y][x];
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < mazeMatrix.length; i++) {
            String[] strLines = new String[3];
            strLines[0] = "";
            strLines[1] = "";
            strLines[2] = "";
            for (int j = 0; j < mazeMatrix[i].length; j++) {
                strLines[0] += mazeMatrix[i][j].toCellRep()[0];
                strLines[1] += mazeMatrix[i][j].toCellRep()[1];
                strLines[2] += mazeMatrix[i][j].toCellRep()[2];
            }
            str.append(strLines[0]);
            str.append("\n");
            str.append(strLines[1]);
            str.append("\n");
            str.append(strLines[2]);
            str.append("\n");
        }
        return str.toString();
    }
}
