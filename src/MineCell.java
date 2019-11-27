import javax.swing.*;
import java.awt.*;

public class MineCell extends JButton {
    // --------BUTTON DIMENSION--------
    static final int BUTTON_WIDTH = 50;
    static final int BUTTON_HEIGHT = 50;

    // -------------IMAGES-------------
    static ImageIcon OG_SMILEY = new ImageIcon(Minesweeper.class.getResource("/images/smiley.png"));
    static ImageIcon OG_WIN_FACE = new ImageIcon(Minesweeper.class.getResource("/images/win.jpg"));
    static ImageIcon OG_BAD_MINE = new ImageIcon(Minesweeper.class.getResource("/images/winFace.png"));
    static ImageIcon OG_BLANK_TILE = new ImageIcon(Minesweeper.class.getResource("/images/blank.jpg"));
    static ImageIcon OG_MINE = new ImageIcon(Minesweeper.class.getResource("/images/mine.jpg"));
    static ImageIcon OG_FLAG = new ImageIcon(Minesweeper.class.getResource("/images/flag.jpg"));
    static ImageIcon OG_ONE = new ImageIcon(Minesweeper.class.getResource("/images/one.jpg"));
    static ImageIcon OG_TWO = new ImageIcon(Minesweeper.class.getResource("/images/two.jpg"));
    static ImageIcon OG_THREE = new ImageIcon(Minesweeper.class.getResource("/images/three.jpg"));
    static ImageIcon OG_FOUR = new ImageIcon(Minesweeper.class.getResource("/images/four.jpg"));
    static ImageIcon OG_FIVE = new ImageIcon(Minesweeper.class.getResource("/images/five.jpg"));
    static ImageIcon OG_SIX = new ImageIcon(Minesweeper.class.getResource("/images/six.jpg"));
    static ImageIcon OG_SEVEN = new ImageIcon(Minesweeper.class.getResource("/images/seven.jpg"));
    static ImageIcon OG_EIGHT = new ImageIcon(Minesweeper.class.getResource("/images/eight.jpg"));
    // Scale
    static final ImageIcon SMILEY = getScaledImageIcon(OG_SMILEY);
    static final ImageIcon WIN_FACE = getScaledImageIcon(OG_WIN_FACE);
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
    // Array of numbered icons
    static ImageIcon[] NUMBER_ICONS = new ImageIcon[]{BLANK_TILE, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT};

    // ----------CELL ATTRIBUTES---------
    int cellValue;
    boolean isExposed;
    boolean isFlagged;

    public MineCell(int cellValue) {
    // Cell constructor
        this.cellValue = cellValue;
        isExposed = false;
        isFlagged = false;
    }

    public void assignImage() {


    }

    public static ImageIcon getScaledImageIcon(ImageIcon image) {
    // Scale ImageIcon to appropriate cell dimensions
        Image img = image.getImage().getScaledInstance(MineCell.BUTTON_WIDTH, MineCell.BUTTON_HEIGHT, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(img);
        return newIcon;

    }


}
