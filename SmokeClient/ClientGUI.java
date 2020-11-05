package SmokeClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientGUI extends JFrame implements ActionListener{		// 서버와 통신을 담당. 소켓을 생성하는 스레드 클래스
	private Socket socket;
	
	private JScrollPane jsp;	
	private JButton jbStat; //통계 버튼
	private JPanel p;
	static JTextArea ta;
	static JTextArea ta2;
	
	public ClientGUI(String dong, Socket sock){
		this.socket = sock;
		
		p = new JPanel();
		p.setLayout(null);

		jbStat = new JButton("통계치");		//통계치 버튼 생성
		jbStat.setBounds(80, 30, 180, 50);	//통계치 버튼의 위치를 설정
		jbStat.addActionListener(this);
		p.add(jbStat);						//패널에 붙이고
		add(p);								//Frame에 패널을 붙인다
		
		ta = new JTextArea(100,200);
		ta.setEditable(false);
		jsp = new JScrollPane(ta);
		jsp.setBounds(650, 120, 500, 500);
		p.add(jsp);
		add(p);
		
		ta2 = new JTextArea(100,200);
		ta2.setEditable(false);
		ta2.setBounds(300,120,300,500);
		p.add(ta2);
		add(p);
		
		setResizable(false);
	    setSize(1200, 800);
	    setTitle(dong + "동 흡연감지 시스템");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String str;
		byte[] b = new byte[4096];
		if(e.getSource() == jbStat){		//통계치 버튼 눌렀을 때의 동작
			// JTextArea에 찍힌 감지내역을 텍스트 파일 형태로 저장하는 버튼
			System.out.println("통계치 버튼누름");
			
			str = "파일요청";
			
			try {
				
				b = str.getBytes("UTF-8");
				socket.getOutputStream().write(b);
				socket.getOutputStream().flush();
				
			} catch ( IOException e1) {
				e1.printStackTrace();
			} 

		}
	}
	
}
