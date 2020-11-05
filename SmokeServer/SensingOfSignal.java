package SmokeServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SensingOfSignal implements Runnable{
	
	private BufferedReader in;
	private ServerManagements sm;
	private Socket socket;
	private int index;		//Vector 인덱스 구하기 파이 리스트에서 파이 리스트제거 위함
	
	SensingOfSignal(Socket socket) throws IOException {
		index = ClientList.piTable.size() - 1;		//Vecotr 인덱스 값이 0부터 시작하니까 -1
		this.socket = socket;
		in =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
		sm = new ServerManagements();
	}
	
	public void run() {	
		//파이로 부터 데이터 수신하는 기능
		int c, char_len = 0;
		char[] ch = new char[1024];
		try {
			while(true) {

				while(true) {
					c = in.read();
					if(c == -1)		//라즈베리파이 종료시 -1 반환
						break;
					
					ch[char_len] = (char) c;	//2바이트씩 읽어들임
					if(ch[char_len] == '\n') {	//문자열 끝까지 읽으면 반복문 탈출
						char_len = 0;
						break;
					}
					char_len++;
				}
				
				if(c == -1)	//-1이 오면 쓰래드 종료
					break;
			
				if(ch[0] == 'A')	//읽어온 값 앞이 A라면 
				{
					String msg = String.valueOf(ch, 1, ch.length - 1);
					sm.sensingRecevie(msg);
				}
				else
					System.out.println(String.valueOf(ch));
				
			} //while()
		} catch (IOException e) { }
		finally{
			ClientList.piTable.remove(index);
			MainGUIManager.writeRecordText(this.socket.getInetAddress() + "나갔습니다.");
			sm.showPiList();
			try { this.socket.close(); } 
			catch (IOException e) { }
		}
	} //run()
}
