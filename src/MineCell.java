import javax.swing.*;
import java.awt.*;

public class MineCell extends JButton {

    Minesweeper game;

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
    private int cellValue;
    private int cellRowLocation;
    private int cellColLocation;
    private boolean isMine;
    private boolean isExposed;
    private boolean isFlagged;


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
    }

    public int getCellRow() {
        return cellRowLocation;
    }

    public int getCellCol() {
        return cellColLocation;
    }

    public boolean getIsExposed() {
        return isExposed;
    }

    public boolean getIsFlagged() {
        return isFlagged;
    }

    public void exposeCell() {
    // Make cell value visible
        isExposed = true;
        // Mine
        if (isMine) {
            setIcon(MINE);
        }
        else {
            // Zero
            if (cellValue == 0) {
                setIcon(null);
                setBackground((Color.LIGHT_GRAY));
            }
            // Numbered
            else {
                setIcon(NUMBER_ICONS[cellValue]);
            }
        }
    }

    public void cellLeftClicked() {
        if (!isExposed && !isFlagged) {
            exposeCell();
            // Check if game lost
            if (isMine && !game.isGameLost()) {
                game.loseGame();
                setIcon(BAD_MINE);
                exposeAll();
            }
            // Expose more if cell is zero
            else if (cellValue == 0) {
                exposeZero();
            }
        }
    }

    public void cellChorded() {
        exposeZero();
    }


    public void exposeZero() {
    // Expose surrounding cells when a zero cell is exposed
        int cellRow = cellRowLocation;
        int cellCol = cellColLocation;

        // Check surrounding cells
        for (int i=-1; i < 2; i++) {
            for (int j=-1; j < 2; j++) {
                int rowIndex = cellRow + i;
                int colIndex = cellCol + j;
                // Check in range
                boolean inRange = rowIndex < game.getGameRows() && colIndex < game.getGameCols() && rowIndex > -1 && colIndex > -1;
                if (inRange && !isMine){
                    MineCell adjCell = game.cellGrid[rowIndex][colIndex];
                    // If adjacent cell also zero, expose recursively
                    if (!adjCell.isExposed && !adjCell.isFlagged) {
                        adjCell.exposeCell();
                        if (adjCell.cellValue == 0) {
                            adjCell.exposeZero();
                        }
                    }
                }
            }
        }
    }

    public void exposeAll() {
    // Expose all cells
        int gameRows = game.getGameRows();
        int gameCols = game.getGameCols();

        for(int row=0; row < gameRows; row++) {
            for(int col=0; col < gameCols; col++) {
                MineCell cell = game.cellGrid[row][col];
                if (!cell.isExposed){
                    cell.exposeCell();
                }
            }
        }
    }

    public void flagCell() {
        setIcon(FLAG);
        isFlagged = true;
        game.incrementFlags();
    }

    public void unflagCell() {
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
