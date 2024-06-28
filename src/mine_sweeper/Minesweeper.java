package mine_sweeper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
/*
 * JFrameを継承し、ActionListenerインターフェースを実装
 */
public class Minesweeper extends JFrame implements ActionListener {

	/*
	 * デフォルト値
	 */
	// 場の広さ、爆弾の数
	private static final int DEFAULT_COLS_NUM = 16;
	private static final int DEFAULT_ROWS_NUM = 16;
	private static final int DEFAULT_MINE_NUM = 40;
	// 画面タイトル
	private static final String TITLE = "まいんすいーぱー";

	// ゲームの進行状態
	private GameStatus gameStatus;
	private CellMapObject cellMapObject;

	// セルパネル
	private Cell[][]    cells;

	// 
	/*
	 * 盤面の配置を表すネストしたクラス
	 */
	public static class CellMapObject {
		// 行列の数および爆弾数
		private int colsNum;
		private int rowsNum;
		private int mineNum;
		
		public CellMapObject(int colsNum, int rowsNum, int mineNum) {
			setNums(colsNum, rowsNum, mineNum);
		}
		
		public void setNums(int colsNum, int rowsNum, int mineNum) {
			setColsNum(colsNum);
			setRowsNum(rowsNum);
			setMineNum(mineNum);			
		}
		
		public int getMineNum() {
			return mineNum;
		}

		public void setMineNum(int mineNum) {
			this.mineNum = mineNum;
		}

		public int getRowsNum() {
			return rowsNum;
		}

		public void setRowsNum(int rowsNum) {
			this.rowsNum = rowsNum;
		}

		public int getColsNum() {
			return colsNum;
		}

		public void setColsNum(int colsNum) {
			this.colsNum = colsNum;
		}		
		
	}
	
	/*
	 * ゲーム状態を表すネストしたクラス
	 */
	public static class GameStatus {
		private boolean isGameStarted;
		private boolean isGameFinished;
		
		public GameStatus() {
			setGameStarted(false);
			setGameFinished(false);
		}

		public boolean isGameStarted() {
			return isGameStarted;
		}

		public void setGameStarted(boolean isGameStarted) {
			this.isGameStarted = isGameStarted;
		}

		public boolean isGameFinished() {
			return isGameFinished;
		}

		public void setGameFinished(boolean isGameFinished) {
			this.isGameFinished = isGameFinished;
		}
		
		// ゲームが開始も終了もしていない状態
		public void reset() {
			setGameStarted(false);
			setGameFinished(false);
		}
		// ゲームが開始されている状態
		public void started() {
			setGameStarted(true);
			setGameFinished(false);
		}
		// ゲームが終了している状態
		public void finished() {
			setGameStarted(false);
			setGameFinished(true);
		}
		// ゲーム中であるか否か
		public boolean isInGame() {
			return isGameStarted() && !isGameFinished();
		}
	}
	
	/*
	 * ゲームのスタート
	 * 　コンテナ内のコンポーネントの位置と大きさを再計算し、適切に配置する。
	 */
	public static void main(String[] args){
		Minesweeper minesweeper = new Minesweeper(DEFAULT_COLS_NUM, DEFAULT_ROWS_NUM, DEFAULT_MINE_NUM);
		minesweeper.validate();
	}

	/*
	 * コンストラクタ
	 */
	public Minesweeper(int colsNum, int rowsNum, int mineNum){

		gameStatus = new GameStatus();
		
		
		// タイトルを設定
		this.setTitle(TITLE);
		
		// 閉じるボタンの動作を「非表示」→「アプリケーションの終了」とする。
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// サイズの変更を不可とする。
		this.setResizable(false);
		
		// デフォルトの行列の数および爆弾数
		cellMapObject = new CellMapObject(colsNum, rowsNum, mineNum);
		
		// ゲーム画面の表示
		this.validateScreen();
		
		// ウィンドウを表示させる
		this.setVisible(true);
	}
	
	// 画面を作成する
	private void validateScreen(){
		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().removeAll();
		

		// 画面を最大化した状態で開く
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//  レイアウトを設定
		// ボタンパネルの設定
		this.addComponent(setTopPanel(), 0, 1, GridBagConstraints.CENTER, 1, 0.0);
		// 爆弾マップ
		this.addComponent(setMinesMap(), 0, 0, GridBagConstraints.REMAINDER, 1, 1.0);
		
		// レイアウトの再描画
		this.getContentPane().validate();

		// 設定値を渡しておく
		jp.co.cscnet.yokota_m.MessageBox.setting(cellMapObject.colsNum * cellMapObject.rowsNum, cellMapObject.mineNum);

	}
	
	// マップの設定
	private JPanel setMinesMap() {
		JPanel mapPanel = new JPanel();
		
		mapPanel.setLayout(new GridLayout(cellMapObject.getRowsNum(), cellMapObject.getRowsNum()));
		// セルのサイズを決定する
		cells = new Cell[cellMapObject.getColsNum()][cellMapObject.getRowsNum()];
		
		// セルを新規生成
		for(int i = 0; i < cellMapObject.getRowsNum(); i++){
			for(int j = 0; j < cellMapObject.getColsNum(); j++){
				Cell cell = new Cell(j, i);
				// 配列にセルを割り当てる。（それぞれのセルは位置情報も持っている）
				this.cells[j][i] = cell;
				// セルにマウスイベントを紐づける
				cell.addMouseListener(new CellEventListener(this));
				mapPanel.add(cell) ;
			}
		}
		
		return mapPanel;
	}

	// ボタンの設定
	private JPanel setTopPanel() {

		JPanel topPanell = new JPanel();
		topPanell.setLayout(new GridBagLayout());
		
		// スタートボタンにイベントを紐づけて、ボタン配置用のパネルに追加
		JButton buttonStart = new JButton("スタート");
		buttonStart.addMouseListener(new ButtonEventListener(this));
		buttonStart.setFont(new Font("MS ゴシック", Font.BOLD, 39));
		topPanell.setBackground(Color.YELLOW);
		topPanell.add(buttonStart);	
		
		return topPanell;
		
	}

	private void addComponent(Component c, int x, int y, int w, int h, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout      mgr = (GridBagLayout)this.getContentPane().getLayout();
		
		gbc.fill       = GridBagConstraints.BOTH;
		gbc.gridx      = x;
		gbc.gridy      = y;
		gbc.gridwidth  = w;
		gbc.gridheight = h;
		gbc.weightx    = 1;
		gbc.weighty    = weighty;
		mgr.setConstraints(c, gbc);
		this.getContentPane().add(c);
	}
	
	// ゲームスタート
	private void gameStart(int x, int y){
		
		// 爆弾を配置;
		layoutClear();
		layoutMines(x, y);
		
		// ゲームが開始されている状態（終了していない）
		gameStatus.started();

		// ゲーム開始のメッセージ　※必ずここに入れること！
		jp.co.cscnet.yokota_m.MessageBox.show(jp.co.cscnet.yokota_m.MessageBox.GameStatus.STATUS_START, this);
		
	}
	
	// ゲームオーバー
	private void gameOver(){
		
		// すべての爆弾マスを開く
		for(int i = 0; i < cellMapObject.getRowsNum(); ++i){
			for(int j = 0; j < cellMapObject.getColsNum(); ++j){
				if (this.cells[j][i].isMine()) {	// 爆弾は開く	
					this.cells[j][i].setOpened(true);
				} else {
					// その他は使用不可とする。
					this.cells[j][i].setEnabled(false);
				}
			}
		}
		
		// ゲームが終了している状態
		gameStatus.finished();
		jp.co.cscnet.yokota_m.MessageBox.show(jp.co.cscnet.yokota_m.MessageBox.GameStatus.STATUS_GAMEOVER, this);
	}
	
	// ゲームクリアー
	private void gameClear(){
		
		// すべてのマスを使用不可とする。
		// すべての爆弾マスを開く
		for(int i = 0; i < cellMapObject.getRowsNum(); ++i){
			for(int j = 0; j < cellMapObject.getColsNum(); ++j){
				this.cells[i][j].setEnabled(false);
			}
		}		
		
		// ゲームが終了している状態
		gameStatus.finished();
		jp.co.cscnet.yokota_m.MessageBox.show(jp.co.cscnet.yokota_m.MessageBox.GameStatus.STATUS_CLEAR, this);
	}
	
	// ゲームがクリアされているか否かの判断
	private boolean isGameClear(){
		int count = 0;
		
		// 爆弾の位置以外が全て開かれている場合
		for(int i = 0; i < cellMapObject.getRowsNum(); ++i){
			for(int j = 0; j < cellMapObject.getColsNum(); ++j){
				if(!this.cells[j][i].isMine() && this.cells[j][i].isOpened()){
					++count;
				}
			}
		}
		if(count + cellMapObject.getMineNum() == cellMapObject.getRowsNum() * cellMapObject.getColsNum()){
			return true;
		}
		
		return false;
	}
	
	// セルを開いたときの処理
	private void openCell(int x, int y){
		
		// ゲームが終了している
		if(gameStatus.isGameFinished()){
			return;
		}
		
		// 開放済みのセルの場合
		if(this.cells[x][y].isOpened()){
			return;
		}
		
		// フラグが立っている場合
		if(this.cells[x][y].isFlag()){
			return;
		}
		
		// クリック地点が爆弾の場合の処理
		if(this.cells[x][y].isMine()){
			this.gameOver();
			return;
		}
		
		// 再帰的にセルを開く
		this.openCell(x, y, true);
		
		// フラグの解放
		//this.visited = null;
		

		// ゲームクリアの処理
		if(this.isGameClear()){
			this.gameClear();
		}
		
		// 画面変更を反映
		this.getContentPane().validate();
	}
	
	private void openCell(int x, int y, boolean first){
		if(!this.isValidCoord(x, y)) return;
		if(this.cells[x][y].isVisited()) return;
		
		this.cells[x][y].setVisited(true);
		
		if(!this.cells[x][y].isMine()){
			this.cells[x][y].setOpened(true);
		}
		
		if(this.cells[x][y].getMineCount() == 0){
			for(int i = -1; i <= 1; ++i){
				for(int j = -1; j <= 1; ++j){
					int newX = x + i;
					int newY = y + j;
					
					openCell(newX, newY, false);
				}
			}
		}
	}
	
	// 爆弾を配置する
	private void layoutMines(int clickedX, int clickedY){
		Random rand  = new Random();
		int    count = 0;
		
		// 指定した個数の爆弾を配置する
		while(count < cellMapObject.getMineNum()){
			int x = rand.nextInt(cellMapObject.getColsNum());
			int y = rand.nextInt(cellMapObject.getRowsNum());
			
			// クリックした座標は除く
			if(clickedX == x && clickedY == y) continue; 
			
			Cell cell = this.cells[x][y];
			
			// 既に爆弾が配置されている場合は除く
			if(cell.isMine()) continue;
			
			// 爆弾を配置
			cell.setMine(true);
			++count;
		}
		
		// 各セルの周囲の爆弾の個数を計算する
		for(int i = 0; i < cellMapObject.getRowsNum(); ++i){
			for(int j = 0; j < cellMapObject.getColsNum(); ++j){
				final int[] d = new int[] { 1, -1, 0 };
				
				for(int k = 0; k < d.length; ++k){
					for(int l = 0; l < d.length; ++l){
						int x = j + d[k];
						int y = i + d[l];
						
						if(!this.isValidCoord(x, y)) continue;
						
						// 座標が存在する & 爆弾のあるセル
						if(cells[x][y].isMine()){
							cells[j][i].setMineCount(cells[j][i].getMineCount() + 1);
						}
					}
				}
			}
		}
	}
	
	// 盤面をクリアする
	private void layoutClear(){
		
		// 盤面をリセットする
		for(int i = 0; i < cellMapObject.getRowsNum(); ++i){
			for(int j = 0; j < cellMapObject.getColsNum(); ++j){
				Cell cell = this.cells[j][i];
				
				cell.setMine(false);
				cell.setFlag(false);
				cell.setOpened(false);
				cell.setMineCount(0);
			}
		}
	}
	
	// フラグの反転処理
	private void toggleFlag(Cell cell){
		
		// セルが開かれて無い場合
		if(!cell.isOpened()){
			// フラグが立っている場合
			if(cell.isFlag()){
				// フラグを消す
				cell.setFlag(false);
			}
			
			// フラグが立っていない場合
			else {
				// フラグを立てる
				cell.setFlag(true);
			}
		}
	}
	
	// 正しい座標かどうか
	private boolean isValidCoord(int x, int y){
		return x >= 0 && x < cellMapObject.getColsNum() && y >= 0 && y < cellMapObject.getRowsNum();
	}

	
	
	// セルがクリックされたときの処理
	public void cellClicked(Cell cell){
		
		int x = cell.getCellX();
		int y = cell.getCellY();
		
		// ゲームが終了状態でない場合
		if(!gameStatus.isGameFinished()){
			// ゲームが開始されていない場合
			if( !gameStatus.isGameStarted()){
				this.gameStart(x, y);
			}
			
			// セルを開く
			this.openCell(x, y);
		}
	}
	
	public void cellRightClicked(Cell cell){
		
		//  ゲームが開始されている場合
		if(gameStatus.isInGame()){
			this.toggleFlag(cell);
		}
	}
	
	public void buttonClicked(JButton button){
		// 設定ダイアログを開く
		SettingDialog dialog = new SettingDialog(this);
		// TODO 前回の設定値をデフォルトとしたい。
		dialog.showDialog(DEFAULT_COLS_NUM, DEFAULT_ROWS_NUM, DEFAULT_MINE_NUM);
		
		
		cellMapObject.setNums(dialog.getColsNum(), dialog.getRowsNum(), dialog.getMinsNum());
		
		// TODO キャンセルを押した場合でも新しくゲームが始まってしまう。
		
		// ゲームスタート状態とする。
		validateScreen();
		gameStatus.reset();
			
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
