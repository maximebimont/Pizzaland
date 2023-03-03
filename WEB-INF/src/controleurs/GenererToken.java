package controleurs;

import java.io.IOException;
import java.io.PrintWriter;
//import java.util.UUID;
//import java.util.Base64;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DS;

@WebServlet("/token/*")
public class GenererToken extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.println("GET d'un USER");

		CreateToken(req, out);
	}

	public void CreateToken(HttpServletRequest req, PrintWriter out) {
		// Créé Token pour sécurité

		// Vérification existance dans la bdd
		try (Connection con = new DS().getConnection()) {

			// Récupération login et mdp
			String login = req.getParameter("login");
			String pwd = req.getParameter("pwd");
			out.println("login: " + login + "; pwd: " + pwd);

			Statement stmt = con.createStatement();
			String query = "select * from utilisateur where (login='" + login + "') AND (pwd=MD5('" + pwd + "'))";
			out.println("SELECT: " + query);
			ResultSet rs = stmt.executeQuery(query);

			// Si il existe :
			if (rs.next()) {
				out.println("USER existe !");
				// On créé le token
				String token = JwtManager.createJWT(rs.getInt("uid"), login);
				out.println("Token créé: " + token);
			} else {
				out.println("USER n'existe pas !");
			}

		} catch (SQLException e) {
			out.println("ERREUR: " + e);
		}
	}
}
