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

	CommandeDAO dao = new CommandeDAO();
	PizzaDAO pDao = new PizzaDAO();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

		ObjectMapper objectMapper = new ObjectMapper();
		String pathInfo = req.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			Collection<Commande> commandes = dao.findAll();
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
		if (dao.find(Integer.parseInt(id)) == null) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (splits.length == 2) {
			out.print(objectMapper.writeValueAsString(dao.find(Integer.parseInt(id))));
		} else {
			Commande commande = dao.find(Integer.parseInt(id));
			out.print(commande.getPrice());
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("applications/json");

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
			if (dao.find(newCommande.getCid()) != null) {
				res.sendError(HttpServletResponse.SC_CONFLICT);
				return;
			}
			dao.save(newCommande);
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
		Pizza newPizza = objectMapper.readValue(data, Pizza.class);
		int pizzaID = newPizza.getId();
		if (pDao.find(pizzaID) == null) {
			System.out.println(pizzaID);
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		dao.addPizza(Integer.parseInt(id), pizzaID);
		out.print("La donnée a bien été ajoutée !");
		out.close();
	}
}
