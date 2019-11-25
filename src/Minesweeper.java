import java.util.Random;

public class Minesweeper {
    Minesweeper game;

    // -----DEFAULT LEVELS-----
    // Easy
    static final int EASY_MINES = 10;
    static final int EASY_ROWS = 8;
    static final int EASY_COLS = 8;
    // Medium
    static final int MED_MINES = 40;
    static final int MED_ROWS = 16;
    static final int MED_COLS = 16;
    // Hard
    static final int HARD_MINES = 99;
    static final int HARD_ROWS = 16;
    static final int HARD_COLS = 30;

    // Object attributes
    private int totalMines;
    private int numRows;
    private int numCols;
    private int wrongFlags;
    private int rightFlags;
    private int minesLeft;

    private int[][] grid;


    public Minesweeper() {
    // Constructor - Minesweeper Game
        this.game = game;
        // Begin in easy mode
        this.totalMines = EASY_MINES;
        this.minesLeft = EASY_MINES;
        this.numRows = EASY_ROWS;
        this.numCols = EASY_COLS;
        this.rightFlags = 0;
        this.wrongFlags = 0;

        this.grid = new int[numRows][numCols];

        // Start
        initializeGrid();
        startGUI();
    }

    public void initializeGrid() {
    // Assign mines and numbers to game grid
        System.out.println("FUNCTION CALL initializeGrid");
        // Randomly set mines
        Random randomNum = new Random();
        int mineCounter = 0;
        while (mineCounter < totalMines) {
            int randomRow = randomNum.nextInt(numRows);
            int randomCol = randomNum.nextInt(numCols);
            // Check if already a mine
            if (grid[randomRow][randomCol] != -1) {
                grid[randomRow][randomCol] = -1;
                mineCounter++;
            }
        }

        // Increment number for cells around mines
        for (int row=0; row < numRows; row++) {
            for (int col=0; col < numCols; col++) {
                // Check cells around mines to increment
                if (grid[row][col] == -1) {
                    for (int i=-1; i <= 1; i++) {
                        for(int j=-1; j <= 1; j++) {
                            int setRow = row + i;
                            int setCol = col + j;
                            // Check if cell is a mine or out of range
                            boolean canIncrement = setRow < numRows && setCol < numCols && setRow > -1 && setCol > -1;
                            if (canIncrement) {
                                if (grid[setRow][setCol] != -1) {
                                    grid[setRow][setCol]++;
                                }
                            }
                        }
                    }

                }
            }
        }
        printGrid();
    }

    public void printGrid(){
        System.out.println("-------------------------------");
        for (int row=0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print("|" + grid[row][col]);
            }
            System.out.println("");
        }
        System.out.println("-------------------------------");
    }

    public void startGUI() {
        System.out.println("FUNCTION CALL StartGUI");
    }

    public static void main(String[] args) {
        Minesweeper newGame = new Minesweeper();
    }
}
