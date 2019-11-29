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
    // Level
    String gameLevel;

    // -------------GAME-------------
    Minesweeper game;
    private int totalMines;
    private int numRows;
    private int numCols;
    private int numFlags;
    private int wrongFlags;
    private int rightFlags;
    private int minesLeft;
    private boolean gameLost;
    // Grid
    private int[][] numGrid;
    MineCell[][] cellGrid;

    // --------------GUI-------------
    // Frames
    private JFrame gameWindow = new JFrame("Minesweeper Copycat - Serena He");
    // Panels
    private JPanel gridPanel = new JPanel();
    private JPanel statusPanel = new JPanel();
    private JPanel levelPanel = new JPanel();
    private JPanel finalPanel = new JPanel();
    // Layout

    // Labels
    private JLabel minesLeftLabel = new JLabel();
    private JLabel timeElapsedLabel = new JLabel();

    // Buttons
    private JButton easyButton = new JButton("Easy");
    private JButton mediumButton = new JButton("Medium");
    private JButton hardButton = new JButton("Hard");
    private JButton resetButton = new JButton();

    public Minesweeper() {
    // Constructor - Minesweeper Game
        // Begin in easy mode
        this.totalMines = EASY_MINES;
        this.minesLeft = EASY_MINES;
        this.numRows = EASY_ROWS;
        this.numCols = EASY_COLS;
        gameLevel = "easy";
        gameLost = false;
        // Flags
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

    private void initializeGrid() {
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
                            boolean inRange = setRow < numRows && setCol < numCols && setRow > -1 && setCol > -1;
                            if (inRange) {
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
                MineCell cell = new MineCell(this, value, row, col); // Construct
                cellGrid[row][col] = cell; // Assign cell to grid
            }
        }
        printGrid();
    }

    public int getGameRows() {
        return numRows;
    }

    public int getGameCols() {
        return numCols;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public void loseGame() {
        gameLost = true;
        resetButton.setIcon(MineCell.LOSE_FACE);
    }

    public void winGame() {
        resetButton.setIcon(MineCell.WIN_FACE);
    }

    public void resetGame() {
        initializeGrid();

    }

    private void printGrid(){
        System.out.println("-------------------------------");
        for (int row=0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print("|" + numGrid[row][col]);
            }
            System.out.println("");
        }
        System.out.println("-------------------------------");
    }

    private void startGUI() {
        // -----DIMENSIONS-----
        // Game window
        int windowWidth = numCols*MineCell.CELL_WIDTH;
        int windowHeight = (numRows+3)*MineCell.CELL_WIDTH;
        Dimension gameWindowDimension = new Dimension(windowWidth, windowHeight);
        System.out.println("Window dimensions " + windowWidth + ", " + windowHeight);
        // Status
        int statusPanelHeight = MineCell.CELL_WIDTH;
        Dimension statusPanelDimension = new Dimension(windowWidth, statusPanelHeight);
        System.out.println("Status dimensions " + windowWidth + ", " + statusPanelHeight);
        // Cell grid
        int gridPanelHeight = numRows*MineCell.CELL_WIDTH;
        Dimension gridPanelDimension = new Dimension(windowWidth, gridPanelHeight);
        System.out.println("Grid dimensions " + windowWidth + ", " + gridPanelHeight);
        // Levels
        int levelButtonWidth = 100;
        int levelPanelHeight = MineCell.CELL_WIDTH;
        int levelButtonFontSize = 14;
        Dimension levelButtonDimension = new Dimension(levelButtonWidth, levelPanelHeight);
        Dimension levelPanelDimension = new Dimension(windowWidth, levelPanelHeight);
        System.out.println("Level dimensions " + windowWidth + ", " + levelPanelHeight);
        System.out.println("Level button dimensions " + levelButtonWidth + ", " + levelPanelHeight);

        // -----GAME WINDOW-----
        gameWindow.setSize(gameWindowDimension);
        finalPanel.setSize(gameWindowDimension);

        // -----LEVEL PANEL-----
        // Panel
        levelPanel.setPreferredSize(levelPanelDimension);
        levelPanel.setMaximumSize(levelPanel.getPreferredSize());
        levelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        // Button name
        easyButton.setName("level");
        mediumButton.setName("level");
        hardButton.setName("level");
        // Button size
        easyButton.setPreferredSize(levelButtonDimension);
        mediumButton.setPreferredSize(levelButtonDimension);
        hardButton.setPreferredSize(levelButtonDimension);
        // Button color
        easyButton.setBackground(Color.LIGHT_GRAY);
        mediumButton.setBackground(Color.LIGHT_GRAY);
        hardButton.setBackground(Color.LIGHT_GRAY);
        // Button text
        easyButton.setFont(new Font("Arial", Font.PLAIN, levelButtonFontSize));
        mediumButton.setFont(new Font("Arial", Font.PLAIN, levelButtonFontSize));
        hardButton.setFont(new Font("Arial", Font.PLAIN, levelButtonFontSize));
        // Add mouseListener to buttons
        easyButton.addMouseListener(new MineMouseHandler(this));
        mediumButton.addMouseListener(new MineMouseHandler(this));
        hardButton.addMouseListener(new MineMouseHandler(this));
        // Add button to levelPanel
        levelPanel.add(easyButton);
        levelPanel.add(mediumButton);
        levelPanel.add(hardButton);

        // -----STATUS PANEL-----
        // Status panel
        statusPanel.setPreferredSize(statusPanelDimension);
        statusPanel.setMaximumSize(statusPanel.getPreferredSize());
        statusPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        // Reset button
        resetButton.setPreferredSize(MineCell.CELL_DIMENSION);
        resetButton.setIcon(MineCell.SMILEY);
        resetButton.setName("reset");
        // Timer

        // Mines left

        // Add status to panel
        statusPanel.add(resetButton);

        // -----BUTTON PANEL-----
        // Format
        gridPanel.setPreferredSize(gridPanelDimension);
        gridPanel.setMaximumSize(gridPanel.getPreferredSize());
        gridPanel.setLayout(new GridLayout(numRows, numCols, 0, 0));
        // Add MineCells
        for (int row=0; row < numRows; row++) {
            for (int col=0; col < numCols; col++) {
                gridPanel.add(cellGrid[row][col]);
            }
        }

        // -----START-----
        finalPanel.add(levelPanel);
        finalPanel.add(statusPanel);
        finalPanel.add(gridPanel);
        finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));

        gameWindow.setSize(gameWindowDimension);
        gameWindow.add(finalPanel);

        // Repaint panels and frame
//        gridPanel.repaint();
//        statusPanel.repaint();
//        levelPanel.repaint();
//        finalPanel.repaint();
//        gameWindow.repaint();

        // Frame format
        Dimension windowDim = Toolkit.getDefaultToolkit().getScreenSize();
        gameWindow.setLocation(windowDim.width/2-gameWindow.getSize().width/2, windowDim.height/2-gameWindow.getSize().height/2);
        // Open frame
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("-------------------------------");
    }

    public static void main(String[] args) {
        Minesweeper newGame = new Minesweeper();
    }
}
