package mine_sweeper;

import java.awt.Color;

import javax.swing.JButton;

/*
 * 一つ一つのパネル（セル）
 * それぞれのセルは、下記４つの状態を保持している。
 * １．爆弾であるか否か
 * ２．開かれているか否か
 * ３．マークがつけられているか否か
 * ４．隣接する爆弾の数
 * 
 * 上記４つの状態にしたがって、ボタンの状態を変更するメソッドを内部に持つ
 * 
 * また、配列の値も保持している。
 */
public class Cell extends JButton {
	
	private final int cellX;
	private final int cellY;
	
	// セルの情報
	private int     mineCount   = 0;
	private boolean isMine      = false;
	private boolean isOpened    = false;
	private boolean isFlag      = false;
	private boolean visited		= false;
	
	public Cell(int cellX, int cellY){
		super();
		
		this.cellX = cellX;
		this.cellY = cellY;
		
		this.update();
	}
	
	public void setMineCount(int mineCount){
		this.mineCount = mineCount;
		this.update();
	}
	
	public int getMineCount(){
		return this.mineCount;
	}
	
	public void setMine(boolean isMine){
		this.isMine = isMine;
		this.update();
	}
	
	public boolean isMine(){
		return this.isMine;
	}
	
	public void setOpened(boolean isOpened){
		this.isOpened = isOpened;
		this.update();
	}
	
	public boolean isOpened(){
		return this.isOpened;
	}
	
	public void setFlag(boolean isFlag){
		this.isFlag = isFlag;
		this.update();
	}
	
	public boolean isFlag(){
		return this.isFlag;
	}
	
	public int getCellX(){
		return this.cellX;
	}
	
	public int getCellY(){
		return this.cellY;
	}

	private void update(){
		
		if (this.isOpened) {
			this.setEnabled(false);		// 開いた状態のものはClick不可とする。
			if (this.isMine) {
				this.setBackground(Color.BLUE);
				this.setText("σ");
			} else {
				this.setBackground(Color.white);
				if (this.mineCount > 0){
					this.setText(Integer.toString(this.mineCount));
				} else {
					this.setText("");
				}
			}
		} else {
			this.setEnabled(true);
			if (this.isFlag){
				this.setBackground(Color.GREEN);
				this.setText("┻");
			} else {
				this.setBackground(Color.LIGHT_GRAY);
				this.setText("");
			}
		}
		
	}

	/**
	 * @return visited
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * @param visited セットする visited
	 */
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
