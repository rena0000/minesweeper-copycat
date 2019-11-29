import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
        MineCell cell = (MineCell) ob;
        cell.exposeCell();
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
