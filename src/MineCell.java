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
    static final int CELL_WIDTH = 40;
    static final Dimension CELL_DIMENSION = new Dimension(CELL_WIDTH, CELL_WIDTH);

    // -------------IMAGES-------------
    static ImageIcon OG_SMILEY = new ImageIcon(Minesweeper.class.getResource("/images/face.jpg"));
    static ImageIcon OG_WIN_FACE = new ImageIcon(Minesweeper.class.getResource("/images/winFace.jpg"));
    static ImageIcon OG_LOSE_FACE = new ImageIcon(Minesweeper.class.getResource("/images/loseFace.png"));
    static ImageIcon OG_BAD_MINE = new ImageIcon(Minesweeper.class.getResource("/images/wrongMine.jpg"));
    static ImageIcon OG_BLANK_TILE = new ImageIcon(Minesweeper.class.getResource("/images/blank.jpg"));
    static ImageIcon OG_MINE = new ImageIcon(Minesweeper.class.getResource("/images/mine.jpg"));
    static ImageIcon OG_FLAG = new ImageIcon(Minesweeper.class.getResource("/images/flag.jpg"));
    static ImageIcon OG_ONE = new ImageIcon(Minesweeper.class.getResource("/images/one.png"));
    static ImageIcon OG_TWO = new ImageIcon(Minesweeper.class.getResource("/images/two.png"));
    static ImageIcon OG_THREE = new ImageIcon(Minesweeper.class.getResource("/images/three.png"));
    static ImageIcon OG_FOUR = new ImageIcon(Minesweeper.class.getResource("/images/four.png"));
    static ImageIcon OG_FIVE = new ImageIcon(Minesweeper.class.getResource("/images/five.png"));
    static ImageIcon OG_SIX = new ImageIcon(Minesweeper.class.getResource("/images/six.png"));
    static ImageIcon OG_SEVEN = new ImageIcon(Minesweeper.class.getResource("/images/seven.png"));
    static ImageIcon OG_EIGHT = new ImageIcon(Minesweeper.class.getResource("/images/eight.png"));
    // Scale
    static final ImageIcon SMILEY = getScaledImageIcon(OG_SMILEY);
    static final ImageIcon WIN_FACE = getScaledImageIcon(OG_WIN_FACE);
    static final ImageIcon LOSE_FACE = getScaledImageIcon(OG_LOSE_FACE);
    static final ImageIcon BAD_MINE = getScaledImageIcon(OG_BAD_MINE);
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
    // Cell constructor
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
        return cellRowLocation;
    }

    int getCellCol() {
        return cellColLocation;
    }

    boolean getIsExposed() {
        return isExposed;
    }

    boolean getIsFlagged() {
        return isFlagged;
    }

    boolean getIsMine() {
        return isMine;
    }

    void setMineOnWin() {
        exposedImage = FLAG;
    }

    // ----------MOUSE EVENTS---------s

    void cellLeftClicked() {
        if (!game.getGameStart()) {
            game.setGameStart(true);
            // TODO: Start timer
        }
        if (!isExposed && !isFlagged) {
            exposeCell();
            // Check if game lost
            if (isMine && !game.isGameLost()) {
                game.loseGame();
                setIcon(BAD_MINE);
            }
            // Expose more if cell is zero
            else if (cellValue == 0) {
                exposeZero();
            }
        }
    }

    void cellChorded() {
        exposeZero();
    }

    // ----------EXPOSE---------

    private void exposeZero() {
    // Expose surrounding cells including when a zero cell is exposed
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
        // Make cell value visible
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
        // Check if all surrounding mines are flagged correctly
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
        // Check if cell's surrounding flags are equal or greater than the correct number
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
        setIcon(FLAG);
        isFlagged = true;
        game.incrementFlags();
    }

    void unflagCell() {
        setIcon(BLANK_TILE);
        isFlagged = false;
        game.decrementFlags();
    }

    private static ImageIcon getScaledImageIcon(ImageIcon image) {
    // Scale ImageIcon to appropriate cell dimensions
        Image img = image.getImage().getScaledInstance(CELL_WIDTH, CELL_WIDTH, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(img);
        return newIcon;

    }


}
