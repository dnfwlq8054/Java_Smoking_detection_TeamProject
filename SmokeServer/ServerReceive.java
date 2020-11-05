package SmokeServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;



public class ServerReceive implements Runnable, ClientList, ArrayInfo {

	private Socket socket;
	private ServerManagements serverm;
	private int index;		//벡터 인덱스 구하기 위함
	public String dong;
	
	public ServerReceive(Socket socket) throws IOException {
		this.socket = socket;
		serverm = new ServerManagements();
	}
	
	void categorizeDongAdd(String str){
		//클라이언트가 몇 동으로 접속했는지 확인 훟 접속한 동에 맞게 소켓 정보 저장하는 기능 
		//현재 감지상태 보냄
		ServerSend s = new ServerSend();
		
		if(str.equals("/101")){
			dong = "101";
			ClientList.clientTable1.add(socket);
			ClientList.cTable.put(dong, ClientList.clientTable1);//해시테이블에 정보 입력
			this.index = ClientList.clientTable1.size() - 1;
			s.send(socket, arr1);
		}
		else if(str.equals("/201")){
			dong = "201";
			ClientList.clientTable2.add(socket);
			ClientList.cTable.put(dong, ClientList.clientTable2);
			this.index = ClientList.clientTable1.size() - 1;
			s.send(socket, arr2);
		}
		else if(str.equals("/301")){
			dong = "301";
			ClientList.clientTable3.add(socket);
			ClientList.cTable.put(dong, ClientList.clientTable3);
			this.index = ClientList.clientTable1.size() - 1;
			s.send(socket, arr3);
		}
		else{
			try { this.socket.close(); }	//나머지 동이면 소켓 닫음 
			catch (IOException e) { }
		}
	}
	
	
	public void run() {
		byte[] b = new byte[4096];
		int n = 0;
		String msg = null;
		try{
			n = socket.getInputStream().read(b);
			msg = new String(b, 0, n, "UTF-8");
			
			categorizeDongAdd(msg);
			
			while((n = socket.getInputStream().read(b)) != 0){
				msg = new String(b, 0, n, "UTF-8");
			
				if(msg.equals("파일요청")){
					System.out.println("파일요청들어옴");
					serverm.fileReceive(msg, dong);
				}else{		//이부분 검토
					System.out.println(msg);
					serverm.sensingRecevie(msg);
				}
			}
		}//try()
		catch (IOException e) {}
		finally{
			ClientList.clientTable.remove(index);
			ClientList.cTable.get(dong).remove(index);
			MainGUIManager.writeRecordText(this.socket.getInetAddress() + "나갔습니다.");
			serverm.showClientList();
			try { this.socket.close(); } 
			catch (IOException e) {}
		}//finally()
	}//run()	
	
	
}