package controleurs;

import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import dto.User;

@WebServlet("/generateBasic")
public class GenerateBasic extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDAO studentDAO = new UserDAO();

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        User student = studentDAO.find(login);
        if (student != null && student.hasPassword(password)) {
            Encoder encoder = Base64.getEncoder();
            String token = encoder.encodeToString((student.getLogin() + ":" + student.getPassword()).getBytes());

            req.setAttribute("token", token);
            req.getRequestDispatcher("showToken.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("unknownUser.html");
        }
    }

}