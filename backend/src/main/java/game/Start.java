package game;

public class Start {
    public static void main(String[] args) {
        boolean[][] grid = new boolean[20][20];

        GameOfLife game = new GameOfLife(grid);
        game.getGrid()[0][1] = true;
        game.getGrid()[1][2] = true;
        game.getGrid()[2][0] = true;
        game.getGrid()[2][1] = true;
        game.getGrid()[2][2] = true;
        System.out.println(game);
        //game.countNeighbours(0, 1);
        game.nextGeneration();
        System.out.println("Next generation:");
        System.out.println(game);
        game.nextGeneration();
        System.out.println("Next generation:");
        System.out.println(game);
        game.nextGeneration();
        System.out.println("Next generation:");
        System.out.println(game);
        System.out.println("Next generation:");
        System.out.println(game);
        game.nextGeneration();
        System.out.println("Next generation:");
        System.out.println(game);
        System.out.println("Next generation:");
        System.out.println(game);
        game.nextGeneration();
        System.out.println("Next generation:");
        System.out.println(game);
        System.out.println("Next generation:");
        System.out.println(game);
        game.nextGeneration();
        System.out.println("Next generation:");
        System.out.println(game);
    }
}
