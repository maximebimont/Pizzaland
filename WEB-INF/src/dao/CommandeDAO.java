package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Commande;
import dto.Ingredient;
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
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		try {
			String queryCommande = "SELECT * FROM commande WHERE cid = ? ";
			String queryPizzas = "SELECT * FROM commandefini AS c JOIN pizzas AS p ON c.cid = p.pid"
					+ " WHERE c.cid = ? ";
			PreparedStatement psCommande = con.prepareStatement(queryCommande);
			PreparedStatement psPizzas = con.prepareStatement(queryPizzas);
			psCommande.setInt(1, id);
			ResultSet rsCommande = psCommande.executeQuery();
			if (rsCommande.next()) {
				psPizzas.setInt(1, id);
				ResultSet rsPizzas = psPizzas.executeQuery();
				while (rsPizzas.next()) {
					pizzas.add(dao.find(rsPizzas.getInt("pid")));
				}
				commande = new Commande(id, rsCommande.getInt("uid"), rsCommande.getString("date"), pizzas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commande;
	}

	public List<Commande> findAll() {
		List<Commande> list = new ArrayList<Commande>();
		String queryCommande = "SELECT * FROM commande";
		String queryPizza = "SELECT * FROM commandefini AS c JOIN pizzas AS p ON c.pid = p.pid WHERE c.cid = ?";
		int cid, uid;
		String date;
		List<Pizza> pizzas;

		try {
			Statement st = con.createStatement();
			ResultSet rsCommande = st.executeQuery(queryCommande);
			PreparedStatement psPizzas = con.prepareStatement(queryPizza);
			while (rsCommande.next()) {
				cid = rsCommande.getInt(1);
				uid = rsCommande.getInt(2);
				date = rsCommande.getString(3);
				pizzas = new ArrayList<Pizza>();
				psPizzas.setInt(1, cid);
				ResultSet rsPizzas = psPizzas.executeQuery();
				while (rsPizzas.next()) {
					pizzas.add(dao.find(rsPizzas.getInt("pid")));
				}
				list.add(new Commande(cid, uid, date, pizzas));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public void post(Commande commande) {
		try {
			String queryCommande = "INSERT INTO commande VALUES(  ? , ? , ? )";
			String queryPizzas = "INSERT INTO commandefini VALUES( ? , ? )";
			PreparedStatement psCommande = con.prepareStatement(queryCommande);
			PreparedStatement psPizzas = con.prepareStatement(queryPizzas);
			psCommande.setInt(1, commande.getCid());
			psCommande.setInt(2, commande.getUid());
			psCommande.setString(3, commande.getDate());
			psCommande.executeUpdate();
			for (Pizza pizza : commande.getPizzas()) {
				psPizzas.setInt(1, commande.getCid());
				psPizzas.setInt(2, pizza.getId());
				psPizzas.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try {
			String queryCommande = "DELETE FROM commande WHERE cid = ? ";
			String queryPizzas = "DELETE FROM commandefini WHERE cid = ? ";
			PreparedStatement psCommande = con.prepareStatement(queryCommande);
			PreparedStatement psPizzas = con.prepareStatement(queryPizzas);
			psCommande.setInt(1, id);
			psPizzas.setInt(1, id);
			psPizzas.executeUpdate();
			psCommande.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPizza(int cid, int pid) {
		try {
			String query = "INSERT INTO commandefini VALUES ( ? , ? )";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, cid);
			ps.setInt(2, pid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasPizza(int cid, int pid) {
		try {
			String query = "SELECT FROM commandefini WHERE cid = ? AND pid = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, cid);
			ps.setInt(2, pid);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void removePizza(int cid, int pid) {
		try {
			String query = "DELETE FROM commandefini WHERE cid = ? AND pid = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, cid);
			ps.setInt(2, pid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
