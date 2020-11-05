package SmokeClient;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSender extends Thread {		//송신 스레드 클래스
	private Socket socket;
	private Scanner sc;
	private String dong;
	
	ClientSender(Socket socket, String dong){
		this.socket = socket;
		this.dong = dong;
		
	}
	
	//서버에 메시지를 날려서
	//DB서버에 질의(Query)
	
	
	public void run(){
		String str;
		byte[] b = new byte[4096];
		try{
			b = dong.getBytes("UTF-8");
			socket.getOutputStream().write(b);
			socket.getOutputStream().flush();
			
			while(true){
				sc = new Scanner(System.in);
				str = sc.nextLine();
				b = str.getBytes("UTF-8");
				socket.getOutputStream().write(b);
				socket.getOutputStream().flush();

			}//while()
		}//try()
		catch (IOException e) { }
		finally{
			try { socket.close(); } 
			catch (IOException e) { }
		}//finally	
	}//run()
	
}
