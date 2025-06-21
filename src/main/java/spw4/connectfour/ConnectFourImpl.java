package spw4.connectfour;

public class ConnectFourImpl implements ConnectFour {
    private char[][] board;
    private Player player;

    public ConnectFourImpl(Player playerOnTurn) {
        this.player = playerOnTurn;
        this.board = new char[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = '.';
            }
        }
    }

    public Player getPlayerAt(int row, int col) {
        char possiblePlayer = board[row][col];
        if (possiblePlayer == 'R') {
            return Player.red;
        } else if (possiblePlayer == 'Y') {
            return Player.yellow;
        }
        return Player.none;
    }

    public Player getPlayerOnTurn() {
        return player;
    }

    public boolean isGameOver() {
        return hasHorizontalWin() || hasVerticalWin() || hasDiagonalWin();
    }

    private boolean hasHorizontalWin() {
        int cols = board[0].length;

        for (char[] row : board) {
            for (int col = 0; col <= cols - 4; col++) {
                if (row[col] != '.') {
                    char current = row[col];
                    if (current == row[col + 1] &&
                            current == row[col + 2] &&
                            current == row[col + 3]) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean hasDiagonalWin() {
        int rows = board.length;
        int cols = board[0].length;

        // Positive slope (/)
        for (int row = rows - 1; row >= 3; row--) {
            for (int col = 0; col <= cols - 4; col++) {
                if (board[row][col] != '.') {
                    char current = board[row][col];
                    if (current == board[row - 1][col + 1] &&
                            current == board[row - 2][col + 2] &&
                            current == board[row - 3][col + 3]) {
                        return true;
                    }
                }
            }
        }

        // Negative slope (\)
        for (int row = rows - 1; row >= 3; row--) {
            for (int col = 3; col < cols; col++) {
                if (board[row][col] != '.') {
                    char current = board[row][col];
                    if (current == board[row - 1][col - 1] &&
                            current == board[row - 2][col - 2] &&
                            current == board[row - 3][col - 3]) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean hasVerticalWin() {
        int rows = board.length;
        int cols = board[0].length;

        for (int col = 0; col < cols; col++) {
            for (int row = rows - 1; row >= 3; row--) {
                if (board[row][col] != '.') {
                    char current = board[row][col];
                    if (current == board[row - 1][col] &&
                            current == board[row - 2][col] &&
                            current == board[row - 3][col]) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Player getWinner() {
        if (isGameOver()) {
            return player == Player.red ? Player.yellow : Player.red;
        }
        return Player.none;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player: ");
        if (player == Player.red) {
            sb.append("RED");
        } else if (player == Player.yellow) {
            sb.append("YELLOW");
        } else {
            sb.append("NONE");
        }
        sb.append("\n");
        for (char[] chars : board) {
            sb.append("|");
            for (char aChar : chars) {
                sb.append(' ').append(aChar).append(' ');
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    public void reset(Player playerOnTurn) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = '.';
            }
        }
        this.player = playerOnTurn;
    }

    public void drop(int col) {
        int i = 5;
        while (i >= 0) {
            if (board[i][col] == '.') {
                board[i][col] = player == Player.red ? 'R' : 'Y';
                player = player == Player.red ? Player.yellow : Player.red;
                return;
            }
            i--;
        }
    }
}
