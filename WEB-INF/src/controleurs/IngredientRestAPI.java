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

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("application/json;charset=UTF-8");
		PrintWriter out = res.getWriter();
		ObjectMapper objectMapper = new ObjectMapper();

		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			Collection<Ingredient> models = dao.findAll();
			String jsonstring = objectMapper.writeValueAsString(models);
			out.print(jsonstring + "\n");
			return;
		}

		String[] splits = pathInfo.split("/");
		if (splits.length != 2) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST); // 400
			return;
		}
		int id = Integer.parseInt(splits[1]);
		if (dao.findById(id) == null) {
			System.out.println("pas trouve");
			res.sendError(HttpServletResponse.SC_NOT_FOUND); // 404
			return;
		}
		String jsonstring2 = objectMapper.writeValueAsString(dao.findById(id));
		out.print(jsonstring2 + "\n");
		return;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("application/json;charset=UTF-8");
		PrintWriter out = res.getWriter();
		ObjectMapper objectMapper = new ObjectMapper();
		String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
		Ingredient ingredient = objectMapper.readValue(data, Ingredient.class);
		if (dao.findById(ingredient.getId()) != null || dao.findByName(ingredient.getName()) != null) {
			res.sendError(HttpServletResponse.SC_CONFLICT); // 409
			return;
		}
		dao.save(ingredient);
		out.print(data);
		out.close();
	}
}
