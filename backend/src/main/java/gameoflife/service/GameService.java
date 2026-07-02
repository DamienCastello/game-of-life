package gameoflife.service;

import gameoflife.model.GameOfLife;
import org.springframework.stereotype.Service;
import gameoflife.patterns.Patterns;

@Service
public class GameService {

    private GameOfLife game;

    public boolean[][] applyPattern(boolean[][] grid, String pattern) {

        switch (pattern) {
            case "blinker" -> Patterns.createBlinker(grid);
            case "glider" -> Patterns.createGlider(grid);
            case "block" -> Patterns.createBlock(grid);
        }

        return grid;
    }

    public boolean[][] preview(String pattern) {

        boolean[][] grid = new boolean[20][20];

        applyPattern(grid, pattern);

        return grid;
    }

    public boolean[][] start(String pattern, boolean[][] customGrid) {
        if ("custom".equals(pattern)) {
            // La partie a été dessinée par le joueur
            game = new GameOfLife(customGrid);
        } else {
            // La partie démarre avec un pattern prédéfini
            game = new GameOfLife(new boolean[20][20]);
            applyPattern(game.getGrid(), pattern);
        }

        return game.getGrid();
    }

    public boolean[][] next() {
        game.nextGeneration();
        return game.getGrid();
    }
}