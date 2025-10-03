package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static int gameSize = 10;

    public static void main(String[] args) {

        Board player1 = new Board(gameSize);
        Board player2 = new Board(gameSize);
        Board[] players = new Board[] {player1, player2};

        Scanner scanner = new Scanner(System.in);

        prepGame(scanner, player1, player2);

        System.out.println("The game starts!");


        boolean gameOver;
        int roundCounter = 0;

        do {
            printPlayerHUD(players[roundCounter % 2]);
            int[] shot = takeAShot(scanner);
            boolean didHit = players[(roundCounter+1) % 2].evaluateShot(shot);
            players[roundCounter % 2].alterFoggedBoard(shot, didHit);
            players[(roundCounter+1) % 2].alterBoard(shot, didHit);

            gameOver = players[(roundCounter+1) % 2].evaluateBoard();
            if (!gameOver) {
                changePlayer(scanner);
                roundCounter++;
            }
        } while (!gameOver);

        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public static void printPlayerHUD(Board activePlayer) {
        activePlayer.printFoggedBoard();
        System.out.println("-".repeat(21));
        activePlayer.printBoard();
    }

    public static void prepGame(Scanner scanner, Board player1, Board player2) {
        System.out.println("Player 1, place your ships on the game field");
        player1.printBoard();
        player1.requestShipPlacement(scanner);

        changePlayer(scanner);

        System.out.println("Player 2, place your ships on the game field");
        player2.printBoard();
        player2.requestShipPlacement(scanner);

        changePlayer(scanner);
    }

    public static void changePlayer(Scanner scanner) {
        System.out.println("Press Enter and pass the move to another player\n...");
        scanner.nextLine();
    }

    static int[] takeAShot(Scanner input) {
        System.out.println("Take a shot!");
        int[] playerShot;
        boolean correctShot;

        do {
            playerShot = Ship.convertCoordinatesToArray(input.nextLine());
            correctShot = checkCoordinates(playerShot);
            if (!correctShot) {
                System.out.println("Error! You entered the wrong coordinates!\nTry again:");
            }
        } while (!correctShot);
        return playerShot;
    }

    // checks if the shot coordinates are on the board
    private static boolean checkCoordinates(int[] checker) {
        return checker[0] > 0 && checker[0] <= gameSize && checker[1] > 0 && checker[1] <= gameSize;
    }
}
