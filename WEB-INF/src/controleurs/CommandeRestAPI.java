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

import dao.CommandeDAO;
import dto.Commande;

@WebServlet("/commandes/*")
public class CommandeRestAPI extends HttpServlet {

	CommandeDAO dao = new CommandeDAO();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
        res.setContentType("applications/json");
        
        ObjectMapper objectMapper = new ObjectMapper();
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
        	Collection<Commande> commandes = dao.findAll();
        	String jsonstring = objectMapper.writeValueAsString(commandes);
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
			Commande commande = dao.find(Integer.parseInt(id));
			if (splits[2].equals("name")){
				out.print(commande.getCid());
			} else {
				out.print(commande.getPrice());
			}
		}
        return;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
        res.setContentType("applications/json");
        
        ObjectMapper objectMapper = new ObjectMapper();
        String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
        Commande newCommande = objectMapper.readValue(data, Commande.class);
        if(dao.find(newCommande.getCid()) != null) {
        	res.sendError(HttpServletResponse.SC_CONFLICT);
        	return;
        }
        dao.save(newCommande);
        out.print("La donnée a bien été ajoutée !");
        out.close();
	}

}
