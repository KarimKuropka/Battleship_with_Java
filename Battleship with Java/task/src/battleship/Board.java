package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Board {
    char[][] board;
    char[][] foggedBoard;
    private final int size;
    // list of ships on the board
    Ship[] ships;

    Board(int size) {
        // creates a frame of width 1 on each side
        size += 2;
        this.size = size;
        this.board = new char[size][size];
        this.foggedBoard = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '~';
                foggedBoard[i][j] = '~';
            }
        }
        this.ships = new Ship[5];
    }

    public int getSize() {
        return size;
    }

    public void printBoard() {
        StringBuilder columns = new StringBuilder("  ");
        // print the columns
        for (int i = 1; i <= size - 2; i++) { columns.append(i).append(" "); }
        System.out.println(columns);
        // prints the lines, leaves out the frame
        for (int i = 0; i < size - 2; i++ ) {
            char ch = (char) ('A' + i);
            StringBuilder line = new StringBuilder(ch + " ");
            for (int j = 1; j < size - 1; j++){
                line.append(board[i+1][j]).append(" ");
            }
            System.out.println(line);
        }
    }

    public void printFoggedBoard() {
        StringBuilder columns = new StringBuilder("  ");
        // print the columns
        for (int i = 1; i <= size - 2; i++) { columns.append(i).append(" "); }
        System.out.println(columns);
        // prints the lines, leaves out the frame
        for (int i = 0; i < size - 2; i++ ) {
            char ch = (char) ('A' + i);
            StringBuilder line = new StringBuilder(ch + " ");
            for (int j = 1; j < size - 1; j++){
                line.append(foggedBoard[i+1][j]).append(" ");
            }
            System.out.println(line);
        }
    }

    void requestShipPlacement(Scanner scanner) {
        int[] shipLengths = {5, 4, 3, 3, 2};
        String[] shipNames = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        for (int i = 0; i < 5; i++) {
            Ship ship;
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", shipNames[i], shipLengths[i]);
            boolean validShip;
            boolean surroundingsClear = false;
            do {
                String[] input = scanner.nextLine().split(" ");
                ship = new Ship(input, this.size);
                validShip = ship.isValidShip();
                if (validShip) {surroundingsClear = checkSurroundings(ship);}
                if (!validShip) {
                    System.out.println("Error! Wrong ship location! Try again:");
                } else if (!surroundingsClear) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                } else if (ship.getLength() != shipLengths[i]) {
                    System.out.printf("Error! Wrong Length of the %s! Try again:\n", shipNames[i]);
                } else {
                    // if all rules are met, the ship is placed on the board and the board gets printed
                    this.ships[i] = ship;
                    putShipOnBoard(ship);
                    printBoard();
                }
            // if the ship is not valid, its length is null so length comparison covers that
            } while (ship.getLength() != shipLengths[i] || !surroundingsClear);
        }
    }

    // checks surroundings of each ship part, returns true, if no other ship is near
    private boolean checkSurroundings(Ship ship) {
        int[][] parts = ship.getPartsArray();
        for (int[] part : parts) {
            if (
                    this.board[part[0] - 1][part[1]] == 'o'
                 || this.board[part[0] + 1][part[1]] == 'o'
                 || this.board[part[0]][part[1] + 1] == 'o'
                 || this.board[part[0]][part[1] - 1] == 'o'
            ) {
                return false;
            }
        }
        return true;
    }

    // enter the ship on the board replacing '~' with 'o'
    private void putShipOnBoard(Ship ship) {
        int[][] parts = ship.getPartsArray();
        for (int[] part : parts) {
            this.board[part[0]][part[1]] = 'o';
        }
    }



    boolean evaluateShot(int[] playerShot) {
        if (board[playerShot[0]][playerShot[1]] == 'o' || board[playerShot[0]][playerShot[1]] == 'x') {
            Ship target = applyShotToShip(playerShot);
            if (target.isSunken()) {
                System.out.println("You sank a ship!");
            } else {
                System.out.println("You hit a ship!");
            }
            return true;
        } else {
            System.out.println("You missed!");
            return false;
        }
    }



    private Ship applyShotToShip(int[] shot) {
        for (Ship ship : ships) {
            int[][] parts = ship.getPartsArray();
            for (int[] part : parts) {
                if(Arrays.equals(shot, part)) {
                    int[] start = ship.getStartPart();
                    ship.setPartDestroyed((Math.max(part[0] - start[0], part[1] - start[1])));
                    return ship;
                }
            }
        }
        throw new IllegalStateException("No ship found for shot: " + Arrays.toString(shot));
    }

    void alterBoard (int[] coordinates, boolean didHit) {
        if (didHit) {
            board[coordinates[0]][coordinates[1]] = 'x';
        } else {
            board[coordinates[0]][coordinates[1]] = 'M';
        }
    }

    void alterFoggedBoard (int[] coordinates, boolean didHit) {
        if (didHit) {
            foggedBoard[coordinates[0]][coordinates[1]] = 'x';
        } else {
            foggedBoard[coordinates[0]][coordinates[1]] = 'M';
        }
    }

    boolean evaluateBoard() {
        int counter = 0;
        for (Ship ship : ships) {
            if (ship.isSunken()) {
                counter++;
            }
        }
        return counter == 5;
    }
}
