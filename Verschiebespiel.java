import java.util.Random;
import java.util.Scanner;

public class Verschiebespiel {

    static int[][] board = new int[3][3];
    static int moves = 0;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        initBoardRandom();

        while (true) {
            printBoard();

            System.out.print("Enter number: ");
            int n = sc.nextInt();

            if (makeMove(n)) {
                moves++;
                System.out.println("Moves: " + moves);
            } else {
                System.out.println("Invalid move");
            }

            if (isGameOver()) {
                printBoard();
                System.out.println("Finished!");
                break;
            }
        }
    }

    // Gibt das Spielbrett aus
    static void printBoard() {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0)
                    System.out.print("  ");
                else
                    System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Erzeugt eine zufällige Startstellung
    static void initBoardRandom() {
        int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 0 };
        Random rand = new Random();

        for (int i = numbers.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }

        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = numbers[index++];
            }
        }
    }

    // Führt einen Spielzug aus
    static boolean makeMove(int number) {

        int[] numberPos = getFieldIndex(number);
        int[] emptyPos = getEmptyFieldIndex();

        if (numberPos == null)
            return false;

        if (isAdjacentFields(numberPos, emptyPos)) {
            swapFields(numberPos, emptyPos);
            return true;
        }

        return false;
    }

    // Tauscht zwei Felder
    static void swapFields(int[] a, int[] b) {
        int temp = board[a[0]][a[1]];
        board[a[0]][a[1]] = board[b[0]][b[1]];
        board[b[0]][b[1]] = temp;
    }

    // Prüft, ob zwei Felder benachbart sind
    static boolean isAdjacentFields(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) == 1;
    }

    // Ermittelt alle Nachbarfelder (optional, aber gefordert)
    static int[][] getAdjacentFields(int row, int col) {

        int[][] neighbors = new int[4][2];
        int count = 0;

        if (row > 0)
            neighbors[count++] = new int[] { row - 1, col };
        if (row < 2)
            neighbors[count++] = new int[] { row + 1, col };
        if (col > 0)
            neighbors[count++] = new int[] { row, col - 1 };
        if (col < 2)
            neighbors[count++] = new int[] { row, col + 1 };

        int[][] result = new int[count][2];
        for (int i = 0; i < count; i++)
            result[i] = neighbors[i];

        return result;
    }

    // Liefert die Position eines Feldes
    static int[] getFieldIndex(int value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == value)
                    return new int[] { i, j };
            }
        }
        return null;
    }

    // Liefert die Position des leeren Feldes
    static int[] getEmptyFieldIndex() {
        return getFieldIndex(0);
    }

    // Prüft, ob das Spiel beendet ist
    static boolean isGameOver() {
        int expected = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 2 && j == 2)
                    return board[i][j] == 0;
                if (board[i][j] != expected++)
                    return false;
            }
        }
        return true;
    }
}
