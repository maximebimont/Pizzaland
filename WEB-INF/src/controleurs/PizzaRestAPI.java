package controleurs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.PizzaDAO;
import dto.Pizza;

@WebServlet("/pizzas/*")
public class PizzaRestAPI extends HttpServlet {
	
	PizzaDAO dao = new PizzaDAO();
	
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
        if (splits.length != 2 && (splits.length != 3 || (splits.length == 3 && !splits[2].equals("name")))) {
	        res.sendError(HttpServletResponse.SC_BAD_REQUEST);
	        return;
        }
        
        String id = splits[1];
		if (dao.find(Integer.parseInt(id))==null) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND); 
			return;
		}
		if(splits.length == 2) {
			out.print(objectMapper.writeValueAsString(dao.find(Integer.parseInt(id))));
		} else {
			Pizza pizza = dao.find(Integer.parseInt(id));
			out.print(pizza.getName());
		}
        return;
    }
	
//	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
////		PrintWriter out = res.getWriter();
////        res.setContentType("applications/json");
////        
////        ObjectMapper objectMapper = new ObjectMapper();
////        String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
////        Ingredient newIngredient = objectMapper.readValue(data, Ingredient.class);
////        if(dao.findById(newIngredient.getId()) != null || dao.findByName(newIngredient.getName()) != null) {
////        	res.sendError(HttpServletResponse.SC_CONFLICT);
////        	return;
////        }
////        dao.save(newIngredient);
////        out.print(data);
////        out.close();
//	}
//	
//	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
////		PrintWriter out = res.getWriter();
////        res.setContentType("applications/json");
////        
////        String pathInfo = req.getPathInfo();
////        if (pathInfo == null || pathInfo.equals("/")) {
////        	res.sendError(HttpServletResponse.SC_BAD_REQUEST);
////        	return;
////        }
////        
////        String[] splits = pathInfo.split("/");
////        if (splits.length != 2) {
////	        res.sendError(HttpServletResponse.SC_BAD_REQUEST);
////	        return;
////        }
////        
////        String id = splits[1];
////		if (dao.findById(Integer.parseInt(id))==null) {
////			res.sendError(HttpServletResponse.SC_NOT_FOUND);
////			return;
////		}
////		dao.remove(Integer.parseInt(id));
////		out.print("La donnée a bien été supprimée !");
////		out.close();
//	}
//	
//	public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		
//	}

}
