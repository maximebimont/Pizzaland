package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
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
			String query = "SELECT * FROM commande WHERE cid = ? ";
			String queryPizzas = "SELECT * FROM commandefini AS c JOIN pizzas AS p ON c.cid = p.pid"
					+ " WHERE c.cid = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			PreparedStatement psp = con.prepareStatement(queryPizzas);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				psp.setInt(1, id);
				ResultSet rsp = psp.executeQuery();
				while (rsp.next()) {
					pizzas.add(dao.find(rsp.getInt("pid")));
				}
				commande = new Commande(id, rs.getInt("uid"), rs.getString("date"), pizzas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commande;
	}

	public List<Commande> findAll() {
		List<Commande> list = new ArrayList<Commande>();
		String query = "SELECT * FROM commande";
		String queryPizza = "SELECT * FROM commandefini AS c JOIN pizzas AS p ON c.pid = p.pid WHERE c.cid = ?";
		int cid, uid;
		String date;
		List<Pizza> pizzas;

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			PreparedStatement psp = con.prepareStatement(queryPizza);
			while (rs.next()) {
				cid = rs.getInt(1);
				uid = rs.getInt(2);
				date = rs.getString(3);
				pizzas = new ArrayList<Pizza>();
				psp.setInt(1, cid);
				ResultSet rsp = psp.executeQuery();
				while (rsp.next()) {
					pizzas.add(dao.find(rsp.getInt("pid")));
				}
				list.add(new Commande(cid, uid, date, pizzas));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public void save(Commande commande) {
		try {
			String query = "INSERT INTO commande VALUES(  ? , ? , ? )";
			String queryPizzas = "INSERT INTO commandefini VALUES( ? , ? )";
			PreparedStatement ps = con.prepareStatement(query);
			PreparedStatement psp = con.prepareStatement(queryPizzas);
			ps.setInt(1, commande.getCid());
			ps.setInt(2, commande.getUid());
			ps.setString(3, commande.getDate());
			ps.executeUpdate();
			for (Pizza pizza : commande.getPizzas()) {
				psp.setInt(1, commande.getCid());
				psp.setInt(2, pizza.getId());
				psp.executeUpdate();
			}
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

}
