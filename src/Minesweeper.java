/**
 * MINESWEEPER GAME CLASS
 * @author Serena He
 * ----------------------------------------------------------------------------------
 * Main class for Minesweeper-Copycat.
 * Begins a Minesweeper game in easy mode
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private Timer gameTimer;
    private int currentTime;
    private boolean gameStart;
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
        /** CONSTRUCTOR
         * Constructs a new Minesweeper game
         */
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
        // Name
        easyButton.setName("level");
        mediumButton.setName("level");
        hardButton.setName("level");
        // Text
        easyButton.setFont(new Font("Arial", Font.PLAIN, LEVEL_BUTTON_FONT));
        mediumButton.setFont(new Font("Arial", Font.PLAIN, LEVEL_BUTTON_FONT));
        hardButton.setFont(new Font("Arial", Font.PLAIN, LEVEL_BUTTON_FONT));
        // Colour
        easyButton.setBackground(Color.GRAY); // Current selected
        mediumButton.setBackground(Color.LIGHT_GRAY);
        hardButton.setBackground(Color.LIGHT_GRAY);

        // -----STATUS LABELS-----
        // Background colour
        minesLeftLabel.setBackground(Color.BLACK);
        timeElapsedLabel.setBackground(Color.BLACK);
        // Alignment
        minesLeftLabel.setHorizontalAlignment(SwingConstants.LEFT);
        timeElapsedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // Text
        minesLeftLabel.setText(Integer.toString(minesLeft));
        minesLeftLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        timeElapsedLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        // Timer
        currentTime = 0;
        timeElapsedLabel.setText(formatTime(currentTime));
        gameStart = false;
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTime++;
                timeElapsedLabel.setText(formatTime(currentTime));
            }
        });

        // -----ADD MOUSELISTENER-----
        easyButton.addMouseListener(new MineMouseHandler(this));
        mediumButton.addMouseListener(new MineMouseHandler(this));
        hardButton.addMouseListener(new MineMouseHandler(this));
        resetButton.addMouseListener(new MineMouseHandler(this));

        // Begin
        initializeGrid();
        startGUI();
    }

    static String formatTime(int time) {
        /**
         * Formats the current elapsed time to display on screen.
         * @param time The current elapsed time in seconds.
         * @return String The properly formatted time as a string.
         */
        if (time < 10) {
            return ("00" + time);
        }
        else if (time < 100) {
            return ("0" + time);
        }
        else {
            return (Integer.toString(time));
        }
    }

    // ----------GETTER/SETTERS---------

    int getGameLevel() {
        /**
         * Gets the current game level
         * @return int Number corresponding to a game level (1-EASY, 2-MEDIUM, 3-HARD)
         */
        return gameLevel;
    }

    int getGameRows() {
        /**
         * Gets the number of rows in the current game
         * @return int Number of rows
         */
        return numRows;
    }

    int getGameCols() {
        /**
         * Gets the number of columns in the current game
         * @return int Number of columns
         */
        return numCols;
    }

    boolean isGameLost() {
        /**
         * Returns the loss state of the game (eg. True if game has been lost).
         * @return boolean Whether or not the game is lost
         */
        return gameLost;
    }

    boolean getGameStart() {
        return gameStart;
    }

    // ----------COUNTERS---------

    void incrementFlags() {
        /**
         * Increments the numFlags attribute of the Minesweeper class and decrements the "mines left" label
         * @return Nothing.
         */
        numFlags++;
        updateMinesLeftLabel();
    }

    void decrementFlags() {
        /**
         * Decrements the numFlags attribute of the Minesweeper class and increments the "mines left" label
         * @return Nothing.
         */
        numFlags--;
        updateMinesLeftLabel();
    }

    void incrementOpened() {
        /**
         * Increments the numOpened attribute (number of cells opened) of the Minesweeper class
         * @return Nothing.
         */
        numOpened++;
    }

    // ----------GAME WIN/LOSS---------

    void loseGame() {
        /**
         * Called when the game has been lost. Exposes all cells on the grid with mines appearing as mines, and sets a custom face on the reset button.
         * @return Nothing.
         */
        gameLost = true;
        gameTimer.stop();
        exposeAll();
        resetButton.setIcon(MineCell.LOSE_FACE);
    }

    void winGame() {
        /**
         * Called when the game has been won. Exposes all cells on the grid with mines appearing as flags, and sets a custom face on the reset button.
         * @return Nothing.
         */
        gameTimer.stop();
        resetButton.setIcon(MineCell.WIN_FACE);
        exposeAll();
    }

    void checkWin() {
        /**
         * Checks if the current game status satisfies the win condition. Win condition is if the number of non-mines equals the number of opened cells.
         * @return Nothing.
         */
        int numSafeCells = numRows*numCols - totalMines;
        if (numSafeCells == numOpened) {
            winGame();
        }
    }

    void exposeAll() {
        /**
         * Expose all cells on the grid. Sets mines as flags if game was won, and mines as mines if game was lost.
         * @return Nothing.
         */
        for(int row=0; row < numRows; row++) {
            for(int col=0; col < numCols; col++) {
                MineCell cell = cellGrid[row][col];
                if (!cell.getIsExposed()){
                    // Set mine as flag if game was won
                    if (cell.getIsMine() && !gameLost) {
                        cell.setMineOnWin();
                    }
                    cell.exposeCell();
                }
            }
        }
    }

    // ----------GAME GUI/RESET---------
    void gameStart( ) {
        /**
         * Set gameStart to true and begin timing
         */
        gameStart = true;
        gameTimer.start();
    }

    private void updateMinesLeftLabel() {
        minesLeftLabel.setText(Integer.toString(totalMines-numFlags));
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

    private void printGrid(){
        System.out.println("-------------------------------");
        for (int row=0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print("|" + numGrid[row][col]);
            }
            System.out.println("");
        }
    }

    void resetGame() {
        System.out.println("-------RESET-------");
        // Stop timer
        gameTimer.stop();
        // Re-init grids
        numGrid = new int[numRows][numCols];
        cellGrid = new MineCell[numRows][numCols];
        gridPanel.removeAll();
        // Reset dimensions
        setDimensions();
        // Reset game values
        gameLost = false;
        gameStart = false;
        minesLeft = totalMines;
        numFlags = 0;
        numOpened = 0;
        currentTime = 0;
        // Update labels
        timeElapsedLabel.setText(formatTime(currentTime));
        minesLeftLabel.setText(Integer.toString(minesLeft));

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
            numCols = MED_COLS;
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

    private void startGUI() {
    // Initializes GUI for first opening
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
    // Updates GUI for level changes
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
        // Add status to panel
        statusPanel.add(minesLeftLabel);
        statusPanel.add(resetButton);
        statusPanel.add(timeElapsedLabel);

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
        gameWindow.pack();
        gameWindow.setVisible(true);
    }

    public static void main(String[] args) {
        Minesweeper newGame = new Minesweeper();
    }
}
