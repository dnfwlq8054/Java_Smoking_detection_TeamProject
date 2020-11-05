package SmokeServer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainGUIManager {
	private JFrame frm;
	private JButton totalButton, exitButton;
	private JPanel panel1, panel2, panel3, panel4, panel5;
	private JLabel title1, title2, title3;
	private static JTextArea textList, textRecord, textPiList;
	private JScrollPane scroll1, scroll2, scroll3;
	
	private BufferedWriter out;
	
	MainGUIManager(){
		frm = new JFrame("SmokeServer");
		frm.setBounds(150, 150, 900, 500);
		frm.setLayout(new FlowLayout());
		
		totalButton = new JButton("통계자료");
		exitButton = new JButton("종료");
		totalButton.addActionListener(accessDB);
		exitButton.addActionListener(exitBtn);
		
		title1 = new JLabel("기록 목록");
		title2 = new JLabel("접속자 Client IP");
		title3 = new JLabel("접속자 파이 IP");
		
		textRecord = new JTextArea(20,50);
		textRecord.setEditable(false);
		textList = new JTextArea(10,15);
		textList.setEditable(false);
		textPiList = new JTextArea(10, 15);
		textPiList.setEditable(false);
		
		scroll1 = new JScrollPane(textRecord);
		scroll2 = new JScrollPane(textList);
		scroll3 = new JScrollPane(textPiList);
		
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(2,1,10,10));
		panel1.add(totalButton);
		panel1.add(exitButton);
		
		panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(title1, BorderLayout.NORTH);
		panel2.add(scroll1, BorderLayout.SOUTH);
		
		panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
		panel3.add(title2, BorderLayout.NORTH);
		panel3.add(scroll2, BorderLayout.SOUTH);
		
		panel4 = new JPanel();
		panel4.setLayout(new BorderLayout());
		panel4.add(title3, BorderLayout.NORTH);
		panel4.add(scroll3, BorderLayout.SOUTH);
		
		panel5 = new JPanel();
		panel5.setLayout(new BorderLayout());
		panel5.add(panel3, BorderLayout.NORTH);
		panel5.add(panel4, BorderLayout.SOUTH);
		
		frm.add(panel1);
		frm.add(panel2);
		frm.add(panel5);
		
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
	
	ActionListener exitBtn = new ActionListener() {		//ActionListener에 대한 클래스를 만드는것은 불필요 하다고 생각해 
		public void actionPerformed(ActionEvent e) {	//Anonymous클래스로 만들었습니다.
			System.exit(0);
		}
	};
	
	ActionListener accessDB = new ActionListener() {		
		public void actionPerformed(ActionEvent e) {
			System.out.println("통계자료 누름");
			try{
				SmokingDTO smoke;
				Vector<SmokingDTO> v = ClientAction.select();
				for(int i = 0; i < v.size(); i++){
					smoke = v.get(i);
					String s1 = Integer.toString(smoke.getDong());
					String s2 = Integer.toString(smoke.getHo());
					String s3 = smoke.getDate();
					String s4 = smoke.getConnect();
					
					if(s4.equals("연결끊김")){	//추가됨
						saveFile(s3 + "에" + "_" + s1 + "동" + "_" + s2+ "호" + "_" + s4);
						Thread.sleep(5);
						if ( i == v.size() - 1){ 
							Process processBuilder = new ProcessBuilder
									("notepad.exe","..\\ServerDB\\ServerStatsDB.txt").start();
							out = null;
						}
					}
					else{
						saveFile(s3 + "에" + "_" + s1 + "동" + "_" + s2+ "호" + "_" + "감지");
						Thread.sleep(5);
						if ( i == v.size() - 1){ 
							Process processBuilder = new ProcessBuilder
									("notepad.exe","..\\ServerDB\\ServerStatsDB.txt").start();
							out = null;
						}
					}
				}
			}catch(IOException | ClassNotFoundException | SQLException | InterruptedException e1){
				e1.printStackTrace();
			}
		}
	};
	
	void saveFile(String msg){
		try {
			if(check()){
				out = new BufferedWriter(new FileWriter("..\\ServerDB\\ServerStatsDB.txt"));
			}
			out.write(msg);
			out.newLine();
			out.flush();
			
		} catch (IOException e) {
			System.out.println("파일 생성 오류");
		}
	}
	
	boolean check(){
		if(out == null)
			return true;
		else
			return false;
	}
	
	static void setList() { textList.setText(""); }
	
	static void setPiList() { textPiList.setText(""); }
	
	static void writeRecordText(String msg) { textRecord.append(msg + '\n'); }
	
	static void writeListText(String msg) {	textList.append(msg + '\n'); }
	
	static void writePiListText(String msg) { textPiList.append(msg + '\n'); }
	
}

