package game;

import java.util.Arrays;
import java.util.Objects;

public class GameOfLife {
    private boolean[][] grid;

    public GameOfLife(boolean[][] grid) {
        this.grid = grid;
    }

    public int countNeighbours(int row, int col){
        /* -------------------------------
        “row = je change d’étage”
        “col = je me déplace dans l’étage”

                    haut (row - 1)
                        ↑
            gauche ←  (row,col)  → droite
                        ↓
                    bas (row + 1)
         ------------------------------- */
        int neighbours = 0;
        for(int dy = -1; dy <= 1; dy ++){
            for(int dx = -1; dx <= 1; dx ++){
                if(!(dy == 0 && dx == 0)){
                    int newRow = row + dy;
                    int newCol = col + dx;

                    if (newRow >= 0 && newRow < grid.length &&
                            newCol >= 0 && newCol < grid[0].length){
                        neighbours ++;
                        System.out.println(grid[newRow][newCol]);
                    }
                }
            }
        }
        return neighbours;
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
