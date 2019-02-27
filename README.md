# Maze Generator

## Outline
 * `Maze.java` contains the maze generation logic and the maze object
   * `MazeCell.java` contains the logic for each cell in the maze
     * `WallConstants.java` contains the constants and logic for cell walls
 * `RandomList.java` is a list with random `get()` and `add()`
 * `RandomListIterator.java` is an iterator that randomly chooses the order
 * `MazeSolver.java` contains the logic for the DFS maze solver
 * GUI:
   * `MazeRunner.java` is the main GUI class
   * `MazeCanvas.java` displays the maze itself on the GUI frame
 * Command Line:
   * `CommandLine.java` runs the CLI version of the maze