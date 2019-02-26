public class MazeCell {
    private boolean visited = false;

    public static final int WallNorth = (1 << 0);
    public static final int WallSouth = (1 << 1);
    public static final int WallEast = (1 << 2);
    public static final int WallWest = (1 << 3);
    public static final int WallAll = (WallNorth | WallSouth | WallEast | WallWest);

    public static int invertDirection(int wall) {
        switch (wall) {
            case WallNorth:
                return WallSouth;
            case WallSouth:
                return WallNorth;
            case WallEast:
                return WallWest;
            case WallWest:
                return WallEast;
            default:
                return wall;
        }
    }

    private final int X;
    private final int Y;

    private int wallFlags;

    public MazeCell(int x, int y) {
        this.X = x;
        this.Y = y;
        this.wallFlags = WallAll;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getWallFlags() {
        return this.wallFlags;
    }

    public boolean hasWall(int wall) {
        return (this.wallFlags & (wall)) > 0;
    }

    public boolean hasPassage() {
        return this.wallFlags != WallAll;
    }

    public void setWallFlags(int wf) {
        this.wallFlags = wf;
    }

    public void openWall(int wall) {
        this.wallFlags &= ~wall;
    }

    public void visit() {
        this.visited = true;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public int cellDirection(MazeCell o) {
        int res = 0;

        if (o.getX() > this.getX()) {
            res |= WallEast;
        } else if (o.getX() < this.getX()) {
            res |= WallWest;
        }

        if (o.getY() > this.getY()) {
            res |= WallSouth;
        } else if (o.getY() < this.getY()) {
            res |= WallNorth;
        }

        return res;
    }

    public boolean hasPassage(MazeCell o) {
        int direction = this.cellDirection(o);
        return (this.wallFlags & direction) == 0 && (o.getWallFlags() & invertDirection(direction)) == 0;
    }

    public void removeWall(MazeCell o) {
        int direction = this.cellDirection(o);
        this.openWall(direction);
        o.openWall(MazeCell.invertDirection(direction));
    }

    public String[] toCellRep() {
        String[] strArr = new String[3];

        strArr[0] = "██";
        if ((this.wallFlags & WallNorth) > 0) {
            strArr[0] += "██";
        } else {
            strArr[0] += "  ";
        }
        strArr[0] += "██";

        if ((this.wallFlags & WallWest) > 0) {
            strArr[1] = "██";
        } else {
            strArr[1] = "  ";
        }

        strArr[1] += "  ";//"" + getX() + "" + getY();

        if ((this.wallFlags & WallEast) > 0) {
            strArr[1] += "██";
        } else {
            strArr[1] += "  ";
        }

        strArr[2] = "██";
        if ((this.wallFlags & WallSouth) > 0) {
            strArr[2] += "██";
        } else {
            strArr[2] += "  ";
        }
        strArr[2] += "██";

        return strArr;
    }

    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }
}
