package SmokeServer;

import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;


interface ClientList {
	Vector<Socket> clientTable = new Vector<Socket>(5);	//접속한 클라이언트 모두 담는 객체
	Vector<Socket> clientTable1 = new Vector<Socket>(5);	//1동 클라이언트
	Vector<Socket> clientTable2 = new Vector<Socket>(5);	
	Vector<Socket> clientTable3 = new Vector<Socket>(5);
	Vector<Socket> piTable = new Vector<Socket>(5);	//접속한 파이 모두 담는 객체
	Hashtable<String, Vector<Socket>> cTable = new Hashtable<String, Vector<Socket>>(5);
	//1동2동3동 Vector 정보 담는 해쉬 테이블 객체
	//키 값은 동, 값은 Socket Vector.
}
