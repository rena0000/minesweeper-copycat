/**
 * MINESWEEPER CELL CLASS
 * @author Serena He
 * ----------------------------------------------------------------------------------
 * Extending the JButton class from Java Swing, defines a cell object in the Minesweeper grid.
*/

import javax.swing.*;
import java.awt.*;

public class MineCell extends JButton {

    // --------BUTTON DIMENSION--------
    static final int CELL_WIDTH = 36;
    static final Dimension CELL_DIMENSION = new Dimension(CELL_WIDTH, CELL_WIDTH);

    // -------------IMAGES-------------
    static final ImageIcon OG_SMILEY = new ImageIcon(Minesweeper.class.getResource("/images/face.jpg"));
    static final ImageIcon OG_WIN_FACE = new ImageIcon(Minesweeper.class.getResource("/images/winFace.jpg"));
    static final ImageIcon OG_LOSE_FACE = new ImageIcon(Minesweeper.class.getResource("/images/loseFace.png"));
    static final ImageIcon OG_BAD_MINE = new ImageIcon(Minesweeper.class.getResource("/images/wrongMine.jpg"));
    static final ImageIcon OG_WRONG_MINE = new ImageIcon(Minesweeper.class.getResource("/images/oops.jpg"));
    static final ImageIcon OG_BLANK_TILE = new ImageIcon(Minesweeper.class.getResource("/images/blank.jpg"));
    static final ImageIcon OG_MINE = new ImageIcon(Minesweeper.class.getResource("/images/mine.jpg"));
    static final ImageIcon OG_FLAG = new ImageIcon(Minesweeper.class.getResource("/images/flag.jpg"));
    static final ImageIcon OG_ONE = new ImageIcon(Minesweeper.class.getResource("/images/one.png"));
    static final ImageIcon OG_TWO = new ImageIcon(Minesweeper.class.getResource("/images/two.png"));
    static final ImageIcon OG_THREE = new ImageIcon(Minesweeper.class.getResource("/images/three.png"));
    static final ImageIcon OG_FOUR = new ImageIcon(Minesweeper.class.getResource("/images/four.png"));
    static final ImageIcon OG_FIVE = new ImageIcon(Minesweeper.class.getResource("/images/five.png"));
    static final ImageIcon OG_SIX = new ImageIcon(Minesweeper.class.getResource("/images/six.png"));
    static final ImageIcon OG_SEVEN = new ImageIcon(Minesweeper.class.getResource("/images/seven.png"));
    static final ImageIcon OG_EIGHT = new ImageIcon(Minesweeper.class.getResource("/images/eight.png"));
    // Scale
    static final ImageIcon SMILEY = getScaledImageIcon(OG_SMILEY);
    static final ImageIcon WIN_FACE = getScaledImageIcon(OG_WIN_FACE);
    static final ImageIcon LOSE_FACE = getScaledImageIcon(OG_LOSE_FACE);
    static final ImageIcon BAD_MINE = getScaledImageIcon(OG_BAD_MINE);
    static final ImageIcon WRONG_MINE = getScaledImageIcon(OG_WRONG_MINE);
    static final ImageIcon BLANK_TILE = getScaledImageIcon(OG_BLANK_TILE);
    static final ImageIcon MINE = getScaledImageIcon(OG_MINE);
    static final ImageIcon FLAG = getScaledImageIcon(OG_FLAG);
    static final ImageIcon ONE = getScaledImageIcon(OG_ONE);
    static final ImageIcon TWO = getScaledImageIcon(OG_TWO);
    static final ImageIcon THREE = getScaledImageIcon(OG_THREE);
    static final ImageIcon FOUR = getScaledImageIcon(OG_FOUR);
    static final ImageIcon FIVE = getScaledImageIcon(OG_FIVE);
    static final ImageIcon SIX = getScaledImageIcon(OG_SIX);
    static final ImageIcon SEVEN = getScaledImageIcon(OG_SEVEN);
    static final ImageIcon EIGHT = getScaledImageIcon(OG_EIGHT);

    static ImageIcon[] NUMBER_ICONS = new ImageIcon[]{BLANK_TILE, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT};

    // ----------CELL ATTRIBUTES---------
    Minesweeper game;
    private int cellValue;
    private int cellRowLocation;
    private int cellColLocation;
    private boolean isMine;
    private boolean isExposed;
    private boolean isFlagged;
    private ImageIcon exposedImage;


    public MineCell(Minesweeper game, int cellValue, int cellRowLocation, int cellColLocation) {
        /**
         * CONSTRUCTOR
         * @param Minesweeper The Minesweeper object that the cell belong to.
         * @param int The value of the cell (0-8 for a numbered cell, -1 for a mine).
         * @param int The row index of the cell in the grid.
         * @param int The column index of the cell in the grid.
         */
        // Set attributes
        this.cellValue = cellValue;
        this.cellRowLocation = cellRowLocation;
        this.cellColLocation = cellColLocation;
        this.game = game;
        isExposed = false;
        isFlagged = false;
        isMine = (cellValue == -1);
        // Set blank
        setIcon(BLANK_TILE);
        // setText(String.valueOf(cellValue));
        setSize(CELL_DIMENSION);
        // Mouse listener
        this.addMouseListener(new MineMouseHandler(this));
        // Set actual image
        if (isMine) {
            exposedImage = MINE;
        }
        else {
            // Zero
            if (cellValue == 0) {
                exposedImage = null;
            }
            // Numbered
            else {
                exposedImage = NUMBER_ICONS[cellValue];
            }
        }
    }

    // ----------GETTER/SETTERS---------

    int getCellRow() {
        /**
         * Return the row index of the cell.
         * @return int Row index of cell.
         */
        return cellRowLocation;
    }

    int getCellCol() {
        /**
         * Return the column index of the cell.
         * @return int Column index of cell.
         */
        return cellColLocation;
    }

    boolean getIsExposed() {
        /**
         * Return if the cell is exposed or not.
         * @return boolean If the cell is exposed.
         */
        return isExposed;
    }

    boolean getIsFlagged() {
        /**
         * Return if the cell is flagged or not.
         * @return boolean If the cell is flagged.
         */
        return isFlagged;
    }

    boolean getIsMine() {
        /**
         * Return if the cell is a mine.
         * @return boolean If the cell is a mine.
         */
        return isMine;
    }

    void setMineOnWin() {
        /**
         * Called when the game has been won to set all the mines to flags.
         * @return Nothing.
         */
        exposedImage = FLAG;
    }

    void setWronglyFlagged() {
        exposedImage = WRONG_MINE;
    }

    // ----------MOUSE EVENTS---------s

    void cellLeftClicked() {
        /**
         * Response to a mouse click event on a LifeCell. Calls the Minesweeper gameStart() function for the first click
         * of the game. Exposes cells and checks for game win/loss.
         * @return Nothing.
         */
        // Start game on first click
        if (!game.getGameStart()) {
            game.gameStart();
        }
        if (!isExposed && !isFlagged) {
            exposeCell();
            // MINE
            if (isMine) {
                // If game hasn't been lost yet, end game and set current mine to a special icon.
                if (!game.isGameLost()) {
                    game.loseGame();
                    setIcon(BAD_MINE);
                }
            }
            // NOT A MINE
            else {
                if (isFlaggedFully() && !isFlaggedCorrectly()) {
                    game.exposeWrongFlags(this);
                    game.loseGame();
                }
                else {
                    if (cellValue == 0) {
                        exposeZero();
                    }
                }
            }
        }
    }

    void cellChorded() {
        /**
         * Called in response to a mouse chord event. Exposes surrounding cells if the cell has been flagged correctly.
         * @return Nothing.
         */
        if (isExposed) {
            exposeZero();
        }
    }

    // ----------EXPOSE---------

    private void exposeZero() {
        /** Expose surrounding cells when a zero-value cell is opened. Is called recursively if another zero-cell
         * is opened as a result.
         * @return Nothing.
         */
        int cellRow = cellRowLocation;
        int cellCol = cellColLocation;

        if(isFlaggedFully()) {
            if(isFlaggedCorrectly()) {
                // Open surrounding cells
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        int rowIndex = cellRow + i;
                        int colIndex = cellCol + j;
                        // Check in range
                        boolean inRange = rowIndex < game.getGameRows() && colIndex < game.getGameCols() && rowIndex > -1 && colIndex > -1;
                        if (inRange) {
                            MineCell adjCell = game.cellGrid[rowIndex][colIndex];
                            // If adjacent cell also zero, expose recursively
                            if (!adjCell.isExposed && !adjCell.isFlagged && !adjCell.isMine) {
                                adjCell.exposeCell();
                                if (adjCell.cellValue == 0) {
                                    adjCell.exposeZero();
                                }
                            }
                        }
                    }
                }
            }
            else {
                game.loseGame();
            }
        }
    }

    void exposeCell() {
        /**
         * Exposes the cell on the grid. Shows a number for safe, non-zero cells, and a mine for mines.
         * @return Nothing.
         */
        isExposed = true;
        game.incrementOpened();
        // Set image
        if (exposedImage == null) {
            setIcon(null);
            setBackground(Color.LIGHT_GRAY);
        }
        else {
            setIcon(exposedImage);
        }
        game.checkWin();

    }

    // ----------FLAGS---------

    private boolean isFlaggedCorrectly() {
        /**
         * Check if a cell if flagged correctly by comparing the number of correct flags with the number of
         * surrounding mines.
         * @return boolean If the number of correct flags is equal to the number of surrounding mines.
         */
        int cellRow = cellRowLocation;
        int cellCol = cellColLocation;
        int correctFlags = 0;

        // Check surrounding cells
        for (int i=-1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int rowIndex = cellRow + i;
                int colIndex = cellCol + j;
                // Check in range
                boolean inRange = rowIndex < game.getGameRows() && colIndex < game.getGameCols() && rowIndex > -1 && colIndex > -1;
                if (inRange) {
                    MineCell adjCell = game.cellGrid[rowIndex][colIndex];
                    // If adjacent cell also zero, expose recursively
                    if (adjCell.isMine && adjCell.isFlagged) {
                        correctFlags++;
                    }
                }
            }
        }
        return (correctFlags == cellValue);
    }

    private boolean isFlaggedFully() {
        /**
         * Checks if the number of flags surrounding a cell is equal to or greater than the correct value.
         * Does not consider if the location is correct, only checks the numerical value.
         * @return boolean If the correct number of flags surrounds a cell.
         */
        int cellRow = cellRowLocation;
        int cellCol = cellColLocation;
        int numFlags = 0;

        // Check surrounding cells
        for (int i=-1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int rowIndex = cellRow + i;
                int colIndex = cellCol + j;
                // Check in range
                boolean inRange = rowIndex < game.getGameRows() && colIndex < game.getGameCols() && rowIndex > -1 && colIndex > -1;
                if (inRange) {
                    MineCell adjCell = game.cellGrid[rowIndex][colIndex];
                    // If adjacent cell also zero, expose recursively
                    if (adjCell.isFlagged) {
                        numFlags++;
                    }
                }
            }
        }
        return (numFlags >= cellValue);
    }

    void flagCell() {
        /**
         * Flags cell if it is not flagged and updates the number of flags in the game.
         * @return Nothing.
         */
        setIcon(FLAG);
        isFlagged = true;
        game.incrementFlags();
    }

    void unflagCell() {
        /**
         * Unflags cell if it is flagged and updates the number of flags in the game.
         * @return Nothing.
         */
        setIcon(BLANK_TILE);
        isFlagged = false;
        game.decrementFlags();
    }

    private static ImageIcon getScaledImageIcon(ImageIcon image) {
        /**
         * Scale ImageIcon to appropriate cell dimensions.
         * @param ImageIcon Unscaled ImageIcon.
         * @return ImageIcon Scaled ImageIcon.
         */
        Image img = image.getImage().getScaledInstance(CELL_WIDTH, CELL_WIDTH, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(img);
        return newIcon;
    }
}
