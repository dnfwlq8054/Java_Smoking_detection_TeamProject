package SmokeServer;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

public class ServerSend {
	
	Iterator categorizeDong(String dong){
		// 클라이언트 테이블에서 해당 동에 있는 클라이언트 요소들 꺼내와서 반환
		if(dong.equals("101")){
			return ClientList.cTable.get(dong).iterator();
		}
		else if(dong.equals("201")){
			return ClientList.cTable.get(dong).iterator();
		}
		else if(dong.equals("301")){
			return ClientList.cTable.get(dong).iterator();
		}
		else
			return null;
	}
	
	void send(Socket s, char[] arr){		//배열 그림 보내는거
		byte[] b = new byte[4096];
		String string;
		try{
			string = String.valueOf(arr) + "@";		//@ 수신하면 클라가 배열로 처리할거임
			b = string.getBytes("UTF-8");
			s.getOutputStream().write(b);
			s.getOutputStream().flush();
		} catch (IOException e) {}
	}
	
	void sendToAll(char[] arr, String dong, String msg, String ho, char stat){		
		
		Iterator itr = categorizeDong(dong);
		byte[] b = new byte[4096];
		String string;
		try{
			while(itr.hasNext()){	
				Socket s = (Socket)itr.next();
				
				b = msg.getBytes("UTF-8");
				s.getOutputStream().write(b);
				
				string = String.valueOf(arr) + "@";		//감지 상태 같은거 입력될 때마다 알려주는거
				b = string.getBytes("UTF-8");
				s.getOutputStream().write(b);
				s.getOutputStream().flush();
			}//while()
		}
		catch (IOException e) {}
	}	//sendToAll()
	
	void sendToAll(String msg, String dong){		//DB내용 꺼내온거 보내기 역할
		Iterator itr = categorizeDong(dong);
		byte[] b = new byte[4096];
		while(itr.hasNext()){	
			try {
				Socket s = (Socket)itr.next();
				b = msg.getBytes("UTF-8");
				s.getOutputStream().write(b);
				s.getOutputStream().flush();
			}
			catch (IOException e) {}
		}	//while()
	}

}