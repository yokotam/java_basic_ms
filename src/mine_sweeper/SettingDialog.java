package mine_sweeper;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SettingDialog extends JDialog implements ActionListener
{
	
	private JTextField jColsNum;
	private JTextField jRowsNum;
	private JTextField jMineNum;;
	
	private JButton buttonOK;
	
	private JTextField getjColsNum() {
		return jColsNum;
	}


	private void setjColsNum(JTextField jColsNum) {
		this.jColsNum = jColsNum;
	}


	private JTextField getjRowsNum() {
		return jRowsNum;
	}


	private void setjRowsNum(JTextField jRowsNum) {
		this.jRowsNum = jRowsNum;
	}


	private JTextField getjMineNum() {
		return jMineNum;
	}


	private void setjMineNum(JTextField jMineNum) {
		this.jMineNum = jMineNum;
	}


	public int getColsNum() {
		return Integer.valueOf(this.getjColsNum().getText());
	}
	
	public int getRowsNum() {
		return Integer.valueOf(this.getjRowsNum().getText());
	}
	
	public int getMinsNum() {
		return Integer.valueOf(this.getjMineNum().getText());
	}
	
	// コンストラクタ
	public SettingDialog(Minesweeper mines){
		super(mines, "設定");
		
		GridLayout layout = new GridLayout(4, 2);
		
		this.setLayout(layout);
		
		this.setjColsNum(new JTextField(2));
		this.setjRowsNum(new JTextField(2));
		this.setjMineNum(new JTextField(2));
		
		JButton buttonCancel = new JButton("ｷｬﾝｾﾙ");
		buttonOK = new JButton("OK");
		
		this.getContentPane().add(new JLabel("よこ"));
		this.getContentPane().add(this.getjColsNum());
		
		this.getContentPane().add(new JLabel("たて"));
		this.getContentPane().add(this.getjRowsNum());

		this.getContentPane().add(new JLabel("爆弾の数"));
		this.getContentPane().add(this.getjMineNum());
		
		this.getContentPane().add(buttonOK);
		this.getContentPane().add(buttonCancel);		
		
		buttonCancel.addActionListener(this);
		buttonOK.addActionListener(this);

		this.setModal(true);
    }
	
    public void showDialog(int colsNum, int rowsNum, int minsNum){
		
    	this.getjColsNum().setText(Integer.toString(colsNum));
    	this.getjRowsNum().setText(Integer.toString(rowsNum));
    	this.getjMineNum().setText(Integer.toString(minsNum));
		
		setLocationRelativeTo(this.getOwner()); // 位置を調整
		pack();                                 // 大きさを調整
		setVisible(true);                       // 画面を開く
    }
    
    //画面を閉じる
    public void actionPerformed(ActionEvent a){
		JButton button = (JButton)a.getSource();
		
		if(button == this.buttonOK){
			if (this.isValid(this.getColsNum(), this.getRowsNum(), this.getMinsNum())){
				this.setVisible(false);
			} else {
				// TODO 設定値が正しくない場合、どうすれば良いかも表示する
				jp.co.cscnet.yokota_m.MessageBox.show("設定値が正しくありません", "エラー", (JFrame)this.getOwner());
			}
		} else {
			this.setVisible(false);
		}
    }
	
	// 妥当な値か調べる
	public boolean isValid(int cols, int rows, int mineCount){
		
		// TODO 最低でも６×６マス
		// TODO 最大は30×24マス
		// TODO 爆弾の数はマス目の10%～30%の間とする
	
		return true;
	}


}
