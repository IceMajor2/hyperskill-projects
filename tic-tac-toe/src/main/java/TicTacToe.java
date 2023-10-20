
import java.util.Scanner;

public class TicTacToe {

    public static Scanner scanner = new Scanner(System.in);
    public static char[][] board = {
        {'_', '_', '_'},
        {'_', '_', '_'},
        {'_', '_', '_'},};

    public static void main(String[] args) {
        char player = 'X';
        while (gameState().equals("Game not finished")) {
            printBoard();
            nextTurn(player);
            player = player == 'X' ? 'O' : 'X';
        }
        printBoard();
        System.out.println(gameState());
    }

    public static void nextTurn(char player) {
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] nums = input.split(" ");
            int x = -1, y = -1;
            try {
                x = Integer.valueOf(nums[0]);
                y = Integer.valueOf(nums[1]);
            } catch (Exception e) {
                System.out.println("You should enter numbers!");
                continue;
            }
            if (x < 1 || x > 3 || y < 1 || y > 3) {
                System.out.println("Coordinates should be from 1 to 3");
                continue;
            }
            char content = board[x - 1][y - 1];
            if (content != '_') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }
            board[x - 1][y - 1] = player;
            break;
        }
    }

    public static void printBoard() {
        System.out.println("---------");
        for (char[] row : board) {
            System.out.print("| ");
            for (char ch : row) {
                System.out.print(ch + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public static String gameState() {
        int o = 0, x = 0;
        for (char[] row : board) {
            for (char ch : row) {
                if (ch == 'O') {
                    o++;
                } else if (ch == 'X') {
                    x++;
                }
            }
        }
        if (Math.abs(x - o) >= 2) {
            return "Impossible";
        }
        int diagonal = diagonalWin();
        int horizontal = horizontalWin();
        int vertical = verticalWin();

        if (horizontal == -1 || vertical == -1) {
            return "Impossible";
        }
        if (horizontal > 0) {
            return Character.toString(horizontal) + " wins";
        }
        if (vertical > 0) {
            return Character.toString(vertical) + " wins";
        }
        if (diagonal > 0) {
            return Character.toString(diagonal) + " wins";
        }
        if (x + o == 9) {
            return "Draw";
        }
        return "Game not finished";
    }

    public static int diagonalWin() {
        char winner = board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '_'
                ? board[0][0]
                : board[0][2] == board[1][1] && board[2][0] == board[1][1] && board[0][2] != '_'
                        ? board[0][2] : '\0';
        return winner;
    }

    public static int horizontalWin() {
        int win = 0;
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '_') {
                if (win != 0) {
                    return -1;
                }
                win = board[i][0];
            }
        }
        return win;
    }

    public static int verticalWin() {
        int win = 0;
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '_') {
                if (win != 0) {
                    return -1;
                }
                win = board[0][i];
            }
        }
        return win;
    }
}