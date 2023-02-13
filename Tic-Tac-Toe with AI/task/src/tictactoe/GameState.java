package tictactoe;

import java.util.HashMap;
import java.util.Map;

public enum GameState {
    NOT_FINISHED,
    DRAW,
    X_WINS,
    O_WINS;

    public static GameState checkGameState(char[][] field) {
        GameState gameState = GameState.NOT_FINISHED;

        Map<String, Integer> playerMovesCount = updateMoveCounts(field);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameState = checkIsPlayerWinner(field, gameState, 'X', i, j);
                if(gameState == GameState.X_WINS)
                    return gameState;
                gameState = checkIsPlayerWinner(field, gameState, 'O', i, j);
                if(gameState == GameState.O_WINS)
                    return gameState;
                gameState = playerMovesCount.get("_") == 0 ?  GameState.DRAW :  GameState.NOT_FINISHED;

            }
        }

        return gameState;
    }

    private static Map<String, Integer> updateMoveCounts(char[][] field) {
        Map<String, Integer> playerMovesCount = new HashMap<>();
        playerMovesCount.put("X", 0);
        playerMovesCount.put("O", 0);
        playerMovesCount.put("_", 0);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == 'X') {
                    playerMovesCount.put("X", playerMovesCount.get("X") + 1);
                } else if (field[i][j] == 'O') {
                    playerMovesCount.put("O", playerMovesCount.get("O") + 1);
                } else {
                    playerMovesCount.put("_", playerMovesCount.get("_") + 1);
                }
            }
        }
        return playerMovesCount;
    }

    private static GameState checkIsPlayerWinner(char[][] field, GameState gameState, char playerChar, int i, int j) {

        //Check if X wins by three across the board
        if (isValidAcrossCoords(j) && field[i][j] == playerChar && field[i][j + 1] == playerChar && field[i][j + 2] == playerChar) {
            gameState = playerChar == 'X' ? GameState.X_WINS : GameState.O_WINS;
        }
        //Check if X wins by three down the board
        if (isValidDownCoords(i) && field[i][j] == playerChar && field[i + 1][j] == playerChar && field[i + 2][j] == playerChar) {
            gameState = playerChar == 'X' ? GameState.X_WINS : GameState.O_WINS;
        }
        //Check if X wins by three down diagonally
        if (isValidDiagonalDownCoords(i, j) && field[i][j] == playerChar && field[i + 1][j + 1] == playerChar && field[i + 2][j + 2] == playerChar) {
            gameState = playerChar == 'X' ? GameState.X_WINS : GameState.O_WINS;
        }
        //Check if X wins by three ups diagonally
        if (isValidDiagonalUpCoords(i, j) && field[i][j] == playerChar && field[i - 1][j + 1] == playerChar && field[i - 2][j + 2] == playerChar) {
            gameState = playerChar == 'X' ? GameState.X_WINS : GameState.O_WINS;
        }
        return gameState;
    }

    private static boolean isValidAcrossCoords(int j) {
        return j + 2 <= 2;
    }

    private static boolean isValidDownCoords(int i) {
        return i + 2 <= 2;
    }

    private static boolean isValidDiagonalDownCoords(int i, int j) {
        return i + 2 <= 2 && j + 2 <= 2;
    }

    private static boolean isValidDiagonalUpCoords(int i, int j) {
        return i - 2 >= 0 && j + 2 <= 2;
    }

}
