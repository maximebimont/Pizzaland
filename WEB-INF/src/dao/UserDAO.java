package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
private Connection con;
	
	public UserDAO(Connection con) {
		this.con = con;
	}
	
	public UserDAO() {
		try{
			Class.forName("org.postgresql.Driver");
		} catch(Exception e) {
			e.printStackTrace();
		}
		String url = "jdbc:postgresql://psqlserv/but2";
        String nom = "maximebimontetu";
        String mdp = "moi";
		try {
			con = DriverManager.getConnection(url,nom,mdp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean verifyLogin(String login, String password) {
		boolean correctLogin = false;
		try {
			String sql = "SELECT * FROM utilisateur WHERE login = ? AND pwd = ? ";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, login);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				correctLogin = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return correctLogin;
	}
	
	public boolean verifyRole(String login, String password) {
		boolean isAdmin = false;
		try {
			String sql = "SELECT role FROM utilisateur WHERE login = ? AND pwd = ? ";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, login);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String role = rs.getString("role");
				if (role.equals("admin")) isAdmin = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isAdmin;
	}
}
