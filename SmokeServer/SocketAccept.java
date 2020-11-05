package SmokeServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

public class SocketAccept extends Thread implements ArrayInfo{		//클라이어트 접속 받기
	
	private SensingOfSignal pisos;
	private ServerSocket serverSocket = null;	//소켓 생성
	private ServerReceive receive;
	
	SocketAccept(){		//캐릭터 배열 초기화 작업
		Arrays.fill(arr1, '□');
		Arrays.fill(arr2, '□');
		Arrays.fill(arr3, '□');
	}
	
	public void run(){

		try{
			serverSocket = new ServerSocket(9000);	//소켓에 포트번호 부여(bind)
			System.out.println("서버가 준비되었습니다.");
			ClientList.cTable.put("101", new Vector<Socket>());		//초기화작업
			ClientList.cTable.put("201", new Vector<Socket>());
			ClientList.cTable.put("301", new Vector<Socket>());
			//////////////////////콘솔 테스트 입니다.///////////////////////////
			TestSend test = new TestSend();
			Thread t = new Thread(test);
			t.start();
			//////////////////////////////////////////////////////////////
			while(true){
				//	accept함수는 접속이 올때까지 무한정 대기한다.
				Socket socket = serverSocket.accept();	//서버 소켓 연결 함수(accept)
				MainGUIManager.writeRecordText(socket.getInetAddress() + "로부터 요청이 들어왔습니다.");
				checkPi(socket);		//소켓 정보 넘겨서 파이에서 접속한건지 판별
			} 
		}
		catch(IOException e){
			System.out.println("서버는 하나만 동작합니다..");
			System.exit(-1);
		}	//catch()
	}	//run()
	
	void checkPi(Socket socket) throws IOException {		//소켓정보가 파이접속 소켓일때 수행
		String ip = socket.getInetAddress().toString();
			if(ip.contains("14.33.236.")) {
				ClientList.piTable.add(socket);
				pisos = new SensingOfSignal(socket);
				Thread piThread = new Thread(pisos);
				piThread.start();
				MainGUIManager.writeRecordText(socket.getInetAddress() + "이 PI서버에 접속 완료.");
				MainGUIManager.writePiListText(socket.getInetAddress()+ "");
			}
			else {
				ClientList.clientTable.add(socket);
				receive = new ServerReceive(socket);
				Thread t = new Thread(receive);
				t.start();
				MainGUIManager.writeRecordText(socket.getInetAddress() + "이 서버에 접속 완료.");
				MainGUIManager.writeListText(socket.getInetAddress()+ "");
			}
	}
}	//SocetAccept class