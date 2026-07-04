package gameoflife.dto;

/**
 * État renvoyé au front quand on charge un pattern :
 * la grille (déjà dimensionnée) et le mode tore recommandé.
 */
public class GameState {
    public boolean[][] grid;
    public boolean torus;

    public GameState(boolean[][] grid, boolean torus) {
        this.grid = grid;
        this.torus = torus;
    }
}
