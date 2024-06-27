package mine_sweeper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class ButtonEventListener extends MouseAdapter {
	private Minesweeper minesweeper;
	
	public ButtonEventListener(Minesweeper minesweeper){
		this.minesweeper = minesweeper;
	}
	
    public void mousePressed(MouseEvent e){
		this.minesweeper.buttonClicked((JButton)e.getSource());
	}
}
