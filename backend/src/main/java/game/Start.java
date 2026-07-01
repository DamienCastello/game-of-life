package game;

public class Start {
    public static void main(String[] args) {
        boolean[][] grid = new boolean[20][20];

        GameOfLife game = new GameOfLife(grid);
        game.getGrid()[0][0] = true;
        System.out.println(game);
    }
}
