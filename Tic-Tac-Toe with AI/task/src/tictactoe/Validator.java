package tictactoe;

public class Validator {

    public static boolean isValidCoordinates(char[][] field, String x, String y) {
        //Check if the coordinates are valid numbers
        if (!x.matches("\\d+") || !y.matches("\\d+")) {
            System.out.println("You should enter numbers!");
            return false;
        }
        int xCoord = Integer.parseInt(x);
        int yCoord = Integer.parseInt(y);
        //Check if the coordinates are valid
        if (xCoord < 1 || xCoord > 3 || yCoord < 1 || yCoord > 3) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }
        if (field[xCoord - 1][yCoord - 1] == 'X' || field[xCoord - 1][yCoord - 1] == 'O') {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
        return true;
    }

    public static boolean isValidCommands(String[] commands) {
        if (commands.length != 3) {
            System.out.println("Bad parameters!");
            return false;
        }
        if (!commands[0].equals("start") || (!commands[1].equals("easy") && !commands[1].equals("user") && !commands[1].equals("medium") && !commands[1].equals("hard")
                || (!commands[2].equals("easy") && !commands[2].equals("user") && !commands[2].equals("medium") && !commands[2].equals("hard")))) {
            System.out.println("Bad parameters!");
            return false;
        }
        return true;
    }

}
