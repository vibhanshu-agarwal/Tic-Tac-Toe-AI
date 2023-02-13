package tictactoe;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static char aiPlayer = 'X';
    private static char humanPlayer = 'O';

    private static boolean DEBUG = false;

    public static void main(String[] args) {
        char[][] field = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };


        Scanner scanner = new Scanner(System.in);

        //Get the commands from the user
        String[] commands = new String[0];
        do {
            System.out.println("Input command: ");
            commands = scanner.nextLine().split(" ");
            if (commands[0].equals("exit")) break;
        } while (!Validator.isValidCommands(commands));

        //Print the empty grid
        printGameArray(field);

        String x;
        String y = "";
        //Check the game state
        GameState gameState = GameState.NOT_FINISHED;

        while (!commands[0].equals("exit") && gameState == GameState.NOT_FINISHED) {
            boolean result = commands[1].equals("user") ? makeMoveForUser(scanner,
                    field,
                    'X') : makeMoveForAI(
                    field,
                    'X',
                    commandToAILevel(commands[1]));

            //Check the game state
            gameState = GameState.checkGameState(field);

            if (gameState != GameState.NOT_FINISHED) break;

            result = commands[2].equals("user") ? makeMoveForUser(scanner,
                    field,
                    'O') : makeMoveForAI(
                    field,
                    'O',
                    commandToAILevel(commands[2]));

            //Check the game state
            gameState = GameState.checkGameState(field);

        }


        printGameState(gameState);

    }

    private static AILevel commandToAILevel(String command) {
        return AILevel.valueOf(command.toUpperCase());
    }

    private static boolean makeMoveForAI(char[][] field, char moveType, AILevel level) {

        //Set the AI player to the moveType
        aiPlayer = moveType;
        //Also, set the human player to the opposite moveType
        humanPlayer = moveType == 'X' ? 'O' : 'X';

        AtomicInteger x = new AtomicInteger(-1);
        AtomicInteger y = new AtomicInteger(-1);
        //Check the game state
        GameState gameState = GameState.NOT_FINISHED;

        switch (level) {
            case EASY:
                getAvailableRandomCoordinates(x,
                        y,
                        field,
                        gameState);

                if (gameState != GameState.NOT_FINISHED) return false;

                break;
            case MEDIUM:
//                If it already has two in a row and can win with one further move, it does so.
                if (foundWinningMove(x,
                        y,
                        field,
                        moveType)) break;
//                If its opponent can win with one move, it plays the move necessary to block this.
                if (foundWinningMove(x,
                        y,
                        field,
                        moveType == 'X' ? 'O' : 'X')) break; //Winning move for opponent


//                    Otherwise, it makes a random move
                getAvailableRandomCoordinates(x,
                        y,
                        field,
                        gameState);

                if (gameState != GameState.NOT_FINISHED) return false;

                break;
            case HARD:
                //Implement the minimax algorithm for the AI for Tic-tac-Toe
                Move move = calcBestMove(Arrays.copyOf(field,
                                field.length),
                        moveType);

                x.set(move.x);
                y.set(move.y);

                if (gameState != GameState.NOT_FINISHED) return false;

                break;
        }


        System.out.printf("Making move level \"%s\"",
                level.getValue());

        //A Computer makes move as "moveType"
        makeMove(field,
                String.valueOf(x),
                String.valueOf(y),
                moveType);

        printGameArray(field);
        return true;
    }

    private static Move calcBestMove(char[][] newBoard, char aiPlayer) {
        int bestScore = Integer.MIN_VALUE;
        Move move = new Move();
        move.x = -1;
        move.y = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (newBoard[i][j] == ' ') {
                    newBoard[i][j] = aiPlayer;
                    int score = minimax(newBoard,
                            0,
                            false);
                    newBoard[i][j] = ' ';
                    if (score > bestScore) {
                        bestScore = score;
                        move.x = i + 1;
                        move.y = j + 1;
                    }
                }
            }
        }
        move.score = bestScore;
        return move;
    }


    private static Integer minimax(char[][] newBoard, int depth, boolean isMaximizingPlayer) {
        Integer score = getScore(newBoard,
                depth);

        if (score != null) {
            //Print Game state
            if (DEBUG) {
                System.out.println("aiPlayer: " + aiPlayer + " humanPlayer: " + humanPlayer);
//                System.out.println("x: " + aMove.x + " y: " + aMove.y + " depth: " + depth + " player: " + player + " score: " + score);
                printGameArray(newBoard);
                printGameState(GameState.checkGameState(newBoard));
            }
            return score;
        }

        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (newBoard[i][j] == ' ') {
                        newBoard[i][j] = aiPlayer;
                        score = minimax(newBoard,
                                depth + 1,
                                false);
                        newBoard[i][j] = ' ';
                        bestScore = Math.max(score,
                                bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (newBoard[i][j] == ' ') {
                        newBoard[i][j] = humanPlayer;
                        score = minimax(newBoard,
                                depth + 1,
                                true);
                        newBoard[i][j] = ' ';
                        bestScore = Math.min(score,
                                bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private static Integer getScore(char[][] newBoard, int depth) {
        //Check the game state
        GameState gameState = GameState.checkGameState(newBoard);
        //return the score if the game is over
        //IMP: We assume that the AI player is 'X' and the human player is 'O'
        if (aiPlayer == 'X') {
            if (gameState == GameState.X_WINS) return 10 - depth;
            if (gameState == GameState.O_WINS) return depth - 10;
        } else if (aiPlayer == 'O') {
            if (gameState == GameState.X_WINS) return depth - 10;
            if (gameState == GameState.O_WINS) return 10 - depth;
        }
        if (gameState == GameState.DRAW) return 0;
        return null;
    }


    private static boolean foundWinningMove(AtomicInteger x, AtomicInteger y, char[][] field, char moveType) {
        //Check the game state
        GameState gameState = GameState.checkGameState(field);
        if (gameState == GameState.NOT_FINISHED) {
            //Check if the computer can win with one move
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field[i][j] == ' ') {
                        field[i][j] = moveType;
                        gameState = GameState.checkGameState(field);
                        if (gameState == GameState.X_WINS || gameState == GameState.O_WINS) {
                            x.set(i + 1);
                            y.set(j + 1);
                            field[i][j] = ' ';
                            return true;
                        }
                        field[i][j] = ' ';
                    }
                }
            }
        }
        return false;
    }

    private static void getAvailableRandomCoordinates(AtomicInteger x, AtomicInteger y, char[][] field, GameState gameState) {
        //Get random coordinates for the computer to make a move as "moveType"
        do {
            x.set((int) (Math.random() * 3 + 1));
            y.set((int) (Math.random() * 3 + 1));
            //Check the game state
            gameState = GameState.checkGameState(field);
        }
        while (!Validator.isValidCoordinates(field,
                String.valueOf(x.get()),
                String.valueOf(y.get())) && gameState == GameState.NOT_FINISHED);
    }

    private static boolean makeMoveForUser(Scanner scanner, char[][] field, char moveType) {
        String x;
        String y;

        //Check the game state
        GameState gameState = GameState.NOT_FINISHED;
        do {
            //User inputs the coordinates of the cell
            System.out.print("Enter the coordinates: ");
            x = scanner.next();
            //Check if the coordinates are valid numbers
            if (!x.matches("\\d+")) {
                System.out.println("You should enter numbers!");
                return true;
            }
            y = scanner.next();
            //Check the game state
            gameState = GameState.checkGameState(field);
        } while (!Validator.isValidCoordinates(field,
                x,
                y) && gameState == GameState.NOT_FINISHED);

        if (gameState != GameState.NOT_FINISHED) return false;
        //User makes move as "moveType"
        makeMove(field,
                x,
                y,
                moveType);

        printGameArray(field);
        return true;
    }


    private static void printGameArray(char[][] field) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }


    private static void makeMove(char[][] field, String x, String y, char player) {
        field[Integer.parseInt(x) - 1][Integer.parseInt(y) - 1] = player;
    }

    private static void printGameState(GameState gameState) {
        switch (gameState) {
            case X_WINS -> System.out.println("X wins");
            case O_WINS -> System.out.println("O wins");
            case DRAW -> System.out.println("Draw");
            case NOT_FINISHED -> System.out.println("Game not finished");
        }
    }
}
