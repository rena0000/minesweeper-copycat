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

        boolean leftPressed = SwingUtilities.isLeftMouseButton(e);
        boolean rightPressed = SwingUtilities.isRightMouseButton(e);
        boolean isChord = leftPressed && rightPressed;

        // -----LEVEL-----
        if (button.getName() == "level" && leftPressed) {

        }
        // -----RESET-----
        else if (button.getName() == "reset" && rightPressed) {

        }
        // -----MINE CELL-----
        else {
            MineCell cell = (MineCell) button;
            // Chord
            if (isChord) {
                System.out.println("chord");
                if (cell.getIsExposed()) {
                    cell.cellChorded();
                }
            }
            // Right click - flag
            else if (rightPressed) {
                System.out.println("right click");
                if(!cell.getIsExposed()) {
                    if(cell.getIsFlagged()) {
                        cell.unflagCell();
                    }
                    else {
                        cell.flagCell();
                    }
                }
            }
            // Left click - expose
            else if (leftPressed) {
                cell.cellLeftClicked();
            }
            else {

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object ob = e.getSource();
        JButton button = (JButton) ob;

        // -----LEVEL-----
        if (button.getName() == "level" && SwingUtilities.isLeftMouseButton(e)) {
            int currentLevel = game.getGameLevel();

            // Change easy
            if (button.getText() == "Easy" && currentLevel != Minesweeper.EASY_LEVEL) {
                game.changeLevel(Minesweeper.EASY_LEVEL);
            }
            // Change medium
            else if (button.getText() == "Medium" && currentLevel != Minesweeper.MEDIUM_LEVEL) {
                game.changeLevel(Minesweeper.MEDIUM_LEVEL);
            }
            // Change hard
            else if (button.getText() == "Hard" && currentLevel != Minesweeper.HARD_LEVEL) {
                game.changeLevel(Minesweeper.HARD_LEVEL);
            }
        }
        // -----RESET-----
        else if (button.getName() == "reset" && SwingUtilities.isLeftMouseButton(e)) {
            System.out.println("Reset clicked");
            game.resetGame();
        }
        else {

        }
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
