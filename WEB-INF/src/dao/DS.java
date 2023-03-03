package dao;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DS {

	public Connection getConnection() {
		Connection con = null;
		try {
			Properties prop = new Properties();
			prop.load(getClass().getResourceAsStream("config.prop"));
			Class.forName(prop.getProperty("driver"));
			String url = prop.getProperty("url");
			String nom = prop.getProperty("login");
			String mdp = prop.getProperty("password");
			con = DriverManager.getConnection(url,nom,mdp);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return con;
	}
}
