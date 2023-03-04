package controleurs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DS;

@WebServlet("/users/token")
public class UserRestAPI extends HttpServlet {

	Connection con;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("application/json;charset=UTF-8");
		PrintWriter out = res.getWriter();
		String login = req.getParameter("login");
		String pwd = req.getParameter("pwd");

		try {
			con = new DS().getConnection();
			String query = "select * from utilisateur where login = ? and pwd = ?";
			PreparedStatement ps = con.prepareStatement(query);

			if (login == null || pwd == null) {
				ps.setString(1, "login");
				ps.setString(2, "pwd");
			} else {
				ps.setString(1, login);
				ps.setString(2, pwd);
			}

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String s = login + ":" + pwd;
				String encodedString = Base64.getEncoder().encodeToString(s.getBytes());
				out.println("Token : " + encodedString);
			} else
				out.println("Vous êtes inconnu ici !");

		} catch (Exception e) {
			e.printStackTrace();
		}
		out.close();
	}

	public boolean verifToken(String token) {
		byte[] decodedBytes = Base64.getDecoder().decode(token);
		String decodedString = new String(decodedBytes);
		String[] splits = decodedString.split(":");
		String login = splits[0];
		String pwd = splits[1];
		
		try {
			con = new DS().getConnection();
			String query = "select * from utilisateur where login = ? and pwd = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, login);
			ps.setString(2, pwd);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
