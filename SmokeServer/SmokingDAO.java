package SmokeServer;

import java.sql.Connection; 
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SmokingDAO {
	
	public Connection con;
	
	public SmokingDAO() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");		//JDBC 드라이버 로드
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/smoke", "root", "dytc1234");
		// DB 서버 연결
		
	}	// 생성자로 JDBC 연결
	
	
}
