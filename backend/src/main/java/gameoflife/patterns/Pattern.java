package gameoflife.patterns;

/**
 * Un pattern (motif) du jeu de la vie, décrit sous forme "d'art ASCII".
 *
 * Chaque ligne de {@code art} représente une rangée de cellules :
 *   - '.' ou ' ' = cellule morte
 *   - tout autre caractère (par convention 'O') = cellule vivante
 *
 * Le pattern connaît aussi la taille de grille qui le met en valeur
 * (ex : une "usine" a besoin d'une grande grille).
 */
public class Pattern {

    public final String id;      // identifiant technique (ex : "gosper")
    public final String label;   // libellé affiché (ex : "Gosper Gun")
    public final int rows;        // hauteur de grille recommandée
    public final int cols;        // largeur de grille recommandée
    private final String[] art;   // le motif, ligne par ligne

    public Pattern(String id, String label, int rows, int cols, String... art) {
        this.id = id;
        this.label = label;
        this.rows = rows;
        this.cols = cols;
        this.art = art;
    }

    /**
     * Construit une grille {@code rows x cols} avec le motif centré.
     */
    public boolean[][] toGrid() {
        boolean[][] grid = new boolean[rows][cols];

        int artHeight = art.length;
        int artWidth = 0;
        for (String line : art) {
            artWidth = Math.max(artWidth, line.length());
        }

        // On centre le motif dans la grille
        int top = (rows - artHeight) / 2;
        int left = (cols - artWidth) / 2;

        for (int r = 0; r < artHeight; r++) {
            String line = art[r];
            for (int c = 0; c < line.length(); c++) {
                char cell = line.charAt(c);
                if (cell != '.' && cell != ' ') {
                    int gridRow = top + r;
                    int gridCol = left + c;
                    // On ignore ce qui déborderait de la grille
                    if (gridRow >= 0 && gridRow < rows && gridCol >= 0 && gridCol < cols) {
                        grid[gridRow][gridCol] = true;
                    }
                }
            }
        }

        return grid;
    }
}
