/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

public class MazeCell {
    private boolean visited = false;

    private final int X;
    private final int Y;

    private int wallFlags;

    /**
     * Create a maze cell with the passed coordinates
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public MazeCell(int x, int y) {
        this.X = x;
        this.Y = y;
        this.wallFlags = WallConstants.WALL_ALL;
    }

    /**
     * Get the x coordinate
     *
     * @return x coordinate
     */
    public int getX() {
        return X;
    }

    /**
     * Get the y coordinate
     * @return y coordinate
     */
    public int getY() {
        return Y;
    }

    /**
     * Get the wall flags
     * @return wall flags
     */
    public int getWallFlags() {
        return this.wallFlags;
    }

    /**
     * Sets the wall flags
     *
     * @param wf wall flags
     */
    public void setWallFlags(int wf) {
        this.wallFlags = wf;
    }

    /**
     * Checks if cell has the wall
     * @param wall the specific wall
     * @return if the cell has the wall
     */
    public boolean hasWall(int wall) {
        return WallConstants.includes(this.wallFlags, wall);
    }

    /**
     * Checks if there exists a passage in the cell
     * @return if there exists 3 or fewer walls
     */
    public boolean hasPassage() {
        return this.wallFlags != WallConstants.WALL_ALL;
    }

    /**
     * Open the wall for passage
     * @param wall the wall to open
     */
    public void openWall(int wall) {
        this.wallFlags &= ~wall;
    }

    /**
     * Visit the cell
     */
    public void visit() {
        this.visited = true;
    }

    /**
     * Checks if cell has been visited
     * @return true if cell has been visited, otherwise false
     */
    public boolean isVisited() {
        return this.visited;
    }

    /**
     * The direction of the other cell relative to the current cell
     * @param o the other cell
     * @return the direction in walls (can get to second-degree directions like NE, SW, etc.)
     */
    public int cellDirection(MazeCell o) {
        int res = 0;

        if (o.getX() > this.getX()) {
            res |= WallConstants.WALL_EAST;
        } else if (o.getX() < this.getX()) {
            res |= WallConstants.WALL_WEST;
        }

        if (o.getY() > this.getY()) {
            res |= WallConstants.WALL_SOUTH;
        } else if (o.getY() < this.getY()) {
            res |= WallConstants.WALL_NORTH;
        }

        return res;
    }

    /**
     * If there is a passage from this cell to the other and from the other to this cell
     * @param o the other cell
     * @return true if there is a passage between cells
     */
    public boolean hasPassage(MazeCell o) {
        int direction = this.cellDirection(o);
        return (this.wallFlags & direction) == 0 && (o.getWallFlags() & WallConstants.invertDirection(direction)) == 0;
    }

    /**
     * Returns the cellular representation (3x3) of the cell
     * @return the 3x3 text block of the cell
     */
    public String[] toCellRep() {
        String[] strArr = new String[3];

        strArr[0] = "██";
        strArr[0] += hasWall(WallConstants.WALL_NORTH) ? "██" : "  ";
        strArr[0] += "██";

        strArr[1] = hasWall(WallConstants.WALL_WEST) ? "██" : "  ";
        strArr[1] += "  ";
        strArr[1] += hasWall(WallConstants.WALL_EAST) ? "██" : "  ";

        strArr[2] = "██";
        strArr[2] += hasWall(WallConstants.WALL_SOUTH) ? "██" : "  ";
        strArr[2] += "██";

        return strArr;
    }

    /**
     * Returns the coordinates
     * @return string of cell coordinates
     */
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }
}
