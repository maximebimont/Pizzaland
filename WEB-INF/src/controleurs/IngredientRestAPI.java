package controleurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.IngredientDAO;
import dto.Ingredient;

@SuppressWarnings("serial")
@WebServlet("/ingredients/*")
public class IngredientRestAPI extends HttpServlet {

	IngredientDAO dao = new IngredientDAO();

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();

		// Token pour sécurité
		String token = req.getParameter("token");

		if (req.getMethod().equalsIgnoreCase("GET")) {
			doGet(req, res);
		} else if (JwtManager.tokenValid(token)) {
			if (req.getMethod().equalsIgnoreCase("POST")) {
				post(req, res);
			} else if (req.getMethod().equalsIgnoreCase("DELETE")) {
				delete(req, res);
			} else {
				super.service(req, res);
			}
		} else {
			out.println("TOKEN incorrecte !");
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		ObjectMapper objectMapper = new ObjectMapper();
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			Collection<Ingredient> ingredients = dao.findAll();
			String jsonstring = objectMapper.writeValueAsString(ingredients);
			out.print(jsonstring);
			return;
		}

		String[] splits = pathInfo.split("/");
		if (splits.length != 2 && (splits.length != 3 || (splits.length == 3 && !splits[2].equals("name")))) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String id = splits[1];
		if (dao.find(Integer.parseInt(id)) == null) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (splits.length == 2) {
			out.print(objectMapper.writeValueAsString(dao.find(Integer.parseInt(id))));
		} else {
			Ingredient ing = dao.find(Integer.parseInt(id));
			out.print(ing.getName());
		}
		return;
	}

	public void post(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		ObjectMapper objectMapper = new ObjectMapper();
		String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
		Ingredient newIngredient = objectMapper.readValue(data, Ingredient.class);
		if (dao.find(newIngredient.getId()) != null || dao.find(newIngredient.getName()) != null) {
			res.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}
		dao.save(newIngredient);
		out.print("La donnée a bien été ajoutée !");
		out.close();
	}

	public void delete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String[] splits = pathInfo.split("/");
		if (splits.length != 2) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String id = splits[1];
		if (dao.find(Integer.parseInt(id)) == null) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		dao.remove(Integer.parseInt(id));
		out.print("La donnée a bien été supprimée !");
		out.close();
	}
}
