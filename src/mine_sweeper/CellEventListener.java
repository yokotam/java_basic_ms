package mine_sweeper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellEventListener extends MouseAdapter {
	private Minesweeper minesweeper;
	
	public CellEventListener(Minesweeper minesweeper){
		this.minesweeper = minesweeper;
	}
	
    public void mousePressed(MouseEvent e){
		Cell cell        = (Cell)e.getSource();
		int  mouseButton = e.getButton();
		
		if(mouseButton == MouseEvent.BUTTON1){
			// 左クリック
			this.minesweeper.cellClicked(cell);
		} else if(mouseButton == MouseEvent.BUTTON3){
			// 右クリック
			this.minesweeper.cellRightClicked(cell);
		}
	}
}
