package gameoflife.service;

import gameoflife.model.GameOfLife;
import org.springframework.stereotype.Service;
import gameoflife.patterns.Patterns;

@Service
public class GameService {

    private GameOfLife game;

    public boolean[][] start(String pattern) {
        game = new GameOfLife(new boolean[20][20]);

        switch (pattern) {
            case "blinker" -> Patterns.createBlinker(game.getGrid());
            case "glider" -> Patterns.createGlider(game.getGrid());
            case "block" -> Patterns.createBlock(game.getGrid());
        }

        return game.getGrid();
    }

    public boolean[][] next() {
        game.nextGeneration();
        return game.getGrid();
    }
}