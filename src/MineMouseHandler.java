import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class MineMouseHandler implements MouseListener{

    Minesweeper game;
    MineCell cell;

    public MineMouseHandler(Minesweeper game) {
        this.game = game;
    }

    public MineMouseHandler(MineCell cell) {
        this.cell = cell;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object ob = e.getSource();
        JButton button = (JButton) ob;

        // -----LEVEL-----
        if (button.getName() == "level" && SwingUtilities.isLeftMouseButton(e)) {
            String currentLevel = game.gameLevel;
            // Change easy
            if (button.getText() == "easy" && currentLevel != "easy") {

            }
            // Change medium
            else if (button.getText() == "med" && currentLevel != "med") {

            }
            // Change hard
            else if (currentLevel != "hard") {

            }
        }
        // -----RESET-----
        else if (button.getName() == "reset" && SwingUtilities.isLeftMouseButton(e)) {

        }
        // -----MINE CELL-----
        else {
            MineCell cell = (MineCell) button;
            // Chord
            if (SwingUtilities.isLeftMouseButton(e) && SwingUtilities.isRightMouseButton(e)) {
                if (cell.checkIsExposed()) {
                    cell.exposeZero();
                }
            }
            // Left click - expose
            else if (SwingUtilities.isLeftMouseButton(e)) {
                cell.exposeCell();
            }
            // Right click - flag
            else if (SwingUtilities.isRightMouseButton(e)) {
                if(!cell.checkIsExposed()) {
                    if(cell.checkIsFlagged()) {
                        cell.unflagCell();
                    }
                    else {
                        cell.flagCell();
                    }
                }
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
