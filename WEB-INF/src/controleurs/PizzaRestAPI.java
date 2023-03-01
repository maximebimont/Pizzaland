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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.IngredientDAO;
import dao.PizzaDAO;
import dto.Ingredient;
import dto.Pizza;

@WebServlet("/pizzas/*")
public class PizzaRestAPI extends HttpServlet {

	PizzaDAO dao = new PizzaDAO();
	IngredientDAO ingDao = new IngredientDAO();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		ObjectMapper objectMapper = new ObjectMapper();
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			Collection<Pizza> pizzas = dao.findAll();
			String jsonstring = objectMapper.writeValueAsString(pizzas);
			out.print(jsonstring);
			return;
		}

		String[] splits = pathInfo.split("/");
		if (splits.length != 2 && (splits.length != 3
				|| (splits.length == 3 && (!splits[2].equals("name") && !splits[2].equals("finalprice"))))) {
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
			Pizza pizza = dao.find(Integer.parseInt(id));
			if (splits[2].equals("name")) {
				out.print(pizza.getName());
			} else {
				double price = pizza.getPrice();
				for (Ingredient i : pizza.getIngredients()) {
					price += i.getPrice();
				}
				out.print(price);
			}
		}
		return;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
			Pizza newPizza = null;
			try {
				newPizza = objectMapper.readValue(data, Pizza.class);
			} catch (JsonParseException e) {
				System.out.println(e.getMessage());
			}
			if (dao.find(newPizza.getId()) != null) {
				res.sendError(HttpServletResponse.SC_CONFLICT);
				return;
			}
			dao.save(newPizza);
			out.print(data);
			return;
		}
		String[] splits = pathInfo.split("/");
		if (splits.length != 2) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String id = splits[1];
		try {
			Integer.parseInt(id);
		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if (dao.find(Integer.parseInt(id)) == null) {
			System.out.println(id);
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
		Ingredient newIngredient = objectMapper.readValue(data, Ingredient.class);
		int ingredientID = newIngredient.getId();
		if (ingDao.find(ingredientID) == null) {
			System.out.println(ingredientID);
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (dao.hasIng(Integer.parseInt(id), ingredientID)) {
			res.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}
		dao.addIng(Integer.parseInt(id), ingredientID);
		out.print("La donnée a bien été ajoutée !");
		out.close();
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String[] splits = pathInfo.split("/");
		if (splits.length != 2 && splits.length != 3) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String id = splits[1];
		try {
			Integer.parseInt(id);
		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if (dao.find(Integer.parseInt(id)) == null) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (splits.length == 2) {
			dao.remove(Integer.parseInt(id));
		}
		if (splits.length == 3) {
			int ingredientID = 0;
			try {
				ingredientID = Integer.parseInt(splits[2]);
			} catch (Exception e) {
				res.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			if (ingDao.find(ingredientID) == null) {
				System.out.println(ingredientID);
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			if (!dao.hasIng(Integer.parseInt(id), ingredientID)) {
				res.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			dao.removeIng(Integer.parseInt(id), ingredientID);
		}
		out.print("La donnée a bien été supprimée !");
		out.close();
	}

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (req.getMethod().equalsIgnoreCase("PATCH")) {
			doPatch(req, res);
		} else {
			super.service(req, res);
		}
	}

	public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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
		try {
			Integer.parseInt(id);
		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if (dao.find(Integer.parseInt(id)) == null) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
		double price = -1;
		try {
			price = objectMapper.readValue(data, Double.class);
		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		dao.updatePrice(Integer.parseInt(id), price);
		out.println("La donnée a bien été modifiée !");
		out.close();
	}

}
