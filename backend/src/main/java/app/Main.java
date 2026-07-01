package app;

import game.GameOfLife;
import patterns.Patterns;

import java.util.Scanner;

public class Main {
    private static final int GRID_SIZE = 20;

    private static void runSimulation(GameOfLife game, int time) throws InterruptedException {
        while (true) {
            System.out.println("\n".repeat(40));
            System.out.println(game);
            game.nextGeneration();
            Thread.sleep(time);
        }
    }

    private static void displayMenu() {
        System.out.println("BIENVENUE DANS LE JEU DE LA VIE\n");

        System.out.println("Choisir une configuration de départ:");
        System.out.println("1: Blinker");
        System.out.println("2: Glider");
        System.out.println("3: Block");
        System.out.println("0: Quitter");
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        displayMenu();
        int choice = scanner.nextInt();

        if (choice == 0) {
            System.out.println("Fin de la simulation.");
            return;
        }

        if (choice < 1 || choice > 3) {
            System.out.println("Choisissez une option du menu !");
            return;
        }

        GameOfLife game = new GameOfLife(new boolean[GRID_SIZE][GRID_SIZE]);

        switch (choice) {
            case 1:
                Patterns.createBlinker(game.getGrid());
                break;
            case 2:
                Patterns.createGlider(game.getGrid());
                break;
            case 3:
                Patterns.createBlock(game.getGrid());
                break;
        }

        runSimulation(game, 500);
    }
}