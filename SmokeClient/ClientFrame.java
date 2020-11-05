package SmokeClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientFrame extends JFrame implements ActionListener{
	private JLabel jlIpAddr,jlPort;
	private JTextField jtIpAddr,jtPort; //IP주소, PORT번호 입력 필드
	private JButton jbCon,jbExt;	//왼쪽 단 연결, 종료 버튼
	private Socket sock;
	private String ipAddr;		// 클라이언트에서 입력한 IP주소 저장 변수
	private int portNum;

	ClientFrame(){	//생성자
		setLayout(null);

		add(jlIpAddr = new JLabel("IP주소 ",JLabel.CENTER));
		jlIpAddr.setBorder(BorderFactory.createBevelBorder(0));
		jlIpAddr.setBounds(10, 10, 120, 50);
        add(jtIpAddr = new JTextField());
        jtIpAddr.setHorizontalAlignment(JTextField.CENTER);
        jtIpAddr.setBounds(140, 10, 170, 50);
        add(jlPort = new JLabel("Port번호 ",JLabel.CENTER));
        jlPort.setBorder(BorderFactory.createBevelBorder(0));
        jlPort.setBounds(10, 70, 120, 50);
        add(jtPort = new JTextField());
        jtPort.setHorizontalAlignment(JTextField.CENTER);
        jtPort.setBounds(140, 70, 170, 50);

        /*			 버튼				*/
        add(jbCon = new JButton("연결"));
        jbCon.setBounds(120, 150, 180, 50);
        jbCon.addActionListener(this);	// 버튼객체 이벤트 처리 위한 리스너
        add(jbExt = new JButton("종료"));
        jbExt.setBounds(120, 220, 180, 50);
        jbExt.addActionListener(this);
        
        setResizable(false);
        setSize(400, 400);
        setTitle("클라이언트 메인");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
    
	}	//ClientFrame 생성자

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbCon){		//연결 버튼 누르면
			System.out.println("연결버튼 누름");
			//연결 요청이 정상적으로 완료되면 기존 입력 창 닫히고 
			//새로운 윈도우 창 띄운다.
			
			ipAddr = jtIpAddr.getText();
			portNum = Integer.parseInt(jtPort.getText());

			try{
				sock = new Socket();
				
				SocketAddress socketAddress = new InetSocketAddress(ipAddr, portNum);
				new Thread() {   
		              public void run() {   
		                  for (int i = 0; i < 3; i++) {   
		                      try {   
		                          Thread.sleep(2000);   
		                      } catch (InterruptedException e) {   
		                      }   
		                      JOptionPane.getRootFrame().dispose();    
		                  }
		              }
		          }.start();   
		        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "서버에 접속중....");
				sock.connect(socketAddress, 5000);
				
				new SelectDong(sock);		//동 선택 프레임 활성화
				dispose();		//주소 프레임 닫기
				System.out.println("서버연결 완료");
				return;
			}catch(IOException ce) {
				JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "서버 접속 실패, 다시 실행하세요.");
				System.out.println("종료");
				System.exit(0);
			}
		}
		else if(e.getSource() == jbExt){	//종료 버튼 누르면
			System.exit(0);		
		}
	}
}
	
class SelectDong extends JFrame implements ActionListener{			//동 선택 위한 정적 클래스
	private JButton btn1,btn2,btn3;
	private Socket sock;
	private ClientSender sender;
	private ClientReceiver receiver;
	
	SelectDong(Socket sock){
		this.sock = sock;

		setLayout(null);
		add(btn1 = new JButton("1동"));
		btn1.setBounds(55,50,100,60);
		btn1.addActionListener(this);
		add(btn2 = new JButton("2동"));
		btn2.setBounds(160,50,100,60);
		btn2.addActionListener(this);
		add(btn3 = new JButton("3동"));
		btn3.setBounds(265,50,100,60);
		btn3.addActionListener(this);
		
		setResizable(false);
        setSize(400, 200);
        setTitle("동 선택");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn1){
			String dong = "/101";
			new ClientGUI(dong,sock);
			ClientSender sender = new ClientSender(sock,dong);
			ClientReceiver receiver = new ClientReceiver(sock, 2);
			sender.start();
			receiver.start();
			dispose();
			
		}else if(e.getSource() == btn2){
			String dong = "/201";
			new ClientGUI(dong,sock);
			ClientSender sender = new ClientSender(sock,dong);
			ClientReceiver receiver = new ClientReceiver(sock, 3);
			sender.start();
			receiver.start();
			dispose();
			
		}else if(e.getSource() == btn3){
			String dong = "/301";
			new ClientGUI(dong,sock);
			ClientSender sender = new ClientSender(sock,dong);
			ClientReceiver receiver = new ClientReceiver(sock, 4);
			sender.start();
			receiver.start();
			dispose();
		}
	}
}


	
