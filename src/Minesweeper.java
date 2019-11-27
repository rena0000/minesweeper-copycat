import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
    // --------DEFAULT LEVELS--------
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

    // -------------GAME-------------
    Minesweeper game;
    private int totalMines;
    private int numRows;
    private int numCols;
    private int numFlags;
    private int wrongFlags;
    private int rightFlags;
    private int minesLeft;
    // Grid
    private int[][] numGrid;
    private MineCell[][] cellGrid;

    // --------------GUI-------------
    // Frames
    private JFrame gameWindow = new JFrame("Minesweeper Copycat - Serena He");
    // Panels
    private JPanel buttonPanel = new JPanel();
    private JPanel scorePanel = new JPanel();
    private JPanel levelPanel = new JPanel();
    private JPanel finalPanel = new JPanel();
    // Layout
    private GridLayout mineLayout;
    // Labels
    private JLabel timerLabel;
    private JLabel minesLeftLabel;

    public Minesweeper() {
    // Constructor - Minesweeper Game
        this.game = game;
        // Begin in easy mode
        this.totalMines = EASY_MINES;
        this.minesLeft = EASY_MINES;
        this.numRows = EASY_ROWS;
        this.numCols = EASY_COLS;
        this.numFlags = 0;
        this.rightFlags = 0;
        this.wrongFlags = 0;

        // Init grids
        this.numGrid = new int[numRows][numCols];
        this.cellGrid = new MineCell[numRows][numCols];
        initializeGrid();

        // GUI
        startGUI();
    }

    public void initializeGrid() {
        System.out.println("FUNCTION CALL initializeGrid");

        // Randomly set mines in grid shell
        Random randomNum = new Random();
        int mineCounter = 0;
        while (mineCounter < totalMines) {
            int randomRow = randomNum.nextInt(numRows);
            int randomCol = randomNum.nextInt(numCols);
            // Check if already a mine
            if (numGrid[randomRow][randomCol] != -1) {
                numGrid[randomRow][randomCol] = -1;
                mineCounter++;
            }
        }

        // Increment number for cells around mines
        for (int row=0; row < numRows; row++) {
            for (int col=0; col < numCols; col++) {
                // Check cells around mines to increment
                if (numGrid[row][col] == -1) {
                    for (int i=-1; i <= 1; i++) {
                        for(int j=-1; j <= 1; j++) {
                            int setRow = row + i;
                            int setCol = col + j;
                            // Check if cell is a mine or out of range
                            boolean canIncrement = setRow < numRows && setCol < numCols && setRow > -1 && setCol > -1;
                            if (canIncrement) {
                                if (numGrid[setRow][setCol] != -1) {
                                    numGrid[setRow][setCol]++;
                                }
                            }
                        }
                    }

                }
            }
        }

        // Create MineCells for each value in grid
        for (int row=0; row < numRows; row++) {
            for (int col=0; col < numRows; col++) {
                int value = numGrid[row][col]; // Get value
                MineCell cell = new MineCell(value); // Construct
                cellGrid[row][col] = cell; // Assign cell to grid
            }
        }
        printGrid();
    }

    public void printGrid(){
        System.out.println("-------------------------------");
        for (int row=0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print("|" + numGrid[row][col]);
            }
            System.out.println("");
        }
        System.out.println("-------------------------------");
    }

    public void startGUI() {
        // Dimensions
        int windowWidth = numCols*50;
        int windowHeight = numRows*50 + 100;
        int gridWidth = windowWidth;
        int gridHeight = numRows*50;
        int levelsHeight = 50;

        // Game window
        gameWindow.setSize(windowWidth, windowHeight);

        // Level Panel
        Dimension levelButtonDimension = new Dimension((windowWidth)/3, levelsHeight);
        Button easyButton = new Button("Easy");
        Button mediumButton = new Button("Medium");
        Button hardButton = new Button("Hard");
        // Set level button size
        easyButton.setPreferredSize(levelButtonDimension);
        mediumButton.setPreferredSize(levelButtonDimension);
        hardButton.setPreferredSize(levelButtonDimension);
        // Set level button font
        easyButton.setFont(new Font("Arial", Font.PLAIN, 20));
        mediumButton.setFont(new Font("Arial", Font.PLAIN, 20));
        hardButton.setFont(new Font("Arial", Font.PLAIN, 20));
        // Add mouseListener to buttons
        easyButton.addMouseListener(new MineMouseHandler(this));
        mediumButton.addMouseListener(new MineMouseHandler(this));
        hardButton.addMouseListener(new MineMouseHandler(this));
        // Add button to levelPanel
        levelPanel.add(easyButton);
        levelPanel.add(mediumButton);
        levelPanel.add(hardButton);

        // Format buttonPanel
        buttonPanel.setSize(gridWidth, gridHeight);
        buttonPanel.setLayout(new GridLayout(numRows, numCols));

    }

    public static void main(String[] args) {
        Minesweeper newGame = new Minesweeper();
    }
}
