package controleurs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.UserDAO;

@WebServlet("/users")
public class UserRestAPI extends HttpServlet {
	UserDAO dao = new UserDAO();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
        res.setContentType("applications/json");
        
        String login = req.getParameter("login");
        String password = req.getParameter("pwd");
        
        if(login==null || password==null) {
        	res.sendError(HttpServletResponse.SC_BAD_REQUEST);
	        return;
        }
        
        if(!dao.verifyLogin(login, password)) {
        	res.sendError(HttpServletResponse.SC_NOT_FOUND);
	        return;
        }
        
        String fullInput = login + ":" + password;
        String token = Base64.getEncoder().encodeToString(fullInput.getBytes());
        out.println(token);
	}
}