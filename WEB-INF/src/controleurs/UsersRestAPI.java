package controleurs;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.JwtManager;
import io.jsonwebtoken.Claims;

@WebServlet("/users/*")
public class UsersRestAPI extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=utf-8");
		String path = req.getPathInfo();
		PrintWriter out = resp.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		if (path == null || path.equals("/")) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String[] parts = path.split("/");
		if (parts[1].equals("token")) {
			out.println(JwtManager.createJWT("loic"));
			return;
		} else {
			String authTokenHeader = req.getHeader("Authorization");
			String token = authTokenHeader.substring("Bearer".length()).trim();
			try {
				Claims claims = JwtManager.decodeJWT(token);
				System.out.println("CA MARCHE OMG");
			} catch (Exception e) {
				System.out.println("Token cassé");
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}

}