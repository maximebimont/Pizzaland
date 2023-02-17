package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dto.Commande;
import dto.Pizza;

public class CommandeDAO {

	private Connection con;
	private PizzaDAO dao = new PizzaDAO();

	public CommandeDAO(Connection con) {
		this.con = con;
	}

	public CommandeDAO() {
		con = new DS().getConnection();
	}

	public Commande find(int id) {
		Commande commande = null;
		ArrayList<Pizza> pizzas = new ArrayList<Pizza>();
		try {
			String query = "SELECT * FROM commande WHERE cid = ? ";
			String queryPizzas = "SELECT * FROM commandefini AS c JOIN pizzas AS p ON c.pid = p.pid"
					+ " WHERE c.cid = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			PreparedStatement psp = con.prepareStatement(queryPizzas);
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				psp.setInt(1,id);
				ResultSet rsp = psp.executeQuery();
				while (rsp.next()) {
					pizzas.add(new Pizza(rsp.getInt("pid"),rsp.getString("name"),
							rsp.getString("type"),rsp.getDouble("price")));
				}
				commande = new Commande(id,rs.getInt("uid"),LocalDate.parse(rs.getString("date")),pizzas);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return commande;
	}

	public List<Commande> findAll() {
		List<Commande> list = new ArrayList<Commande>();
		String query = "SELECT * FROM commande";
		String queryPizza = "SELECT * FROM commandefini AS c JOIN pizzas AS p ON c.pid = p.pid WHERE c.pid = ?";
		int cid, uid;
		LocalDate date;
		List<Pizza> pizzas;

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			PreparedStatement psp = con.prepareStatement(queryPizza);
			while (rs.next()) {
				cid = rs.getInt(1);
				uid = rs.getInt(2);
				date = LocalDate.parse(rs.getString(3));
				pizzas = new ArrayList<Pizza>();
				psp.setInt(1, cid);
				ResultSet rsp = psp.executeQuery();
				while (rsp.next()) {
					System.out.println(rsp.getInt("pid"));
					pizzas.add(dao.find(rsp.getInt("pid")));
				}
				list.add(new Commande(cid, uid, date, pizzas));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

//	public void save(Commande commande) {
//		try {
//			String query="INSERT INTO commandes VALUES(  ? , ? , ? )";
//			String queryPizzas="INSERT INTO pizzasCommande VALUES( ? , ? )";
//			PreparedStatement ps = con.prepareStatement(query);
//			PreparedStatement psPizzas = con.prepareStatement(queryPizzas);
//			ps.setInt(1, commande.getId());
//			ps.setInt(2, commande.getUtilisateurID());
//			ps.setString(3, commande.getDateCommande().toString());
//			ps.executeUpdate();
//			for(Pizza pizza: commande.getPizzas()) {
//				psPizzas.setInt(1, commande.getId());
//				psPizzas.setInt(2, pizza.getId());
//				psPizzas.executeUpdate();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void delete(int id) {
//		try {
//			String query = "DELETE FROM commandes WHERE id = ? ";
//			String queryPizzas = "DELETE FROM pizzasCommande WHERE cid = ? ";
//			PreparedStatement ps = con.prepareStatement(query);
//			PreparedStatement psPizzas = con.prepareStatement(queryPizzas);
//			ps.setInt(1, id);
//			psPizzas.setInt(1, id);
//			psPizzas.executeUpdate();
//			ps.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void addPizza(int cid, int pid) {
//		try {
//			String query = "INSERT INTO pizzasCommande VALUES ( ? , ? )";
//			PreparedStatement ps = con.prepareStatement(query);
//			ps.setInt(1, cid);
//			ps.setInt(2, pid);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public boolean hasPizza(int cid, int pid) {
//		try {
//			String query = "SELECT FROM pizzasCommande WHERE cid = ? AND pid = ? ";
//			PreparedStatement ps = con.prepareStatement(query);
//			ps.setInt(1, cid);
//			ps.setInt(2, pid);
//			ResultSet rs = ps.executeQuery();
//			if(rs.next()) {
//				return true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//	public void removePizza(int cid, int pid) {
//		try {
//			String query = "DELETE FROM pizzasCommande WHERE cid = ? AND pid = ? ";
//			PreparedStatement ps = con.prepareStatement(query);
//			ps.setInt(1, cid);
//			ps.setInt(2, pid);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
