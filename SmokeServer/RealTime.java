package SmokeServer;

import java.io.IOException;
import java.util.Calendar;

public class RealTime {
	
	static String realTime() throws IOException{		//현재 시간 알려주는 메소드
		String date = NowCalendar.nowDate();
		String time = NowCalendar.nowTime();
		
		return date + "/" + time;
	}
}

class NowCalendar {
	static Calendar c;		//Calendar 선언
	
	static String nowDate(){	//오늘 날짜 리턴
		c = Calendar.getInstance();		//Calendar 객체 생성 new 키워드 안함
		String date = c.get(Calendar.YEAR) + "";	//시스템에서 년 받아옴
		
		int month = (c.get(Calendar.MONTH)+1); //월 받아옴 0부터 반환하기 때문에 1을 더함
		int day = c.get(Calendar.DATE);		//일 받아옴
		
		if(month < 10)			//월이 10보다 작으면
			date+= "-0" + month;		//1~9월 앞에 0 붙임 ex)03
		else
			date+= "-" + month;		//10보다 크면 - 문자 붙임
		
		if(day < 10)		//일이 10보다 작으면
			date+= "-0" + day;		// -0문자 붙임
		else
			date+= "-" + day;		//10보다 크면 -문자만 붙임
		
		return date;		//년 월 일 문자열을 반환
	}
	
	static String nowTime(){	//현재 시간을 리턴
		c = Calendar.getInstance();
		String time = c.get(Calendar.HOUR_OF_DAY) + "시 "		//시스템에서 시간 받아옴
				+ "";
		
		int minute = c.get(Calendar.MINUTE); 	//분
		int second = c.get(Calendar.SECOND);	//초
		
		if(minute < 10)				//분이 10보다 작으면
			time+= "0" + minute + "분 ";		//앞에 0붙이고 더함
		else
			time+= minute + "분 ";			//크면 걍 붙임
		
		if(second < 10)
			time+= "0"+ second + "초";
		else
			time+= second + "초";
		
		return time;
	}
}
