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

import dao.CommandeDAO;
import dao.PizzaDAO;
import dto.Commande;
import dto.Pizza;

@WebServlet("/commandes/*")
public class CommandeRestAPI extends HttpServlet {

	CommandeDAO cDao = new CommandeDAO();
	PizzaDAO pDao = new PizzaDAO();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		ObjectMapper objectMapper = new ObjectMapper();
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			Collection<Commande> commandes = cDao.findAll();
			System.out.println(commandes);
			String jsonstring = objectMapper.writeValueAsString(commandes);
			out.print(jsonstring);
			return;
		}

		String[] splits = pathInfo.split("/");
		if (splits.length != 2 && (splits.length != 3 || !splits[2].equals("finalprice"))) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String id = splits[1];
		if (cDao.find(Integer.parseInt(id)) == null) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (splits.length == 2) {
			out.print(objectMapper.writeValueAsString(cDao.find(Integer.parseInt(id))));
		} else {
			Commande commande = cDao.find(Integer.parseInt(id));
			out.print(commande.getPrice());
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		String authorization = req.getHeader("Authorization");
		if (authorization == null || !authorization.startsWith("Basic")) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		UserRestAPI users = new UserRestAPI();
		String token = authorization.substring("Basic".length()).trim();
		if (!users.verifToken(token)) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
			Commande newCommande = null;
			try {
				newCommande = objectMapper.readValue(data, Commande.class);
			} catch (JsonParseException e) {
				System.out.println(e.getMessage());
			}
			if (cDao.find(newCommande.getCid()) != null) {
				res.sendError(HttpServletResponse.SC_CONFLICT);
				return;
			}
			cDao.post(newCommande);
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
		if (cDao.find(Integer.parseInt(id)) == null) {
			System.out.println(id);
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
		Pizza newPizza = objectMapper.readValue(data, Pizza.class);
		int pizzaID = newPizza.getId();
		if (pDao.find(pizzaID) == null) {
			System.out.println(pizzaID);
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		cDao.addPizza(Integer.parseInt(id), pizzaID);
		out.println("The data has been added successfully !");
		out.close();
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		String authorization = req.getHeader("Authorization");
		if (authorization == null || !authorization.startsWith("Basic")) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		UserRestAPI users = new UserRestAPI();
		String token = authorization.substring("Basic".length()).trim();
		if (!users.verifToken(token)) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

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
		if (cDao.find(Integer.parseInt(id)) == null) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (splits.length == 2) {
			cDao.delete(Integer.parseInt(id));
		}
		if (splits.length == 3) {
			int pizzaID = 0;
			try {
				pizzaID = Integer.parseInt(splits[2]);
			} catch (Exception e) {
				res.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			if (pDao.find(pizzaID) == null) {
				System.out.println(pizzaID);
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			if (!cDao.hasPizza(Integer.parseInt(id), pizzaID)) {
				res.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			cDao.removePizza(Integer.parseInt(id), pizzaID);
		}
		out.println("The data has been removed successfully !");
		out.close();
	}
}
