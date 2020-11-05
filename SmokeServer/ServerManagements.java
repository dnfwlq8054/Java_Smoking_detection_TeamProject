package SmokeServer;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class ServerManagements implements ArrayInfo{
	private ServerSend sSend;
	private StringTokenizer st;
	private int r;
	
	public ServerManagements() throws IOException {
		sSend = new ServerSend();
	}
	
	void fileReceive(String msg, String dong){
		
		try{
			SmokingDTO smoke;
			Vector<SmokingDTO> v = ClientAction.selWhere(dong);
			for(int i = 0; i < v.size(); i++){
				smoke = v.get(i);
				String s1 = Integer.toString(smoke.getDong());
				String s2 = Integer.toString(smoke.getHo());
				String s3 = smoke.getDate();
				String s4 = smoke.getConnect();		//추가됨

				if(s4.equals("연결끊김")){		//추가됨
					sSend.sendToAll(s3 + "에" + "_" + s1 + "동" + "_" + s2+ "호" + "_" + s4, s1);
					Thread.sleep(5);	
					if ( i == v.size() - 1){ 
					sSend.sendToAll("finish",s1); 
					}
				}
				else{
					sSend.sendToAll(s3 + "에" + "_" + s1 + "동" + "_" + s2+ "호" + "_" + "감지", s1);
					Thread.sleep(5);	
					if ( i == v.size() - 1){ 
					sSend.sendToAll("finish",s1); 
					}
				}
			}
		}catch(ClassNotFoundException | SQLException | InterruptedException e){
			e.printStackTrace();
		}
	}
	
	void sensingRecevie(String msg){
		st = new StringTokenizer(msg,"-");
		String dong = st.nextToken();
		String ho = st.nextToken();
		String state = st.nextToken();	//감지 여부
		
		if(state.equals("2")){
			try{
				SmokingDTO smoke = new SmokingDTO();
				smoke.setDong(Integer.parseInt(dong));
				smoke.setHo(Integer.parseInt(ho));
	         
				r = ClientAction.insert(smoke);
				if(r == 1){ System.out.println("insert성공");}
				else{}
		  
			}catch(ClassNotFoundException | SQLException e){
				System.out.println("insert 과정 에러");
				e.printStackTrace();
			}
		}
		else if(state.equals("0")){	//추가됨
			try{
				SmokingDTO smoke = new SmokingDTO();
				smoke.setDong(Integer.parseInt(dong));
				smoke.setHo(Integer.parseInt(ho));
				smoke.setConnect("연결끊김");	//추가됨
	         
				r = ClientAction.insert(smoke,"연결끊김");	//추가됨
				if(r == 1){ System.out.println("insert성공");}
				else{System.out.println("Insert실패");}
			}catch(ClassNotFoundException | SQLException e){
				
			}
		}
		
		if(state.equals("0")){
			sensorLose(dong, ho);
		}
		else if(state.equals("1")){
			somkeNomal(dong, ho);
		}
		else if(state.equals("2")){
			somkeSening(dong, ho);
		}
		else
			System.out.println("error");
		
		
	} // sendsRecevie()
	char[] aptCategorize(char[] arr, int hoSize, int floorSize, String ho, char stat){
		//몇 동 몇호에 감지되면 해당 동에 대한 index값을 계산한 후, 인덱스 값에 배열 데이터를 감지유무에 따라 바꿔주는 기능
		char[] arrcp = new char[hoSize * floorSize + 1];
		int num = Integer.parseInt(ho);
		int div, quo;
		
		for(int i = 0; i < floorSize * hoSize; i++){
				arrcp[i] = arr[i];
			}
		quo = num/100 - 1;
		div = num%100 - 1;
		
		arr[(quo * hoSize) + div] = stat;
		arrcp[(quo * hoSize) + div] = stat;
		
		return arrcp;
	}
	
	char[] aptCreate(String dong, String ho, char stat){
		//몇 동에서 감지됬는지 확인해주는 기능
		if(dong.equals("101")){
			return aptCategorize(arr1, 2, 16, ho, stat);	// 배열에 현재정보저장, 동 갯수, 호 갯수, 감지상태
		}
		else if(dong.equals("201")){
			return aptCategorize(arr3, 3, 20, ho, stat);
		}
		else if(dong.equals("301")){
			return aptCategorize(arr3, 4, 30, ho, stat);
		}
		else
			return null;
	}
	
	void somkeNomal(String dong, String ho){
		//감지된 연기 없을 때 현재 시간과 해당 동에 □ 배열 그림 보내주는 기능
		char[] arr;
		
		try {
			String time = RealTime.realTime();
			System.out.println(time +"/" + dong + "동" + ho + "호" + " 감지된 연기가 없습니다.");
			arr = aptCreate(dong, ho, '□');
			
			sSend.sendToAll(arr, dong, time +"/" + dong + "동" + ho + "호" + " 감지된 연기가 없습니다.", ho, '□');
		} catch (IOException e) { 
			System.out.println("somkeNomal 메소드 문제");
			e.printStackTrace(); 
			}
	}
	
	void somkeSening(String dong, String ho){
		char[] arr;
		
		try {
			String time = RealTime.realTime();
			System.out.println(time +"/" + dong + "동" + ho + "호" + " 연기가 감지되었습니다.!!");
			arr = aptCreate(dong, ho, '■');
			sSend.sendToAll(arr, dong, time +"/" + dong + "동" + ho + "호" + " 연기가 감지되었습니다.!!", ho, '■');
		} catch (IOException e) { 
			System.out.println("somkeSening 메소드 문제");
			e.printStackTrace(); 
			}
	}
	
	void sensorLose(String dong, String ho){
		char[] arr;
		
		try {
			String time = RealTime.realTime();
			System.out.println(time +"/" + dong + "동" + ho + "호" + " 연결이 끊겼습니다.");
			arr = aptCreate(dong, ho, 'X');
			sSend.sendToAll(arr, dong, time +"/" + dong + "동" + ho + "호" + " 연결이 끊겼습니다.", ho, 'X');
		} catch (IOException e) { 
			System.out.println("sensorLose 메소드 문제");
			e.printStackTrace(); 
		}
	}//sensorLose()
	
	void showClientList(){
		//접속한 IP주소 텍스트 에어리어에 보여주는 기능
		MainGUIManager.setList();
		Iterator itr = ClientList.clientTable.iterator();
		
		while(itr.hasNext()){
			Socket s = (Socket)itr.next();
			MainGUIManager.writeListText(s.getInetAddress() + "");
		}
	}
	
	void showPiList(){
		MainGUIManager.setPiList();
		Iterator itr = ClientList.piTable.iterator();
		
		while(itr.hasNext()){
			Socket s = (Socket)itr.next();
			MainGUIManager.writePiListText(s.getInetAddress() + "");
		}
	}
}
