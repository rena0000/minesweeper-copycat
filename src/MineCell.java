import javax.swing.*;
import java.awt.*;

public class MineCell extends JButton {
    // --------BUTTON DIMENSION--------
    static final int CELL_WIDTH = 40;
    static final Dimension CELL_DIMENSION = new Dimension(CELL_WIDTH, CELL_WIDTH);

    // -------------IMAGES-------------
    // static ImageIcon OG_SMILEY = new ImageIcon(Minesweeper.class.getResource("/images/smiley.png"));
    // static ImageIcon OG_WIN_FACE = new ImageIcon(Minesweeper.class.getResource("/images/win.jpg"));
    // static ImageIcon OG_BAD_MINE = new ImageIcon(Minesweeper.class.getResource("/images/winFace.png"));
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
    // static final ImageIcon SMILEY = getScaledImageIcon(OG_SMILEY);
    // static final ImageIcon WIN_FACE = getScaledImageIcon(OG_WIN_FACE);
    // static final ImageIcon BAD_MINE = getScaledImageIcon(OG_BAD_MINE);
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
    private boolean isMine;
    private boolean isExposed;
    private boolean isFlagged;

    public MineCell(int cellValue) {
    // Cell constructor
        // Set attributes
        this.cellValue = cellValue;
        isExposed = false;
        isFlagged = false;
        isMine = (cellValue == -1);
        // JButton properties
        assignImage();
        // setText(String.valueOf(cellValue));
        setSize(CELL_DIMENSION);
        // Mouse listener
        this.addMouseListener(new MineMouseHandler(this));
    }

    private void assignImage() {
        // Opened
        if (isExposed) {
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
        // Unopened
        else {
            // Flag
            if (isFlagged) {
                setIcon(FLAG);
            }
            // Blank
            else {
                setIcon(BLANK_TILE);
            }
        }
    }

    public void exposeCell() {
        isExposed = true;
        assignImage();
    }

    private static ImageIcon getScaledImageIcon(ImageIcon image) {
    // Scale ImageIcon to appropriate cell dimensions
        Image img = image.getImage().getScaledInstance(CELL_WIDTH, CELL_WIDTH, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(img);
        return newIcon;

    }


}
