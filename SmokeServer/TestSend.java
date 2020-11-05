package SmokeServer;

import java.io.IOException;
import java.util.Scanner;

public class TestSend implements Runnable{
	Scanner sc = new Scanner(System.in);
	String msg;
	ServerManagements sm;
	
	TestSend(){
		try {
			sm = new ServerManagements();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run() {
		while(true){
			msg = sc.nextLine();
			sm.sensingRecevie(msg);
		}
	}

}
