package controleurs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.UserDAO;
import dto.User;

@WebServlet("/users/*")
public class UserRestAPI extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType(("application/json;charset=UTF-8"));

		PrintWriter out = resp.getWriter();
		ObjectMapper om = new ObjectMapper();

		UserDAO dao = new UserDAO();

		String json = om.writeValueAsString(dao.findAll());
		out.println(json);
	}

	protected void verifyBasicToken(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String authHeader = req.getHeader("Authorization");

		String login = null;
		String pwd = null;
		User user = null;

		if (authHeader != null) {
			String token = authHeader.substring("Basic ".length());
			Decoder decoder = Base64.getDecoder();
			String decodedToken = new String(decoder.decode(token.getBytes()));

			String[] authData = decodedToken.split(":");

			login = authData[0];
			pwd = authData[1];

			UserDAO dao = new UserDAO();
			user = dao.find(login);
		}

		if (user == null || !user.hasPassword(pwd)) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}
