/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

public class WallConstants {
    public static final int WALL_NORTH = 1 << 0;
    public static final int WALL_SOUTH = 1 << 1;
    public static final int WALL_EAST = 1 << 2;
    public static final int WALL_WEST = 1 << 3;

    public static final int WALL_ALL = (WALL_NORTH | WALL_SOUTH | WALL_EAST | WALL_WEST);
    public static final int WALL_NONE = 0;

    public static int invertDirection(int wall) {
        switch (wall) {
            case WALL_NORTH:
                return WALL_SOUTH;
            case WALL_SOUTH:
                return WALL_NORTH;
            case WALL_EAST:
                return WALL_WEST;
            case WALL_WEST:
                return WALL_EAST;
            default:
                return WALL_NONE;
        }
    }

    public static int include(int wf, int wall) {
        return wf | wall;
    }

    public static int exclude(int wf, int wall) {
        return wf & ~wall;
    }

    public static boolean includes(int wf, int wall) {
        return (wf & wall) > 0;
    }
}
