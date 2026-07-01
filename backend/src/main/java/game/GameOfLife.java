package game;

import java.util.Arrays;
import java.util.Objects;

public class GameOfLife {
    private boolean[][] grid;

    public GameOfLife(boolean[][] grid) {
        this.grid = grid;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameOfLife that = (GameOfLife) o;
        return Objects.deepEquals(grid, that.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(boolean[] col : grid){
            sb.append(Arrays.toString(col)).append("\n");
        }
        return sb.toString();
    }
}
