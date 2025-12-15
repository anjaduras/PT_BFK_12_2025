
// Java.io für File Operationen, java.util fürs Scanner
import java.io.*;
import java.util.*;

public class Verschiebespiel {
    // NTS: static means these variables belong to the CLASS, not individual object
    static int[][] board;
    static int size;
    static int moves;

    public static void main(String[] args) throws Exception {
        // throwing exception if, f.e., the file is not found!
        Scanner sc = new Scanner(System.in);
        File saveDir = new File("saves");
        if (!saveDir.exists())
            saveDir.mkdir();

        // Wenn es schon Spiele gibt, nach Laden fragen
        File[] saves = saveDir.listFiles((d, n) -> n.endsWith(".txt"));
        if (saves != null && saves.length > 0) {
            System.out.print("Gespeichertes Spiel laden? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                loadGame(sc, saves);
            } else {
                newGame(sc);
            }
        } else {
            newGame(sc);
        }

        // SPIELSCHLEIFE (main functionality)
        while (true) {
            printBoard();
            System.out.println("Spielzüge verwendet: " + moves);
            System.out
                    .print("Gib die Nummer ein, die du verschieben möchtest, oder „s“, um zu speichern & zu beenden: ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("s")) {
                System.out.print("Name speichern: ");
                saveGame(sc.nextLine());
                System.out.println("Spiel gespeichert! :) Beenden...");
                break;
            }

            try {
                int num = Integer.parseInt(input);
                // parseInt converts a string into integer!
                if (!makeMove(num))
                    System.out.println("Ungültiger Zug! :(");
                // makeMove method that tries to move the tile with number num
            } catch (NumberFormatException e) {
                System.out.println("Ungültiger Zug! :(");
            }

            if (isGameOver()) {
                printBoard();
                System.out.println("Beendet mit " + moves + " Zügen! Glückwunsch! :)");
                System.out.print("Nochmal spielen? (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    newGame(sc);
                } else {
                    break;
                }
            }
        }
    }

    static void newGame(Scanner sc) {
        System.out.print("Brettgröße: ");
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

    static void printBoard() {
        int maxVal = size * size - 1;
        int digits = Integer.toString(maxVal).length(); // Formatierung für Board 4 und größer
        for (int[] row : board) {
            for (int val : row) {
                if (val == 0)
                    System.out.print(" ".repeat(digits) + " "); // leeres Feld
                else
                    System.out.print(String.format("%0" + digits + "d ", val));
            }
            System.out.println();
        }
        System.out.println();
    }

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
        System.out.println("Verfügbare Spielstände:");
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
