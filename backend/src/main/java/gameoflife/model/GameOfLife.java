package gameoflife.model;

import java.util.Arrays;
import java.util.Objects;

public class GameOfLife {
    private boolean[][] grid;

    // true  -> grille "tore" : les bords bouclent (planète sans bord)
    // false -> grille bornée : hors de la grille = cellule morte
    private final boolean torus;

    public GameOfLife(boolean[][] grid) {
        this(grid, true);
    }

    public GameOfLife(boolean[][] grid, boolean torus) {
        this.grid = grid;
        this.torus = torus;
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
    int height = grid.length;
    int width = grid[0].length;

    for(int dy = -1; dy <= 1; dy ++){
        for(int dx = -1; dx <= 1; dx ++){

            // On ignore la cellule elle-même
            if(!(dy == 0 && dx == 0)){

                int newRow = row + dy;
                int newCol = col + dx;

                if (torus) {
                    /* -----------------------------------------
                    Mode tore : on fait "boucler" les coordonnées.
                    - si on sort à gauche  → on revient à droite
                    - si on sort en haut   → on revient en bas
                    Cela transforme la grille en "tore"
                    (comme une planète sans bord).
                    ----------------------------------------- */
                    newRow = (newRow + height) % height;
                    newCol = (newCol + width) % width;
                } else if (newRow < 0 || newRow >= height
                        || newCol < 0 || newCol >= width) {
                    /* -----------------------------------------
                    Mode borné : tout ce qui est hors de la grille
                    est considéré comme mort. On ignore ce voisin.
                    ----------------------------------------- */
                    continue;
                }

                // Si la cellule voisine est vivante
                if(grid[newRow][newCol]){
                    neighbours ++;
                }
            }
        }
    }

    return neighbours;
}

    public void nextGeneration(){
        // Nouvelle grille de même taille que l'actuelle
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];

        /* -----------------------------------------
        On crée une nouvelle génération basée sur la grille actuelle.

        IMPORTANT :
        On ne modifie jamais la grille en cours pendant le calcul,
        sinon les calculs de voisins seraient faussés.

        On travaille donc sur une copie "newGrid",
        puis on remplace la grille à la fin.
        ----------------------------------------- */

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
