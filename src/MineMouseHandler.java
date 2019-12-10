/**
 * MINESWEEPER MOUSE CLASS
 * @author Serena He
 * ----------------------------------------------------------------------------------
 * Implementing the MouseListener class for the Minesweeper game
*/

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class MineMouseHandler implements MouseListener{

    private static long delayThresh = 50; // milliseconds
    private static int  firstClick = 0;
    private static long firstClickTime;
    private static long secondClickTime;

    private Minesweeper game;
    private MineCell cell;

    public MineMouseHandler(Minesweeper game) {
        this.game = game;
    }

    public MineMouseHandler(MineCell cell) {
        this.cell = cell;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /**
         * Mouse has been pressed. Applicable only to Minesweeper grid cells. Checks if the left mouse button, right
         * mouse button, or both have been pressed and responds accordingly.
         */
        Object ob = e.getSource();
        JButton button = (JButton) ob;

        boolean leftPressed = SwingUtilities.isLeftMouseButton(e);
        boolean rightPressed = SwingUtilities.isRightMouseButton(e);
        boolean isChord = false;

        secondClickTime = System.currentTimeMillis();
        long timeDiff = secondClickTime - firstClickTime;

        // Check if button is a first or second click
        if (firstClick == 0 || timeDiff >= delayThresh) {
            firstClickTime = System.currentTimeMillis();
            firstClick = leftPressed == true ? 1 : 3;
        }
        else if ((firstClick == 1 && rightPressed == true) || (firstClick == 3 && leftPressed == true)){
            secondClickTime = System.currentTimeMillis();
            System.out.println("Delay: " + (secondClickTime - firstClickTime));
            if (timeDiff < delayThresh) {
                isChord = true;
                firstClick = 0;
                firstClickTime = 0;
                secondClickTime = 0;
            }
        }
        // No chord, reset everything
        else {
            firstClick = 0;
            firstClickTime = 0;
            secondClickTime = 0;
        }

        // -----MINECELL-----
        if (button.getName() == null){
            MineCell cell = (MineCell) button;
            // Chord
            if (isChord) {
                if (cell.getIsExposed()) {
                    cell.cellChorded();
                }
            }
            // Right click - flag
            else if (rightPressed) {
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
                // Do nothing
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /**
         * Mouse has been pressed and released. Applicable to level changing buttons and reset button.
         */
        Object ob = e.getSource();
        JButton button = (JButton) ob;

        boolean leftClicked = SwingUtilities.isLeftMouseButton(e);

        // -----LEVEL-----
        if (button.getName() == "level" && leftClicked) {
            Minesweeper.Level currentLevel = game.getGameLevel();

            // Change easy
            if (button.getText() == "Easy" && currentLevel != Minesweeper.Level.EASY) {
                game.changeLevel(Minesweeper.Level.EASY);
            }
            // Change medium
            else if (button.getText() == "Medium" && currentLevel != Minesweeper.Level.MEDIUM) {
                game.changeLevel(Minesweeper.Level.MEDIUM);
            }
            // Change hard
            else if (button.getText() == "Hard" && currentLevel != Minesweeper.Level.HARD) {
                game.changeLevel(Minesweeper.Level.HARD);
            }
        }
        // -----RESET-----
        else if (button.getName() == "reset" && leftClicked) {
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
