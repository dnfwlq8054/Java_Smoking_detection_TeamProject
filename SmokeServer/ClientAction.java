package SmokeServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ClientAction {
	
	//서버의 select로 만들어 반환되는 v를 클라이언트로 송신.
	
	public static Vector<SmokingDTO> select()throws SQLException, ClassNotFoundException {
			SmokingDAO dao = new SmokingDAO();
			String sql = "select * from smoke";
			PreparedStatement pstmt = dao.con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			Vector<SmokingDTO> v = new Vector<SmokingDTO>();
			
			while(rs.next()){
				SmokingDTO smoke = new SmokingDTO();		//행 갯수 만큼 객체가 만들어진다.
				
				smoke.setDong(rs.getInt("dong"));
				smoke.setHo(rs.getInt("ho"));
				smoke.setDate(rs.getString("wdate"));
				smoke.setConnect(rs.getString("connect"));	//추가됨

				v.add(smoke);
			}
			return v;
	}
	
	public static Vector<SmokingDTO> selWhere(String dong) throws SQLException, ClassNotFoundException {
		SmokingDAO dao = new SmokingDAO();
		String sql = "select * from smoke where dong = " + dong;
		PreparedStatement pstmt = dao.con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		Vector<SmokingDTO> v = new Vector<SmokingDTO>();
		
		while(rs.next()){
			SmokingDTO smoke = new SmokingDTO();		//행 갯수 만큼 객체가 만들어진다.
			
			smoke.setDong(rs.getInt("dong"));
			smoke.setHo(rs.getInt("ho"));
			smoke.setDate(rs.getString("wdate"));
			smoke.setConnect(rs.getString("connect"));	//추가됨
			
			v.add(smoke);
		}
		return v;
		
	}
	
	public static int insert(SmokingDTO smoke,String connect)throws SQLException, ClassNotFoundException{
		int r = 0;
		SmokingDAO dao = new SmokingDAO();
		String sql = "insert into smoke(dong, ho,connect, wdate) "
				+ "values ('" + smoke.getDong() + "'" + "," + "'" + smoke.getHo() + "'" 
				+ "," + "'" + connect + "'" + "," + "now())";
		
		PreparedStatement pstmt = dao.con.prepareStatement(sql);
		r = pstmt.executeUpdate();
		// Update()는 반환값이 int형이므로 insert에 성공하면 1을 반환한다.
		if(r == 1){
			System.out.println("Insert 성공");
		}
		else{ System.out.println("Insert 실패");	}
		
		return r;
	}
	
	
	public static int insert(SmokingDTO smoke)throws SQLException, ClassNotFoundException{
		int r = 0;
		SmokingDAO dao = new SmokingDAO();
		String sql = "insert into smoke(dong, ho, connect, wdate) "
				+ "values ('" + smoke.getDong() + "'" + "," + "'" + smoke.getHo() + "'" 
				+ "," + "'" + " " + "'" + ","  + "now())";
		
		PreparedStatement pstmt = dao.con.prepareStatement(sql);
		r = pstmt.executeUpdate();
		
		if(r == 1){
			System.out.println("Insert 성공");
		}
		else{ System.out.println("Insert 실패");	}
		
		return r;
	}
}
