import java.io.*;
import java.util.*;

public class Verschiebespiel {

    static int[][] board;
    static int size;
    static int moves;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        File saveDir = new File("saves");
        if (!saveDir.exists())
            saveDir.mkdir();

        // Load a game?
        File[] saves = saveDir.listFiles((d, n) -> n.endsWith(".txt"));
        if (saves != null && saves.length > 0) {
            System.out.print("Load a saved game? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                loadGame(sc, saves);
            } else {
                newGame(sc);
            }
        } else {
            newGame(sc);
        }

        // Game loop
        while (true) {
            printBoard();
            System.out.println("Moves: " + moves);
            System.out.print("Enter number to move or 's' to save & exit: ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("s")) {
                System.out.print("Save name: ");
                saveGame(sc.nextLine());
                System.out.println("Game saved. Exiting...");
                break;
            }

            try {
                int num = Integer.parseInt(input);
                if (!makeMove(num))
                    System.out.println("Invalid move!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }

            if (isGameOver()) {
                printBoard();
                System.out.println("Finished in " + moves + " moves!");
                System.out.print("Play again? (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    newGame(sc);
                } else {
                    break;
                }
            }
        }
    }

    /** ------------------- GAME SETUP ------------------- */

    static void newGame(Scanner sc) {
        System.out.print("Board size: ");
        size = Integer.parseInt(sc.nextLine());
        moves = 0;
        board = new int[size][size];
        initBoardRandom();
    }

    static void initBoardRandom() {
        int[] nums = new int[size * size];
        for (int i = 0; i < nums.length; i++)
            nums[i] = i;

        Random r = new Random();
        for (int i = nums.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            int t = nums[i];
            nums[i] = nums[j];
            nums[j] = t;
        }

        for (int i = 0, idx = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = nums[idx++];
    }

    /** ------------------- BOARD DISPLAY ------------------- */

    static void printBoard() {
        int maxVal = size * size - 1;
        int digits = Integer.toString(maxVal).length(); // for formatting

        for (int[] row : board) {
            for (int val : row) {
                if (val == 0)
                    System.out.print(" ".repeat(digits) + " "); // empty field
                else
                    System.out.print(String.format("%0" + digits + "d ", val));
            }
            System.out.println();
        }
        System.out.println();
    }

    /** ------------------- MOVES ------------------- */

    static boolean makeMove(int num) {
        int[] pos = getFieldIndex(num);
        int[] empty = getEmptyFieldIndex();
        if (pos == null)
            return false;

        if (isAdjacentFields(pos, empty)) {
            swapFields(pos, empty);
            moves++;
            return true;
        }
        return false;
    }

    static void swapFields(int[] a, int[] b) {
        int temp = board[a[0]][a[1]];
        board[a[0]][a[1]] = board[b[0]][b[1]];
        board[b[0]][b[1]] = temp;
    }

    static boolean isAdjacentFields(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) == 1;
    }

    static List<int[]> getAdjacentFields(int[] pos) {
        List<int[]> adj = new ArrayList<>();
        int x = pos[0], y = pos[1];
        if (x > 0)
            adj.add(new int[] { x - 1, y });
        if (x < size - 1)
            adj.add(new int[] { x + 1, y });
        if (y > 0)
            adj.add(new int[] { x, y - 1 });
        if (y < size - 1)
            adj.add(new int[] { x, y + 1 });
        return adj;
    }

    /** ------------------- CHECK WIN ------------------- */

    static boolean isGameOver() {
        int x = 1;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1)
                    return board[i][j] == 0;
                if (board[i][j] != x++)
                    return false;
            }
        return true;
    }

    /** ------------------- HELPERS ------------------- */

    static int[] getFieldIndex(int n) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] == n)
                    return new int[] { i, j };
        return null;
    }

    static int[] getEmptyFieldIndex() {
        return getFieldIndex(0);
    }

    /** ------------------- SAVE/LOAD ------------------- */

    static void saveGame(String name) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter("saves/" + name + ".txt"));
        pw.println(size);
        pw.println(moves);
        for (int[] row : board) {
            for (int v : row)
                pw.print(v + " ");
            pw.println();
        }
        pw.close();
    }

    static void loadGame(Scanner sc, File[] files) throws Exception {
        System.out.println("Available saves:");
        for (int i = 0; i < files.length; i++)
            System.out.println((i + 1) + ": " + files[i].getName().replace(".txt", ""));
        int choice = Integer.parseInt(sc.nextLine()) - 1;

        Scanner fsc = new Scanner(files[choice]);
        size = fsc.nextInt();
        moves = fsc.nextInt();
        board = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = fsc.nextInt();
        fsc.close();
    }
}
