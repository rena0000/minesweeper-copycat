import java.awt.*;
import java.util.Arrays;
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
    private int gameLevel;
    public static int EASY_LEVEL = 1;
    public static int MEDIUM_LEVEL = 2;
    public static int HARD_LEVEL = 3;

    // -------------GAME-------------
    Minesweeper game;
    private int totalMines;
    private int numRows;
    private int numCols;
    private int numFlags;
    private int numOpened;
    private int minesLeft;
    private boolean gameLost;
    // Grid
    private int[][] numGrid;
    MineCell[][] cellGrid;

    // --------------GUI-------------
    // Dimensions
    private static Dimension SCREEN_DIM = Toolkit.getDefaultToolkit().getScreenSize();
    private Dimension gameWindowDimension = new Dimension();
    private Dimension statusPanelDimension = new Dimension();
    private Dimension levelButtonDimension = new Dimension();
    private Dimension levelPanelDimension = new Dimension();
    private Dimension gridPanelDimension = new Dimension();
    // Frames
    private JFrame gameWindow = new JFrame("Minesweeper Copycat - Serena He");
    // Panels
    private JPanel gridPanel = new JPanel();
    private JPanel statusPanel = new JPanel();
    private JPanel levelPanel = new JPanel();
    private JPanel finalPanel = new JPanel();

    // Labels
    private JLabel minesLeftLabel = new JLabel();
    private JLabel timeElapsedLabel = new JLabel();

    // Buttons
    private static int LEVEL_BUTTON_FONT = 14;
    private static int LEVEL_BUTTON_WIDTH = 100;
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
        // Flags
        gameLevel = EASY_LEVEL;
        gameLost = false;
        numFlags = 0;
        numOpened = 0;
        // Init grids
        this.numGrid = new int[numRows][numCols];
        this.cellGrid = new MineCell[numRows][numCols];

        // -----LEVEL BUTTONS-----
        // Button name
        easyButton.setName("level");
        mediumButton.setName("level");
        hardButton.setName("level");
        // Button text
        easyButton.setFont(new Font("Arial", Font.PLAIN, LEVEL_BUTTON_FONT));
        mediumButton.setFont(new Font("Arial", Font.PLAIN, LEVEL_BUTTON_FONT));
        hardButton.setFont(new Font("Arial", Font.PLAIN, LEVEL_BUTTON_FONT));
        // Button color
        easyButton.setBackground(Color.GRAY); // Current selected
        mediumButton.setBackground(Color.LIGHT_GRAY);
        hardButton.setBackground(Color.LIGHT_GRAY);

        // -----ADD MOUSELISTENER-----
        easyButton.addMouseListener(new MineMouseHandler(this));
        mediumButton.addMouseListener(new MineMouseHandler(this));
        hardButton.addMouseListener(new MineMouseHandler(this));
        resetButton.addMouseListener(new MineMouseHandler(this));

        // Begin
        initializeGrid();
        startGUI();
    }

    private void setDimensions() {
        // Game window
        int windowWidth = numCols*MineCell.CELL_WIDTH;
        int windowHeight = (numRows+3)*MineCell.CELL_WIDTH;
        gameWindowDimension.setSize(windowWidth, windowHeight);
        statusPanelDimension.setSize(windowWidth, MineCell.CELL_WIDTH);
        gridPanelDimension.setSize(windowWidth, numRows*MineCell.CELL_WIDTH);
        levelButtonDimension.setSize(LEVEL_BUTTON_WIDTH, MineCell.CELL_WIDTH);
        levelPanelDimension.setSize(windowWidth, MineCell.CELL_WIDTH);
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
            for (int col=0; col < numCols; col++) {
                int value = numGrid[row][col]; // Get value
                MineCell cell = new MineCell(this, value, row, col);
                cellGrid[row][col] = cell; // Assign cell to grid
            }
        }
        printGrid();
    }

    public int getGameLevel() {
        return gameLevel;
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

    public void incrementFlags() {
        numFlags++;
    }

    public void decrementFlags() {
        numFlags--;
    }

    public void incrementOpened() {
        numOpened++;
    }

    public void loseGame() {
        gameLost = true;
        resetButton.setIcon(MineCell.LOSE_FACE);
    }

    public void winGame() {
        resetButton.setIcon(MineCell.WIN_FACE);
    }

    public void resetGame() {
        System.out.println("-------RESET-------");
        // Re-init grids
        numGrid = new int[numRows][numCols];
        cellGrid = new MineCell[numRows][numCols];
        gridPanel.removeAll();
        // Reset dimensions
        setDimensions();
        // Reset game values
        gameLost = false;
        minesLeft = totalMines;
        numFlags = 0;

        initializeGrid();
        updateGUI();
    }

    public void changeLevel(int level) {
        if (level == EASY_LEVEL) {
            System.out.println("-------EASY-------");
            totalMines = EASY_MINES;
            numRows = EASY_ROWS;
            numCols = EASY_COLS;
            gameLevel = EASY_LEVEL;
            // Recolour
            easyButton.setBackground(Color.GRAY); // Current selected
            mediumButton.setBackground(Color.LIGHT_GRAY);
            hardButton.setBackground(Color.LIGHT_GRAY);
        }
        else if (level == MEDIUM_LEVEL) {
            System.out.println("-------MED-------");
            totalMines = MED_MINES;
            numRows = MED_ROWS;
            numCols = MED_ROWS;
            gameLevel = MEDIUM_LEVEL;
            // Recolour
            easyButton.setBackground(Color.LIGHT_GRAY);
            mediumButton.setBackground(Color.GRAY); // Current selected
            hardButton.setBackground(Color.LIGHT_GRAY);
        }
        else {
            System.out.println("-------HARD-------");
            totalMines = HARD_MINES;
            numRows = HARD_ROWS;
            numCols = HARD_COLS;
            gameLevel = HARD_LEVEL;
            // Recolour
            easyButton.setBackground(Color.LIGHT_GRAY);
            mediumButton.setBackground(Color.LIGHT_GRAY);
            hardButton.setBackground(Color.GRAY); // Current selected
        }
        // RESET
        resetGame();
    }

    private void printGrid(){
        System.out.println("-------------------------------");
        for (int row=0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print("|" + numGrid[row][col]);
            }
            System.out.println("");
        }
    }

    private void startGUI() {
        // Layouts
        levelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        statusPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        // Update
        updateGUI();
        // Frame format
        gameWindow.setLocation(SCREEN_DIM.width/2-gameWindow.getSize().width/2, SCREEN_DIM.height/2-gameWindow.getSize().height/2);
        // Open frame
        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void updateGUI() {
        //gameWindow.setVisible(false);
        setDimensions();
        // -----GAME WINDOW-----
        gameWindow.setSize(gameWindowDimension);
        finalPanel.setSize(gameWindowDimension);

        // -----LEVEL PANEL-----
        // Panel
        levelPanel.setPreferredSize(levelPanelDimension);
        levelPanel.setMaximumSize(levelPanel.getPreferredSize());
        // Button size
        easyButton.setPreferredSize(levelButtonDimension);
        mediumButton.setPreferredSize(levelButtonDimension);
        hardButton.setPreferredSize(levelButtonDimension);
        // Add button to levelPanel
        levelPanel.add(easyButton);
        levelPanel.add(mediumButton);
        levelPanel.add(hardButton);

        // -----STATUS PANEL-----
        // Status panel
        statusPanel.setPreferredSize(statusPanelDimension);
        statusPanel.setMaximumSize(statusPanel.getPreferredSize());
        // Reset button
        resetButton.setPreferredSize(MineCell.CELL_DIMENSION);
        resetButton.setIcon(MineCell.SMILEY);
        resetButton.setName("reset");
        // Timer
        timeElapsedLabel.setText("00:00");
        // Mines left
        minesLeftLabel.setText(Integer.toString(minesLeft));
        // Add status to panel
        statusPanel.add(resetButton);

        // -----BUTTON PANEL-----
        // Format
        System.out.println("Grid: " + gridPanelDimension);
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
        gameWindow.setVisible(true);

    }

    public static void main(String[] args) {
        Minesweeper newGame = new Minesweeper();
    }
}
