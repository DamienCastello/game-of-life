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
                        // Cellule voisine vivante
                        if(grid[newRow][newCol]){
                            neighbours ++;
                        }
                        //System.out.println(grid[newRow][newCol]);
                    }
                }
            }
        }
        return neighbours;
    }

    public void nextGeneration(){
        boolean[][] newGrid = new boolean[20][20];

        for(int row = 0; row < grid.length; row ++){
            for(int col = 0; col < grid[0].length; col ++){
                int neighbours = countNeighbours(row, col);
                // Cellule vivante
                if(grid[row][col]){
                    // Survie
                    if(neighbours == 2 || neighbours == 3) {
                        newGrid[row][col] = true;
                    }
                    // meurt par isolement ou surpopulation
                    else {
                        newGrid[row][col] = false;
                    }
                }
                // Cellule morte
                else {
                    // Naissance
                    if(neighbours == 3) {
                        newGrid[row][col] = true;
                    }
                    // Pas de naissance
                    else {
                        newGrid[row][col] = false;
                    }
                }
            }
        }
        grid = newGrid;
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

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {

                if (grid[row][col]) {
                    sb.append("X ");
                } else {
                    sb.append(". ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
