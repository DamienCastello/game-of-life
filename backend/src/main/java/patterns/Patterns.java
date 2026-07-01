package patterns;

public class Patterns {
    public static void createBlinker(boolean[][] grid) {
        int row = grid.length / 2;
        int col = grid[0].length / 2;

        grid[row][col - 1] = true;
        grid[row][col] = true;
        grid[row][col + 1] = true;
    }

    public static void createGlider(boolean[][] grid) {
        int row = grid.length / 2;
        int col = grid[0].length / 2;

        grid[row][col + 1] = true;
        grid[row + 1][col + 2] = true;
        grid[row + 2][col] = true;
        grid[row + 2][col + 1] = true;
        grid[row + 2][col + 2] = true;
    }

    public static void createBlock(boolean[][] grid) {
        int row = grid.length / 2;
        int col = grid[0].length / 2;

        grid[row][col] = true;
        grid[row][col + 1] = true;
        grid[row + 1][col] = true;
        grid[row + 1][col + 1] = true;
    }
}
