package battleship;

public class Ship {
    private int length;
    private final int[][] endParts;
    private int[][] parts;
    private final boolean validShip;
    private final boolean[] partDestroyed;
    private boolean sunken;

    public Ship(String[] endPartsString, int boardSize) {
        this.endParts = convertCoordinatesToArray(endPartsString);
        // validate the ship
        this.validShip = validateShip(this.endParts, boardSize);
        if (this.validShip) {
            this.parts = constructShip(this.endParts, isHorizontal(this.endParts));
            this.length = parts.length;
        }
        this.partDestroyed = new boolean[length];
        this.sunken = false;
    }

    public int getLength() {
        return length;
    }

    // the IO part does not need to know the actual processing of the coordinates
    public String[] getParts() {
        return covertArrayToCoordinates(parts);
    }

    // for internal use
    int[][] getPartsArray() {
        return parts;
    }

    int[] getStartPart() {
        return parts[0];
    }

    public boolean isValidShip() {
        return validShip;
    }

    public int[][] getEndParts() {
        return endParts;
    }

    public boolean isSunken() {
        return sunken;
    }

    public void setPartDestroyed(int part) {
        this.partDestroyed[part] = true;
        if (!this.sunken) {
            int counter = 0;
            for (boolean partCheck : this.partDestroyed) {
                if (partCheck) {
                    counter++;
                }
            }
            if (counter == length) {
                this.sunken = true;
            }
        }
    }

    // converts an Array of strings with human-readable game coordinates into and 2-dim array
    static int[][] convertCoordinatesToArray(String[] parts) {
        int[][] arrayIndizes = new int[parts.length][2];
        for (int i = 0; i < 2; i++) {
            // solves the equation [ 'A' - 'CoordinateChar' = 1 - ArrayIndex ] after ArrayIndex
            // 'A' and 1 are the same reference point so the distance is calculated from both as fix points
            arrayIndizes[i][0] = 1 - ('A' - parts[i].charAt(0));
            // reads the number after the character and parses it to Int, -1 to make it an array coordinate
            arrayIndizes[i][1] = Integer.parseInt(parts[i].substring(1)); // no -1 needed because of the frame
        }
        return arrayIndizes;
    }

    // converts a String with one human-readable game coordinate into a pair of array coordinates
    static int[] convertCoordinatesToArray(String coordinate) {
        int[] arrayIndizes = new int[2];
        // as above, just for a single coordinate
        arrayIndizes[0] = 1 - ('A' - coordinate.charAt(0));
        arrayIndizes[1] = Integer.parseInt(coordinate.substring(1)); // no -1 needed because of the frame
        return arrayIndizes;
    }

    // converts the array back to the visible game coordinates
    static String[] covertArrayToCoordinates(int[][] parts) {
        String[] gameCoordinates = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            // yAxis needs to convert int to char with the same formula as above
            char yAxis = (char) (--parts[i][0] + 'A');
            int xAxis = (parts[i][1]);
            gameCoordinates[i] = yAxis + "" + xAxis ;
        }
        return gameCoordinates;
    }

    // checks if the min and max coordinates does not exceed the field
    private boolean validateShip(int[][] endParts, int boardSize) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int[] row : endParts) {
            for (int value : row) {
                if (value > max) {
                    max = value;
                }
                if (value < min) {
                    min = value;
                }
            }
        }
        if (max > boardSize - 1 || min < 1) {
            return false;
        }
        // also checks if all parts are in one row / in one column
        return endParts[0][0] == endParts[1][0] || endParts[0][1] == endParts[1][1];
    }

    // returns true, if the ship is horizontal and false, if it is vertical
    static boolean isHorizontal(int[][] endParts) {
        return endParts[0][0] == endParts[1][0];
    }

    // always builds the ship from the lower end point upwards
    private static int[][] constructShip(int[][] endParts, boolean horizontal) {
        int [][] ship;
        if (horizontal) { // y-axis stays the same, x-axis is increased by adding i to the start point
            int length = Math.abs(endParts[1][1] - endParts[0][1]) + 1;
            ship = new int[length][2];
            for (int i = 0; i < length; i++) {
                ship[i][0] = endParts[0][0];
                ship[i][1] = Math.min(endParts[1][1], endParts[0][1]) + i;
            }
        } else { // same as above, but x-axis stays the same, while y-axis increases
            int length = Math.abs(endParts[1][0] - endParts[0][0]) + 1;
            ship = new int[length][2];
            for (int i = 0; i < length; i++) {
                ship[i][1] = endParts[0][1];
                ship[i][0] = Math.min(endParts[1][0], endParts[0][0]) + i;
            }
        }
        return ship;
    }
}


