package SmokeServer;

import java.io.Serializable;
import java.sql.Date;
import java.util.Vector;

public class SmokingDTO {			// DB에 정보를 저장하고 값을 꺼내오기 위해 필요한 클래스
	
	private String date;		// 감지 날짜 가져오기 위한 멤버변수
	private int dong;		//동
	private int ho;			//호수
	private String connect;  //연결 여부 
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDong() {
		return dong;
	}

	public void setDong(int dong) {
		this.dong = dong;
	}

	public int getHo() {
		return ho;
	}

	public void setHo(int ho) {
		this.ho = ho;
	}

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

	

}
