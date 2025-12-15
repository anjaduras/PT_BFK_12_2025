import java.util.Random;
import java.util.Scanner;

public class Verschiebespiel {

    static int[][] board = new int[3][3];
    static int moves = 0;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Create a random start board
        generateRandomBoard();

        while (true) {
            printBoard();

            System.out.print("Enter number: ");
            int n = sc.nextInt();

            if (move(n)) {
                moves++;
                System.out.println("Moves: " + moves);
            } else {
                System.out.println("Invalid move");
            }

            if (isFinished()) {
                printBoard();
                System.out.println("Finished!");
                break;
            }
        }
    }

    // Create random board
    static void generateRandomBoard() {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 0};
        Random random = new Random();

        // Shuffle numbers
        for (int i = numbers.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }

        // Fill the board with the shuffled numbers
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = numbers[index++];
            }
        }
    }

    // Print the board (0 = empty space)
    static void printBoard() {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0)
                    System.out.print("  "); // empty space
                else
                    System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Move the selected number
    static boolean move(int n) {

        int nr = -1, nc = -1, er = -1, ec = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == n) { nr = i; nc = j; }
                if (board[i][j] == 0) { er = i; ec = j; }
            }
        }

        // Check if the selected number is next to the empty space
        if (Math.abs(nr - er) + Math.abs(nc - ec) == 1) {
            board[er][ec] = n;
            board[nr][nc] = 0;
            return true;
        }
        return false;
    }

    // Check if the game is finished
    static boolean isFinished() {
        return board[0][0] == 1 && board[0][1] == 2 && board[0][2] == 3 &&
               board[1][0] == 4 && board[1][1] == 5 && board[1][2] == 6 &&
               board[2][0] == 7 && board[2][1] == 8 && board[2][2] == 0;
    }
}
