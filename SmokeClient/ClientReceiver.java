package SmokeClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientReceiver extends Thread {
	private Socket socket;			//소켓변수
	private BufferedWriter out;		//파일에 쓰기 위한 변수
	private int hoSize;				
	
	ClientReceiver(Socket socket, int hoSize){
		this.socket = socket;		//인자 socket과 hosize를 전달받음
		this.hoSize = hoSize;		//ho의 갯수를 전달받는다.
	}
	
	public void run(){			//쓰레드 start 시작
		byte[] b = new byte[4096];
		int n = 0;
		String msg;

		try{
			while((n = socket.getInputStream().read(b)) != 0){
				msg = new String(b, 0, n, "UTF-8");
				
				if(msg.contains("/")){
					System.out.println(msg);
					ClientGUI.ta.append(msg + "\n");
				}
				else if(msg.contains("@")){
					changeToChar(msg);
				}
				else if(msg.equals("finish")){
					System.out.println(msg);
					Process processBuilder = new ProcessBuilder
							("notepad.exe","..\\ClientDB\\StatsDB.txt").start();
					out = null;
				}
				else{
					System.out.println(msg);
					saveFile(msg);
				}
			}
		}//try()
		catch (IOException e) { }
		finally{
			try { socket.close(); } 
			catch (IOException e) { }
		}//finally	
	}//run()
	
	void changeToChar(String msg){
		char[] aptMap = msg.toCharArray();
		String sMap = "";
		
		for(int i = (aptMap.length - 1)/hoSize; 1 <= i; i--){	//배열 크기/호 사이즈 = 층수가 나온다.
			if(i < 10)
				sMap+= i + "  층 >>  ";
			else
				sMap+= i + "층 >>  ";
			
			for(int j = (i-1) * hoSize; j < i * hoSize; j++){
				sMap+= aptMap[j];
			}
			sMap+= '\n';
		}
		ClientGUI.ta2.setText(sMap);
	}
	
	void saveFile(String msg){
		try {
			if(check()){
				out = new BufferedWriter(new FileWriter("..\\ClientDB\\StatsDB.txt"));
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
	
}
