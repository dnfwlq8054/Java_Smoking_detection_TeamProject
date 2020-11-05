package SmokeServer;

public class MainServer {

	public static void main(String[] args) {
		System.out.println("서버 준비중입니다. 잠시만 기다려주세요..");
		new MainGUIManager();
		MainGUIManager.writeRecordText("서버 준비가 완료됐습니다.");
		SocketAccept socketAccept = new SocketAccept();
		socketAccept.start();	//클라이언트 접속을 받는 쓰레드 시작
		return;
	}

}

