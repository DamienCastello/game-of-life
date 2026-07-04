package gameoflife.dto;

/**
 * Requête pour calculer la génération suivante.
 * Le front envoie sa grille : le serveur ne conserve aucun état entre
 * les requêtes.
 */
public class NextRequest {
    public boolean[][] grid;
}
